package com.tokopedia.content.product.preview.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.databinding.FragmentReviewBinding
import com.tokopedia.content.product.preview.utils.PAGE_SOURCE
import com.tokopedia.content.product.preview.utils.REVIEW_CREDIBILITY_APPLINK
import com.tokopedia.content.product.preview.view.adapter.review.ReviewParentAdapter
import com.tokopedia.content.product.preview.view.uimodel.AuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.ReviewUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.viewmodel.EntrySource
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModelFactory
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


class ReviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator,
    private val router: Router,
) : TkpdBaseV4Fragment(), ReviewParentContentViewHolder.Listener, MenuBottomSheet.Listener, ReviewReportBottomSheet.Listener {

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

    private val viewModel by activityViewModels<ProductPreviewViewModel> {
        viewModelFactory.create(
            EntrySource(productId = "4937529690") //TODO: Testing purpose, change from arguments
        )
    }

    private val reviewAdapter by lazyThreadSafetyNone {
        ReviewParentAdapter(this)
    }

    private val snapHelper = PagerSnapHelper() //TODO: adjust pager snap helper

    override fun getScreenName() = TAG

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
        setupView()
        observeReview()
        observeEvent()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onAction(ProductPreviewAction.FetchReview)
    }

    private fun setupView() {
        binding.rvReview.adapter = reviewAdapter
        snapHelper.attachToRecyclerView(binding.rvReview)
    }

    private fun observeReview() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.review.withCache().collectLatest { (prev, curr) ->
                renderList(prev, curr)
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.uiEvent.collectLatest {
                when(val event = it) {
                    is ProductPreviewEvent.ShowMenuSheet -> {
                        MenuBottomSheet.getOrCreate(childFragmentManager, requireActivity().classLoader).apply {
                            setMenu(event.menus)
                        }.show(childFragmentManager)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun renderList(prev: List<ReviewUiModel>?, data: List<ReviewUiModel>) {
        if (prev == null || prev == data) return //TODO: adjust condition
        reviewAdapter.submitList(data)
    }

    /**
     * Review Content Listener
     */
    override fun onReviewCredibilityClicked(author: AuthorUiModel) {
        val appLink = UriUtil.buildUri(REVIEW_CREDIBILITY_APPLINK, author.id, PAGE_SOURCE)
        router.route(requireContext(), appLink)
    }

    override fun onMenuClicked(menus: List<ContentMenuItem>) {
        viewModel.onAction(ProductPreviewAction.ClickMenu(menus))
    }

    /**
     * Menu Bottom Sheet Listener
     */
    override fun onOptionClicked(menu: ContentMenuItem) {
        when(menu.type) {
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

    companion object {
        const val TAG = "ReviewFragment"

        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ReviewFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? ReviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ReviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ReviewFragment
        }
    }
}
