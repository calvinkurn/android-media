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
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentFlashSaleListBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign
import com.tokopedia.tkpd.flashsale.util.constant.BundleConstant.BUNDLE_KEY_TARGET_TAB_POSITION
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class FlashSaleListFragment : BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_TAB_NAME = "tab_name"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "campaign_count"
        private const val PAGE_SIZE = 10

        @JvmStatic
        fun newInstance(
            tabPosition: Int,
            tabName: String,
            totalCampaign: Int
        ): FlashSaleListFragment {
            val fragment = FlashSaleListFragment()
            fragment.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_TARGET_TAB_POSITION, tabPosition)
                putString(BUNDLE_KEY_TAB_NAME, tabName)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, totalCampaign)
            }
            return fragment
        }

    }

    private val tabPosition by lazy {
        arguments?.getInt(BUNDLE_KEY_TARGET_TAB_POSITION).orZero()
    }

    private val tabName by lazy {
        arguments?.getString(BUNDLE_KEY_TAB_NAME).orEmpty()
    }

    private val totalCampaign by lazy {
        arguments?.getInt(BUNDLE_KEY_CAMPAIGN_COUNT).orZero()
    }
    

    private val flashSaleAdapter = FlashSaleAdapter()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    

    private var binding by autoClearedNullable<StfsFragmentFlashSaleListBinding>()
    private var isFirstLoad = true
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
        observeUiEvent()
        observeUiState()
        viewModel.getFlashSaleList(tabName, Int.ZERO)
    }


    private fun setupView() {
        setupClickListener()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding?.recyclerView?.run {
            layoutManager = LinearLayoutManager(activity ?: return, LinearLayoutManager.VERTICAL, false)
            adapter = flashSaleAdapter

            endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    flashSaleAdapter.showLoading()
                    viewModel.getFlashSaleList(tabName, page * PAGE_SIZE)
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

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEvent.collect { event -> handleEvent(event) }
        }
    }

    private fun handleEvent(event: FlashSaleListViewModel.UiEvent) {
        when (event) {
            is FlashSaleListViewModel.UiEvent.FetchError -> {
                binding?.root.showToasterError(event.throwable)
            }
        }
    }


    private fun handleUiState(uiState: FlashSaleListViewModel.UiState) {
        renderLoadingState(uiState.isLoading)
        renderFlashSaleList(uiState.campaigns)
    }


    private fun renderLoadingState(isLoading: Boolean) {
        binding?.loader?.isVisible = isLoading
    }

    private fun renderFlashSaleList(campaigns: List<Campaign>) {
        if (campaigns.isEmpty()) return
        flashSaleAdapter.submit(campaigns)
    }
}