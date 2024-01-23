package com.tokopedia.content.product.preview.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
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
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewEvent
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR

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
        object : EndlessRecyclerViewScrollListener(binding.rvReview.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.onAction(ProductPreviewAction.FetchReview(isRefresh = false))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
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

    private val likeResult = registerForActivityResult(
        LoginReviewContract()
    ) { loginStatus ->
        if (loginStatus) viewModel.onAction(ProductPreviewAction.LikeFromResult)
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
        setupView()
        observeReview()
        observeEvent()

        viewModel.onAction(ProductPreviewAction.FetchReview(isRefresh = true))
    }

    private fun setupView() {
        binding.rvReview.adapter = reviewAdapter
        snapHelper.attachToRecyclerView(binding.rvReview)
        binding.rvReview.addOnScrollListener(scrollListener)
    }

    private fun observeReview() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(
                viewLifecycleOwner.lifecycle,
                Lifecycle.State.RESUMED
            ).withCache().collectLatest { (prev, curr) ->
                renderList(prev?.reviewUiModel, curr.reviewUiModel)
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
                            MenuBottomSheet.getOrCreate(
                                childFragmentManager,
                                requireActivity().classLoader
                            ).apply {
                                setMenu(event.status)
                            }.show(childFragmentManager)
                        }

                        is ProductPreviewEvent.LoginEvent<*> -> {
                            when (event.data) {
                                is ReviewMenuStatus -> menuResult.launch(Unit)
                                is ReviewLikeUiState -> likeResult.launch(Unit)
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun renderList(prev: ReviewUiModel?, data: ReviewUiModel) {
        if (prev?.reviewContent == data.reviewContent) return

        val state = data.reviewPaging

        showLoading(state is ReviewPaging.Load)

        when (state) {
            is ReviewPaging.Success -> reviewAdapter.submitList(data.reviewContent)
            is ReviewPaging.Error -> showError(state)
            else -> {}
        }
    }

    private fun showLoading(isShown: Boolean) {
        binding.reviewLoader.showWithCondition(isShown)
        binding.rvReview.showWithCondition(!isShown)
    }

    private fun showError(state: ReviewPaging.Error) = with(binding.reviewGlobalError) {
        // TODO: why is it in dark mode.
        show()
        if (state.throwable is UnknownHostException) {
            setType(GlobalError.NO_CONNECTION)
            errorSecondaryAction.show()
            errorSecondaryAction.text =
                getString(contentcommonR.string.content_global_error_secondary_text)
            setSecondaryActionClickListener {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                router.route(requireActivity(), intent)
            }
        } else {
            errorSecondaryAction.hide()
            setType(GlobalError.SERVER_ERROR)
        }
        setActionClickListener {
            viewModel.onAction(ProductPreviewAction.FetchReview(isRefresh = true))
        }
    }

    /**
     * Review Content Listener
     */
    override fun onReviewCredibilityClicked(author: ReviewAuthorUiModel) {
        val appLink = UriUtil.buildUri(REVIEW_CREDIBILITY_APPLINK, author.id, PAGE_SOURCE)
        router.route(requireContext(), appLink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvReview.removeOnScrollListener(scrollListener)
        _binding = null
    }

    override fun onMenuClicked(menu: ReviewMenuStatus) {
        viewModel.onAction(ProductPreviewAction.ClickMenu(false))
    }

    /**
     * Menu Bottom Sheet Listener
     */
    override fun onOptionClicked(menu: ContentMenuItem) {
        when (menu.type) {
            ContentMenuIdentifier.Report ->
                ReviewReportBottomSheet.getOrCreate(
                    childFragmentManager,
                    requireActivity().classLoader
                ).show(childFragmentManager)

            else -> {}
        }
    }

    override fun onLike(status: ReviewLikeUiState) {
        viewModel.onAction(ProductPreviewAction.Like(status))
    }

    /**
     * Review Report Bottom Sheet Listener
     */
    override fun onReasonClicked(report: ReviewReportUiModel) {
        viewModel.onAction(ProductPreviewAction.SubmitReport(report))
    }

    private fun getCurrentPosition(): Int {
        return (binding.rvReview.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
            ?: RecyclerView.NO_POSITION
    }

    companion object {
        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): ReviewFragment {
            val oldInstance =
                fragmentManager.findFragmentByTag(REVIEW_FRAGMENT_TAG) as? ReviewFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                ReviewFragment::class.java.name
            ).apply {
                arguments = bundle
            } as ReviewFragment
        }
    }
}
