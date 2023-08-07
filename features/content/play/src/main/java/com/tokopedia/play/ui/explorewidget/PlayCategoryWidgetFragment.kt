package com.tokopedia.play.ui.explorewidget

import android.os.Bundle
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic2
import com.tokopedia.play.databinding.FragmentPlayCategoryWidgetBinding
import com.tokopedia.play.ui.explorewidget.adapter.CategoryWidgetAdapter
import com.tokopedia.play.ui.explorewidget.viewholder.CategoryWidgetViewHolder
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.BasePlayFragment
import com.tokopedia.play.view.uimodel.ExploreWidgetState
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.action.EmptyPageWidget
import com.tokopedia.play.view.uimodel.action.FetchWidgets
import com.tokopedia.play.view.uimodel.action.NextPageWidgets
import com.tokopedia.play.view.uimodel.event.ExploreWidgetNextTab
import com.tokopedia.play.view.uimodel.getCategoryShimmering
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.play_common.util.extension.buildSpannedString
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * @author by astidhiyaa on 23/05/23
 */
class PlayCategoryWidgetFragment @Inject constructor(
    private val router: Router,
    private val trackingQueue: TrackingQueue,
    private val analyticFactory: PlayAnalytic2.Factory
) :
    BasePlayFragment() {

    private var _binding: FragmentPlayCategoryWidgetBinding? = null
    private val binding: FragmentPlayCategoryWidgetBinding get() = _binding!!

    private val categoryAdapter by lazyThreadSafetyNone {
        CategoryWidgetAdapter(object : CategoryWidgetViewHolder.Item.Listener {
            override fun onClicked(item: PlayWidgetChannelUiModel, position: Int) {
                analytic?.clickContentCard(selectedChannel = item, position = position, widgetInfo = viewModel.widgetInfo, config = viewModel.exploreWidgetConfig, type = ExploreWidgetType.Category)
                router.route(context, item.appLink)
            }

            override fun onImpressed(item: PlayWidgetChannelUiModel, position: Int) {
                analytic?.impressChannelCard(item = item, position = position, widgetInfo = viewModel.widgetInfo, config = viewModel.exploreWidgetConfig, type = ExploreWidgetType.Category)
            }
        })
    }

    private val scrollListener by lazyThreadSafetyNone {
        object : EndlessRecyclerViewScrollListener(binding.rvPlayCategoryWidget.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                viewModel.submitAction(NextPageWidgets(ExploreWidgetType.Category))
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    analytic?.scrollExplore(viewModel.widgetInfo, ExploreWidgetType.Category)
            }
        }
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(requireView(), viewLifecycleOwner) }
    )

    private val clickableSpan by lazy(LazyThreadSafetyMode.NONE) {
        object : ClickableSpan() {
            override fun updateDrawState(tp: TextPaint) {
                tp.color = MethodChecker.getColor(
                    requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                tp.isUnderlineText = false
                tp.typeface = com.tokopedia.unifyprinciples.Typography.getFontType(
                    requireContext(),
                    true,
                    com.tokopedia.unifyprinciples.Typography.DISPLAY_3
                )
            }

            override fun onClick(widget: View) {
                viewModel.submitAction(EmptyPageWidget(ExploreWidgetType.Category))
            }
        }
    }

    private var analytic: PlayAnalytic2? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayCategoryWidgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observeState()
        observeEvent()
    }

    private fun setupView() {
        binding.rvPlayCategoryWidget.addOnScrollListener(scrollListener)
        binding.rvPlayCategoryWidget.adapter = categoryAdapter

        binding.viewExploreWidgetEmpty.tvDescEmptyExploreWidget.text =
            buildSpannedString {
                append(getString(R.string.play_empty_desc_explore_widget))
                append(
                    getString(R.string.play_explore_widget_empty),
                    clickableSpan,
                    Spanned.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }

        binding.viewExploreWidgetEmpty.tvDescEmptyExploreWidget.movementMethod =
            LinkMovementMethod.getInstance()

        viewModel.submitAction(FetchWidgets(ExploreWidgetType.Category))
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { cachedState ->
                renderCards(cachedState)

                if (analytic != null || cachedState.value.channel.channelInfo.id.isBlank()) return@collectLatest
                analytic = analyticFactory.create(
                    trackingQueue = trackingQueue,
                    channelInfo = cachedState.value.channel.channelInfo
                )
            }
        }
    }

    private fun observeEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiEvent.collectLatest { event ->
                when (event) {
                    ExploreWidgetNextTab -> goToNextPage()
                    else -> {}
                }
            }
        }
    }

    private fun renderCards(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged { it.exploreWidget.category }) return

        val result = state.value.exploreWidget.category
        showEmpty(result.state is ExploreWidgetState.Empty)

        when (result.state) {
            is ExploreWidgetState.Success -> categoryAdapter.setItemsAndAnimateChanges(result.data)
            ExploreWidgetState.Loading -> categoryAdapter.setItemsAndAnimateChanges(
                getCategoryShimmering
            )

            is ExploreWidgetState.Fail -> {
                analytic?.impressToasterGlobalError()
                toaster.showToaster(
                    message = generateErrorMessage(result.state.error),
                    actionLabel = getString(R.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionListener = {
                        analytic?.clickRetryToaster()
                        run { result.state.onRetry() }
                    }
                )
            }

            else -> {}
        }
    }

    private fun showEmpty(needToShow: Boolean) {
        binding.rvPlayCategoryWidget.showWithCondition(!needToShow)
        binding.viewExploreWidgetEmpty.root.showWithCondition(needToShow)
    }

    private fun goToNextPage() {
        if (requireParentFragment() !is PlayChannelRecommendationFragment) return
        (requireParentFragment() as PlayChannelRecommendationFragment).moveTab(1)
    }

    override fun onDestroyView() {
        binding.rvPlayCategoryWidget.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }
}
