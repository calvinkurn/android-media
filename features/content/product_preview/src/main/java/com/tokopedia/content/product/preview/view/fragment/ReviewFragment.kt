package com.tokopedia.content.product.preview.view.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.UriUtil
import com.tokopedia.content.common.R as contentcommonR
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
import com.tokopedia.content.product.preview.view.uimodel.LikeUiState
import com.tokopedia.content.product.preview.view.uimodel.MenuStatus
import com.tokopedia.content.product.preview.view.uimodel.PageState
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewAction
import com.tokopedia.content.product.preview.view.uimodel.ProductPreviewEvent
import com.tokopedia.content.product.preview.view.uimodel.ReportUiModel
import com.tokopedia.content.product.preview.view.uimodel.product.ProductContentUiModel
import com.tokopedia.content.product.preview.view.viewholder.review.ReviewParentContentViewHolder
import com.tokopedia.content.product.preview.viewmodel.ProductPreviewViewModel
import com.tokopedia.content.product.preview.viewmodel.factory.ProductPreviewViewModelFactory
import com.tokopedia.content.product.preview.viewmodel.state.ReviewPageState
import com.tokopedia.content.product.preview.viewmodel.utils.EntrySource
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class ReviewFragment @Inject constructor(
    private val viewModelFactory: ProductPreviewViewModelFactory.Creator,
    private val router: Router,
) : TkpdBaseV4Fragment(), ReviewParentContentViewHolder.Listener, MenuBottomSheet.Listener,
    ReviewReportBottomSheet.Listener {

    private var _binding: FragmentReviewBinding? = null
    private val binding: FragmentReviewBinding
        get() = _binding!!

    private val arguments
        get() = (requireParentFragment() as? ProductPreviewFragment)?.productPreviewData.ifNull { ProductContentUiModel() }

    private val viewModel by viewModels<ProductPreviewViewModel> { viewModelFactory.create(EntrySource((arguments))) }

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
                            MenuBottomSheet.getOrCreate(
                                childFragmentManager,
                                requireActivity().classLoader
                            ).apply {
                                setMenu(event.status)
                            }.show(childFragmentManager)
                        }

                        is ProductPreviewEvent.LoginEvent<*> -> {
                            when (event.data) {
                                is MenuStatus -> menuResult.launch(Unit)
                                is LikeUiState -> likeResult.launch(Unit)
                            }
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun renderList(prev: ReviewPageState?, data: ReviewPageState) {
        if (prev?.reviewList == data.reviewList) return

        val state = data.pageState

        showLoading(state is PageState.Load)

        when (state) {
            is PageState.Success -> reviewAdapter.submitList(data.reviewList)
            is PageState.Error -> showError(state)
            else -> {}
        }
    }

    private fun showLoading(isShown: Boolean) {
        binding.reviewLoader.showWithCondition(isShown)
        binding.rvReview.showWithCondition(!isShown)
    }

    private fun showError(state: PageState.Error) {
        //TODO: why is it in dark mode.
        binding.reviewGlobalError.show()
        if (state.throwable is UnknownHostException) {
            binding.reviewGlobalError.setType(GlobalError.NO_CONNECTION)
            binding.reviewGlobalError.errorSecondaryAction.show()
            binding.reviewGlobalError.errorSecondaryAction.text = getString(contentcommonR.string.content_global_error_secondary_text)
            binding.reviewGlobalError.setSecondaryActionClickListener {
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                router.route(requireActivity(), intent)
            }
        } else {
            binding.reviewGlobalError.errorSecondaryAction.hide()
            binding.reviewGlobalError.setType(GlobalError.SERVER_ERROR)
        }
        binding.reviewGlobalError.setActionClickListener {
            viewModel.onAction(ProductPreviewAction.FetchReview(isRefresh = true))
        }
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
                ReviewReportBottomSheet.getOrCreate(
                    childFragmentManager,
                    requireActivity().classLoader
                ).show(childFragmentManager)

            else -> {}
        }
    }

    override fun onLike(status: LikeUiState) {
        viewModel.onAction(ProductPreviewAction.Like(status))
    }

    /**
     * Review Report Bottom Sheet Listener
     */
    override fun onReasonClicked(report: ReportUiModel) {
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
