package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.databinding.FragmentReviewBinding
import com.tokopedia.content.product.preview.utils.LoginReviewContract
import com.tokopedia.content.product.preview.utils.PAGE_SOURCE
import com.tokopedia.content.product.preview.utils.REVIEW_CREDIBILITY_APPLINK
import com.tokopedia.content.product.preview.utils.REVIEW_FRAGMENT_TAG
import com.tokopedia.content.product.preview.view.adapter.review.ReviewParentAdapter
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewFragment @Inject constructor(
    private val router: Router
) : TkpdBaseV4Fragment(),
    ReviewParentContentViewHolder.Listener,
    MenuBottomSheet.Listener,
    ReviewReportBottomSheet.Listener {

    private val viewModel by activityViewModels<ProductPreviewViewModel>()

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

    private val reviewAdapter by lazyThreadSafetyNone {
        ReviewParentAdapter(this)
    }

    private val snapHelper = PagerSnapHelper() // TODO: adjust pager snap helper

    private val scrollListener by lazyThreadSafetyNone {
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val index = getCurrentPosition()
                    viewModel.onAction(ProductPreviewAction.UpdateReviewPosition(index))
                }
            }
        }
    }

    private val menuResult = registerForActivityResult(
        LoginReviewContract()
    ) { loginStatus ->
        if (loginStatus) viewModel.onAction(ProductPreviewAction.ClickMenu(true))
    }

    override fun getScreenName() = REVIEW_FRAGMENT_TAG

    override fun onCreate(savedInstanceState: Bundle?) {
        childFragmentManager.addFragmentOnAttachListener { _, fragment ->
            when (fragment) {
                is MenuBottomSheet -> fragment.setListener(this)
                is ReviewReportBottomSheet -> fragment.setListener(this)
            }
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(ProductPreviewAction.FetchReview)

        setupView()
        observeReview()
        observeEvent()
    }

    private fun setupView() {
        binding.rvReview.adapter = reviewAdapter
        snapHelper.attachToRecyclerView(binding.rvReview)
        binding.rvReview.addOnScrollListener(scrollListener)
    }

    private fun observeReview() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.review.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).withCache().collectLatest { (prev, curr) ->
                renderList(prev, curr)
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiEvent
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .collect {
                    when (val event = it) {
                        is ProductPreviewEvent.ShowMenuSheet -> {
                            MenuBottomSheet.getOrCreate(childFragmentManager, requireActivity().classLoader).apply {
                                setMenu(event.status)
                            }.show(childFragmentManager)
                        }
                        is ProductPreviewEvent.LoginEvent<*> -> {
                            when (event.data) {
                                is MenuStatus -> menuResult.launch(Unit)
                            }
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun renderList(prev: List<ReviewUiModel>?, data: List<ReviewUiModel>) {
        if (prev == data) return // TODO: adjust condition
        reviewAdapter.submitList(data)
    }

    /**
     * Review Content Listener
     */
    override fun onReviewCredibilityClicked(author: AuthorUiModel) {
        val appLink = UriUtil.buildUri(REVIEW_CREDIBILITY_APPLINK, author.id, PAGE_SOURCE)
        router.route(requireContext(), appLink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvReview.removeOnScrollListener(scrollListener)
        _binding = null
    }
    override fun onMenuClicked(menu: MenuStatus) {
        viewModel.onAction(ProductPreviewAction.ClickMenu(false))
    }

    /**
     * Menu Bottom Sheet Listener
     */
    override fun onOptionClicked(menu: ContentMenuItem) {
        when (menu.type) {
            ContentMenuIdentifier.Report ->
                ReviewReportBottomSheet.getOrCreate(childFragmentManager, requireActivity().classLoader).show(childFragmentManager)
            else -> {}
        }
    }

    /**
     * Review Report Bottom Sheet Listener
     */
    override fun onReasonClicked(report: ReportUiModel) {
        viewModel.onAction(ProductPreviewAction.SubmitReport(report))
    }

    private fun getCurrentPosition(): Int {
        return (binding.rvReview.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition() ?: RecyclerView.NO_POSITION
    }

    companion object {
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ReviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(REVIEW_FRAGMENT_TAG) as? ReviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ReviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ReviewFragment
        }
    }
}
