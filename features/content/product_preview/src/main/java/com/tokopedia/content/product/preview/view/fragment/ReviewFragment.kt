package com.tokopedia.content.product.preview.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.report_content.model.ContentMenuIdentifier
import com.tokopedia.content.common.report_content.model.ContentMenuItem
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.withCache
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.analytics.ProductPreviewAnalytics
import com.tokopedia.content.product.preview.databinding.FragmentReviewBinding
import com.tokopedia.content.product.preview.utils.LoginReviewContract
import com.tokopedia.content.product.preview.utils.PAGE_SOURCE
import com.tokopedia.content.product.preview.utils.REVIEW_CREDIBILITY_APPLINK
import com.tokopedia.content.product.preview.utils.REVIEW_FRAGMENT_TAG
import com.tokopedia.content.product.preview.view.adapter.review.ReviewContentAdapter
import com.tokopedia.content.product.preview.view.listener.ReviewInteractionListener
import com.tokopedia.content.product.preview.view.listener.ReviewMediaListener
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel
import com.tokopedia.content.product.preview.view.uimodel.pager.ProductPreviewTabUiModel.Companion.TAB_REVIEW_NAME
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewMenuStatus
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewPaging
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewUiModel
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.action.ProductPreviewAction
import com.tokopedia.content.product.preview.viewmodel.event.ProductPreviewEvent
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ReviewFragment @Inject constructor(
    private val analyticsFactory: ProductPreviewAnalytics.Factory,
    private val router: Router
) : TkpdBaseV4Fragment(),
    ReviewInteractionListener,
    MenuBottomSheet.Listener,
    ReviewReportBottomSheet.Listener,
    ReviewMediaListener {

    private val viewModel by activityViewModels<ProductPreviewViewModel>()

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

    private val analytics: ProductPreviewAnalytics by lazyThreadSafetyNone {
        analyticsFactory.create(viewModel.productPreviewSource.productId)
    }

    private val reviewAdapter by lazyThreadSafetyNone {
        ReviewContentAdapter(
            reviewInteractionListener = this,
            reviewMediaListener = this
        )
    }

    private val snapHelper = PagerSnapHelper()

    private val layoutManagerContent by lazyThreadSafetyNone {
        LinearLayoutManager(requireContext(), VERTICAL, false)
    }

    private val scrollListener by lazyThreadSafetyNone {
        object : EndlessRecyclerViewScrollListener(binding.rvReview.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.onAction(ProductPreviewAction.FetchReview(isRefresh = false, page = page))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        analytics.onSwipeReviewNextContent()
                        val position = getCurrentPosition()
                        viewModel.onAction(ProductPreviewAction.ReviewContentSelected(position))
                        viewModel.onAction(ProductPreviewAction.ReviewContentScrolling(position, false))
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        val position = getCurrentPosition()
                        viewModel.onAction(ProductPreviewAction.ReviewContentScrolling(position, true))
                    }
                    else -> super.onScrollStateChanged(recyclerView, newState)
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
        initializeReviewMainData()

        setupView()

        observeReview()
        observeEvent()
    }

    private fun initializeReviewMainData() {
        viewModel.onAction(ProductPreviewAction.InitializeReviewMainData)
    }

    private fun setupView() {
        binding.rvReview.adapter = reviewAdapter
        binding.rvReview.layoutManager = layoutManagerContent
        snapHelper.attachToRecyclerView(binding.rvReview)
        binding.rvReview.removeOnScrollListener(scrollListener)
        binding.rvReview.addOnScrollListener(scrollListener)
        binding.rvReview.itemAnimator = null
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

                        is ProductPreviewEvent.ShowSuccessToaster -> {
                            if (event.type == ProductPreviewEvent.ShowSuccessToaster.Type.Report) dismissSheets()
                        }

                        is ProductPreviewEvent.ShowErrorToaster -> {
                            val view = ReviewReportBottomSheet.get(childFragmentManager)?.view?.rootView ?: requireView().rootView
                            Toaster.build(
                                view ?: return@collect,
                                text = event.message.message.ifNull { getString(event.type.textRes) },
                                actionText = getString(R.string.bottom_atc_failed_click_toaster),
                                duration = Toaster.LENGTH_LONG,
                                clickListener = {
                                    event.onClick()
                                },
                                type = Toaster.TYPE_ERROR
                            ).show()
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun dismissSheets() {
        ReviewReportBottomSheet.get(childFragmentManager)?.dismiss()
        MenuBottomSheet.get(childFragmentManager)?.dismiss()
    }

    private fun renderList(prev: ReviewUiModel?, data: ReviewUiModel) {
        if (prev == data) return

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
        errorTitle.setTextColor(getColor(requireContext(), unifyprinciplesR.color.Unify_Static_White))
        errorDescription.setTextColor(getColor(requireContext(), unifyprinciplesR.color.Unify_Static_White))

        show()
        binding.reviewLoader.hide()
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
            state.onRetry()
            hide()
            binding.reviewLoader.show()
        }
    }

    /**
     * Review Content Listener
     */
    override fun onReviewCredibilityClicked(author: ReviewAuthorUiModel) {
        analytics.onClickReviewAccountName()

        val appLink = UriUtil.buildUri(REVIEW_CREDIBILITY_APPLINK, author.id, PAGE_SOURCE)
        router.route(requireContext(), appLink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvReview.removeOnScrollListener(scrollListener)
        _binding = null
    }

    override fun onMenuClicked() {
        analytics.onClickReviewThreeDots()
        viewModel.onAction(ProductPreviewAction.ClickMenu(false))
    }

    /**
     * Review Media Listener
     */
    override fun onReviewMediaScrolled() {
        analytics.onSwipeContentAndTab(
            tabName = TAB_REVIEW_NAME,
            isTabChanged = false,
        )
    }

    override fun onPauseResumeVideo() {
        analytics.onClickPauseOrPlayVideo(ProductPreviewTabUiModel.TAB_REVIEW_KEY)
    }

    override fun onImpressedImage() {
        analytics.onImpressImage(ProductPreviewTabUiModel.TAB_REVIEW_KEY)
    }

    override fun onImpressedVideo() {
        analytics.onImpressVideo(ProductPreviewTabUiModel.TAB_REVIEW_KEY)
    }

    override fun onMediaSelected(position: Int) {
        viewModel.onAction(ProductPreviewAction.ReviewMediaSelected(position))
    }

    /**
     * Menu Bottom Sheet Listener
     */
    override fun onOptionClicked(menu: ContentMenuItem) {
        when (menu.type) {
            ContentMenuIdentifier.WatchMode -> {
                analytics.onClickReviewWatchMode()
                MenuBottomSheet.get(childFragmentManager)?.dismiss()
                viewModel.onAction(ProductPreviewAction.ToggleReviewWatchMode)
            }
            ContentMenuIdentifier.Report -> {
                analytics.onClickReviewReport()
                ReviewReportBottomSheet.getOrCreate(
                    childFragmentManager,
                    requireActivity().classLoader
                ).show(childFragmentManager)
            }
            else -> return
        }
    }

    override fun onLike(isDoubleTap: Boolean) {
        analytics.onClickLikeOrUnlike()
        viewModel.onAction(ProductPreviewAction.Like(isDoubleTap))
    }

    override fun updateReviewWatchMode() {
        viewModel.onAction(ProductPreviewAction.ToggleReviewWatchMode)
    }

    /**
     * Review Report Bottom Sheet Listener
     */
    override fun onSubmitOption(report: ReviewReportUiModel) {
        analytics.onClickSubmitReport()
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
