package com.tokopedia.feedplus.browse.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.Router
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTracker
import com.tokopedia.feedplus.browse.presentation.adapter.CategoryInspirationAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.CategoryInspirationItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.FragmentFeedCategoryInspirationBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
internal class CategoryInspirationFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory,
    private val router: Router,
    private val tracker: FeedBrowseTracker
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedCategoryInspirationBinding? = null
    private val binding get() = _binding!!

    private val chipsListener = object : ChipsViewHolder.Listener {
        override fun onChipImpressed(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        ) {
        }

        override fun onChipClicked(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        ) {
            viewModel.onAction(CategoryInspirationAction.SelectMenu(chip))
        }

        override fun onChipSelected(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel
        ) {
            viewModel.onAction(CategoryInspirationAction.LoadData(chip))
        }
    }

    private val inspirationCardListener = object : InspirationCardViewHolder.Item.Listener {
        override fun onClicked(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            router.route(context, model.item.appLink)
        }
    }

    private val loadMoreListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return

            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            if (lastVisibleItemPosition >= adapter.itemCount - LOAD_PAGE_THRESHOLD) {
                viewModel.onAction(CategoryInspirationAction.LoadMoreData)
            }
        }
    }

    private val adapter by lazy {
        CategoryInspirationAdapter(chipsListener, inspirationCardListener)
    }

    private val viewModel by viewModels<CategoryInspirationViewModel> { viewModelFactory }

    override fun getScreenName(): String {
        return "Feed Browse Inspiration"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedCategoryInspirationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        observe()

        viewModel.onAction(CategoryInspirationAction.Init)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.header)
        binding.header.setBackgroundColor(Color.TRANSPARENT)
        binding.header.setNavigationOnClickListener {
            exitPage()
        }

        val layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = adapter.getSpanSizeLookup()
        }
        binding.rvCategoryInspiration.layoutManager = layoutManager
        binding.rvCategoryInspiration.itemAnimator = null
        binding.rvCategoryInspiration.addItemDecoration(
            CategoryInspirationItemDecoration(
                binding.rvCategoryInspiration.resources,
                layoutManager.spanCount
            )
        )
        binding.rvCategoryInspiration.addOnScrollListener(loadMoreListener)
        binding.rvCategoryInspiration.adapter = adapter
    }

    private fun exitPage() {
        tracker.sendClickBackExitEvent()
        requireActivity().finish()
    }

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    adapter.setList(state.state, state.items, state.selectedMenuId) {
                        binding.rvCategoryInspiration.invalidateItemDecorations()
                    }
                }
        }
    }

    companion object {
        private const val LOAD_PAGE_THRESHOLD = 2
    }
}
