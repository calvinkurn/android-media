package com.tokopedia.tkpd.flashsale.presentation.list.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.components.adapter.CompositeAdapter
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListBinding
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.LoadingDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.OngoingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.UpcomingFlashSaleDelegateAdapter
import com.tokopedia.tkpd.flashsale.presentation.list.child.adapter.item.LoadingItem
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant.BUNDLE_KEY_TARGET_TAB_POSITION
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import com.tokopedia.seller_tokopedia_flash_sale.R

class FlashSaleListFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_TAB_NAME = "tab_name"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "campaign_count"
        private const val PAGE_SIZE = 10

        private const val IMAGE_URL_EMPTY_UPCOMING_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_upcoming_campaign.png"
        private const val IMAGE_URL_EMPTY_REGISTERED_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_registered_campaign.png"
        private const val IMAGE_URL_EMPTY_ONGOING_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_ongoing_campaign.png"
        private const val IMAGE_URL_EMPTY_FINISHED_CAMPAIGN =
            "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_empty_finished_campaign.png"

        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            tabId: Int,
            tabName: String,
            totalCampaign: Int
        ): FlashSaleListFragment {
            val fragment = FlashSaleListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TARGET_TAB_POSITION, tabPosition)
                putInt(BundleConstant.BUNDLE_KEY_TAB_ID, tabId)
                putString(BUNDLE_KEY_TAB_NAME, tabName)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, totalCampaign)
            }
            return fragment
        }

    }

    private val tabPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_TARGET_TAB_POSITION).orZero()
    }

    private val tabId by lazy {
        arguments?.getInt(BundleConstant.BUNDLE_KEY_TAB_ID).orZero()
    }

    private val tabName by lazy {
        arguments?.getString(BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    private val totalCampaign by lazy {
        arguments?.getInt(BUNDLE_KEY_CAMPAIGN_COUNT).orZero()
    }


    private val flashSaleAdapter by lazy {
        CompositeAdapter.Builder()
            .add(OngoingFlashSaleDelegateAdapter())
            .add(UpcomingFlashSaleDelegateAdapter())
            .add(LoadingDelegateAdapter())
            .build()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory


    private var binding by autoClearedNullable<StfsFragmentFlashSaleListBinding>()
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(FlashSaleListViewModel::class.java) }
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun getScreenName(): String = FlashSaleListFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentFlashSaleListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiEffect()
        observeUiState()
        viewModel.processEvent(FlashSaleListViewModel.UiEvent.LoadPage(tabName, tabId, Int.ZERO))
    }


    private fun setupView() {
        setupSortFilter()
        setupClickListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.run {
            layoutManager =
                LinearLayoutManager(activity ?: return, LinearLayoutManager.VERTICAL, false)
            adapter = flashSaleAdapter

            endlessRecyclerViewScrollListener =
                object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        val currentItemCount = flashSaleAdapter.getItems().size
                        if (currentItemCount < totalCampaign) {
                            flashSaleAdapter.addItem(LoadingItem)
                            viewModel.processEvent(
                                FlashSaleListViewModel.UiEvent.LoadPage(
                                    tabName,
                                    tabId,
                                    page * PAGE_SIZE
                                )
                            )
                        }
                    }
                }
            addOnScrollListener(endlessRecyclerViewScrollListener ?: return)
        }
    }

    private fun setupClickListener() {
        binding?.run {


        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleEffect(effect: FlashSaleListViewModel.UiEffect) {
        when (effect) {
            is FlashSaleListViewModel.UiEffect.FetchFlashSaleError -> {
                binding?.root.showToasterError(effect.throwable)
            }
        }
    }


    private fun handleUiState(uiState: FlashSaleListViewModel.UiState) {
        renderLoadingState(uiState.isLoading)
        renderFlashSaleList(uiState.flashSales)
    }


    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading

    }

    private fun renderFlashSaleList(flashSales: List<DelegateAdapterItem>) {
        flashSaleAdapter.removeItem(LoadingItem)

        if (flashSales.isNotEmpty()) {
            flashSaleAdapter.addItems(flashSales)
        }
    }

    private fun setupSortFilter() {

        val sortFilter = SortFilterItem(getString(R.string.stfs_sort))
        val onSortClicked = {
            sortFilter.type = if(sortFilter.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            sortFilter.selectedItem = arrayListOf("Jabodetabek", "Bali")
        }

        sortFilter.listener = { onSortClicked() }
        sortFilter.chevronListener = { onSortClicked() }

        val categoryFilter = SortFilterItem(getString(R.string.stfs_all_category))
        val onCategoryClicked = {
            sortFilter.type = if(sortFilter.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            sortFilter.selectedItem = arrayListOf("Jabodetabek", "Bali")
        }
        categoryFilter.listener = { onCategoryClicked() }
        categoryFilter.chevronListener = { onCategoryClicked() }


        val items = arrayListOf(sortFilter, categoryFilter)

        binding?.sortFilter?.addItem(items)
        binding?.sortFilter?.parentListener = {}
        binding?.sortFilter?.dismissListener = {}
    }

}