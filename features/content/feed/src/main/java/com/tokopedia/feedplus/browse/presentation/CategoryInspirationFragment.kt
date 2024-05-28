package com.tokopedia.feedplus.browse.presentation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.calculateWindowSizeClass
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseImpressionManager
import com.tokopedia.feedplus.browse.data.tracker.FeedBrowseTrackerImpl
import com.tokopedia.feedplus.browse.presentation.adapter.CategoryInspirationAdapter
import com.tokopedia.feedplus.browse.presentation.adapter.itemdecoration.CategoryInspirationItemDecoration
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.ChipsViewHolder
import com.tokopedia.feedplus.browse.presentation.adapter.viewholder.InspirationCardViewHolder
import com.tokopedia.feedplus.browse.presentation.model.CategoryInspirationAction
import com.tokopedia.feedplus.browse.presentation.model.FeedBrowseItemListModel
import com.tokopedia.feedplus.databinding.FragmentFeedCategoryInspirationBinding
import com.tokopedia.feedplus.domain.mapper.toAuthorWidgetModel
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by kenny.hadisaputra on 21/09/23
 */
internal class CategoryInspirationFragment @Inject constructor(
    categoryInspirationViewModelFactory: CategoryInspirationViewModel.Factory,
    private val impressionManager: FeedBrowseImpressionManager,
    private val router: Router,
    trackerFactory: FeedBrowseTrackerImpl.Factory
) : TkpdBaseV4Fragment() {

    private var _binding: FragmentFeedCategoryInspirationBinding? = null
    private val binding get() = _binding!!

    private val tracker by lazyThreadSafetyNone {
        trackerFactory.create(FeedBrowseTrackerImpl.PREFIX_CATEGORY_INSPIRATION_PAGE)
    }

    private val chipsListener = object : ChipsViewHolder.Listener {
        override fun onChipImpressed(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        ) {
            tracker.viewChipsWidget(
                chip,
                widgetModel.slotInfo,
                chipPosition
            )
        }

        override fun onChipClicked(
            viewHolder: ChipsViewHolder,
            widgetModel: FeedBrowseItemListModel.Chips.Item,
            chip: WidgetMenuModel,
            chipPosition: Int
        ) {
            tracker.clickChipsWidget(
                chip,
                widgetModel.slotInfo,
                chipPosition
            )
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
        override fun onImpressed(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            tracker.viewChannelCard(
                model.item,
                model.config,
                model.slotInfo,
                model.index
            )
        }

        override fun onClicked(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            tracker.clickChannelCard(
                model.item,
                model.config,
                model.slotInfo,
                model.index
            )
            router.route(context, model.item.appLink)
        }

        override fun onAuthorClicked(
            viewHolder: InspirationCardViewHolder.Item,
            model: FeedBrowseItemListModel.InspirationCard.Item
        ) {
            tracker.clickAuthorName(
                item = model.item.partner.toAuthorWidgetModel(model.item.channelId),
                slotInfo = model.slotInfo,
                widgetPositionInList = model.index,
            )
            router.route(context, model.item.partner.appLink)
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

    private val adapter by viewLifecycleBound(
        {
            CategoryInspirationAdapter(
                it.viewLifecycleOwner.lifecycleScope,
                requireActivity().calculateWindowSizeClass(),
                chipsListener,
                inspirationCardListener
            )
        }
    )

    private val viewModel by viewModels<CategoryInspirationViewModel> {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val source = arguments?.getString(ARGS_SOURCE)
                    ?: error("Sources not found, please call CategoryInspirationFragment#createParams and set that as the arguments")

                return categoryInspirationViewModelFactory.create(
                    source = source
                ) as T
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            tracker.clickBackExit()
            requireActivity().finish()
        }
    }

    override fun getScreenName(): String {
        return "Feed Category Inspiration"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            onBackPressedCallback
        )
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

        tracker.openScreenBrowseFeedPage()

        setupView()
        observe()

        if (activity?.lastNonConfigurationInstance == null) {
            viewModel.onAction(CategoryInspirationAction.Init)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCategoryInspiration.removeOnScrollListener(loadMoreListener)
        _binding = null
    }

    private fun setupView() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.header)
        binding.header.setBackgroundColor(Color.TRANSPARENT)
        binding.header.setNavigationOnClickListener {
            onBackPressedCallback.handleOnBackPressed()
        }

        val layoutManager = GridLayoutManager(context, adapter.spanCount).apply {
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

    private fun observe() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { state ->
                    binding.header.headerTitle = state.title

                    impressionManager.onNewWidgets(state.items, state.selectedMenuId)
                    adapter.setList(state.state, state.items, state.selectedMenuId) {
                        if (_binding == null) return@setList
                        binding.rvCategoryInspiration.invalidateItemDecorations()
                    }
                }
        }
    }

    companion object {
        private const val ARGS_SOURCE = "args_source"

        private const val LOAD_PAGE_THRESHOLD = 2

        fun createParams(
            source: String
        ): Bundle {
            return Bundle().apply {
                putString(ARGS_SOURCE, source)
            }
        }
    }
}
