package com.tokopedia.play.ui.explorewidget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.content.common.util.Router
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play.R
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
import com.tokopedia.play.view.uimodel.action.RefreshWidget
import com.tokopedia.play.view.uimodel.getCategoryShimmering
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.unifycomponents.Toaster
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

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(requireView(), viewLifecycleOwner) }
    )

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

        val result = state.value.exploreWidget.category
        when (result.state) {
            is ExploreWidgetState.Success -> categoryAdapter.setItemsAndAnimateChanges(result.data)
            ExploreWidgetState.Loading -> categoryAdapter.setItemsAndAnimateChanges(getCategoryShimmering)
            is ExploreWidgetState.Fail -> {
                toaster.showToaster(
                    message = generateErrorMessage(result.state.error),
                    actionLabel = getString(R.string.play_try_again),
                    duration = Toaster.LENGTH_LONG,
                    type = Toaster.TYPE_ERROR,
                    actionListener = {
                        viewModel.submitAction(RefreshWidget)
                    }
                )
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        binding.rvPlayCategoryWidget.removeOnScrollListener(scrollListener)
        super.onDestroyView()
    }
}
