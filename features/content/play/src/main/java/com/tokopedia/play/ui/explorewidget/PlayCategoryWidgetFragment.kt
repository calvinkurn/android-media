package com.tokopedia.play.ui.explorewidget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.databinding.FragmentPlayCategoryWidgetBinding
import com.tokopedia.play.ui.explorewidget.adapter.CategoryWidgetAdapter
import com.tokopedia.play.ui.explorewidget.viewholder.CategoryWidgetViewHolder
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.fragment.BasePlayFragment
import com.tokopedia.play.view.uimodel.ExploreWidgetState
import com.tokopedia.play.view.uimodel.ExploreWidgetType
import com.tokopedia.play.view.uimodel.action.FetchWidgets
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShimmerUiModel
import com.tokopedia.play_common.model.result.NetworkResult
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * @author by astidhiyaa on 23/05/23
 */
class PlayCategoryWidgetFragment @Inject constructor(private val router: Router) :
    BasePlayFragment() {

    private var _binding: FragmentPlayCategoryWidgetBinding? = null
    private val binding: FragmentPlayCategoryWidgetBinding get() = _binding!!

    private val categoryAdapter by lazyThreadSafetyNone {
        CategoryWidgetAdapter(object : CategoryWidgetViewHolder.Item.Listener {
            override fun onClicked(item: PlayWidgetChannelUiModel) {
                router.route(context, item.appLink)
            }
        })
    }

    private val scrollListener by lazyThreadSafetyNone {
        object : EndlessRecyclerViewScrollListener(binding.rvPlayCategoryWidget.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                // viewModel.submitAction(NextPageWidgets)
            }
        }
    }

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
    }

    private fun setupView() {
        binding.rvPlayCategoryWidget.addOnScrollListener(scrollListener)
        binding.rvPlayCategoryWidget.adapter = categoryAdapter

        //TODO() temp
        viewModel.submitAction(
            FetchWidgets(ExploreWidgetType.Category)
        )
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.withCache().collectLatest { cachedState ->
                renderCards(cachedState)
            }
        }
    }

    private fun renderCards(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged { it.exploreWidget.category }) return

        when (state.value.exploreWidget.category.state) {
            is ExploreWidgetState.Success -> categoryAdapter.setItemsAndAnimateChanges(state.value.exploreWidget.category.data)
            else -> {
                categoryAdapter.setItemsAndAnimateChanges(
                    listOf(
                        PlayWidgetShimmerUiModel,
                        PlayWidgetShimmerUiModel,
                        PlayWidgetShimmerUiModel,
                        PlayWidgetShimmerUiModel,
                        PlayWidgetShimmerUiModel
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        binding.rvPlayCategoryWidget.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }
}
