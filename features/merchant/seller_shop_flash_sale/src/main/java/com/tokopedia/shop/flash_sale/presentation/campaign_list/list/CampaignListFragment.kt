package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsFragmentCampaignListBinding
import com.tokopedia.shop.flash_sale.common.constant.Constant.EMPTY_STRING
import com.tokopedia.shop.flash_sale.common.constant.Constant.FIRST_PAGE
import com.tokopedia.shop.flash_sale.common.constant.Constant.ZERO
import com.tokopedia.shop.flash_sale.common.customcomponent.BaseSimpleListFragment
import com.tokopedia.shop.flash_sale.common.extension.showError
import com.tokopedia.shop.flash_sale.common.extension.slideDown
import com.tokopedia.shop.flash_sale.common.extension.slideUp
import com.tokopedia.shop.flash_sale.di.component.DaggerShopFlashSaleComponent
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CampaignListFragment: BaseSimpleListFragment<CampaignAdapter, CampaignUiModel>() {

    companion object {
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_NAME = "status_name"
        private const val BUNDLE_KEY_CAMPAIGN_STATUS_ID = "status_id"
        private const val BUNDLE_KEY_CAMPAIGN_COUNT = "product_count"
        private const val PAGE_SIZE = 10
        private const val SCROLL_DISTANCE_DELAY_IN_MILLIS: Long = 300
        private const val EMPTY_STATE_IMAGE_URL =
            "https://images.tokopedia.net/img/android/campaign/slash_price/empty_product_with_discount.png"

        @JvmStatic
        fun newInstance(
            campaignStatusName : String,
            campaignStatusIds: IntArray,
            campaignCount : Int,
        ): CampaignListFragment {
            val fragment = CampaignListFragment()
            fragment.arguments = Bundle().apply {
                putString(BUNDLE_KEY_CAMPAIGN_STATUS_NAME, campaignStatusName)
                putIntArray(BUNDLE_KEY_CAMPAIGN_STATUS_ID, campaignStatusIds)
                putInt(BUNDLE_KEY_CAMPAIGN_COUNT, campaignCount)
            }
            return fragment
        }

    }

    private val campaignStatusName by lazy {
        arguments?.getString(BUNDLE_KEY_CAMPAIGN_STATUS_NAME).orEmpty()
    }

    private val campaignStatusIds by lazy {
        arguments?.getIntArray(BUNDLE_KEY_CAMPAIGN_STATUS_ID)
    }

    private val totalCampaign by lazy {
        arguments?.getInt(BUNDLE_KEY_CAMPAIGN_COUNT).orZero()
    }

    private val campaignAdapter by lazy {
        CampaignAdapter(
            onCampaignClicked,
            onOverflowMenuClicked
        )
    }

    private var binding by autoClearedNullable<SsfsFragmentCampaignListBinding>()
    private var isFirstLoad = true
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(CampaignListViewModel::class.java) }
    private var onScrollDown: () -> Unit = {}
    private var onScrollUp: () -> Unit = {}

    override fun getScreenName(): String = CampaignListFragment::class.java.canonicalName.orEmpty()
    override fun initInjector() {
        DaggerShopFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SsfsFragmentCampaignListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeCampaigns()
        observeCampaignAttribute()
        observeCampaignCreation()
    }

    private fun setupView() {
        setupSearchBar()
        setupScrollListener()
    }

    private fun setupSearchBar() {
        binding?.run {
            searchBar.searchBarTextField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearAllData()
                    onShowLoading()
                    getCampaigns(FIRST_PAGE)
                    return@setOnEditorActionListener false
                }
                return@setOnEditorActionListener false
            }
            searchBar.clearListener = { clearSearchBar() }
        }
    }

    private fun setupScrollListener() {
        binding?.run {
            recyclerView.addOnScrollListener(
                RecyclerViewScrollListener(
                    onScrollDown = {
                        doOnDelayFinished {
                            onScrollDown()
                            handleScrollDownEvent()
                        }
                    },
                    onScrollUp = {
                        doOnDelayFinished {
                            onScrollUp()
                            handleScrollUpEvent()
                        }
                    }
                )
            )
        }
    }

    private fun clearSearchBar() {
        clearAllData()
        onShowLoading()
        viewModel.getCampaigns(
            PAGE_SIZE,
            FIRST_PAGE,
            campaignStatusIds?.toList().orEmpty(),
            EMPTY_STRING
        )
    }

    private fun observeCampaigns() {
        viewModel.campaigns.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> {
                    isFirstLoad = false
                    displayCampaigns(result.data)
                }
                is Fail -> {
                    binding?.root showError result.throwable
                    binding?.searchBar?.gone()
                }
            }
        }
    }

    private fun observeCampaignAttribute() {
        viewModel.campaignAttribute.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val attribute = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    private fun observeCampaignCreation() {
        viewModel.campaignCreation.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Success -> {
                    val creationResult = result.data
                }
                is Fail -> {

                }
            }
        }
    }

    fun setOnScrollDownListener(onScrollDown: () -> Unit = {}) {
        this.onScrollDown = onScrollDown
    }

    fun setOnScrollUpListener(onScrollUp: () -> Unit = {}) {
        this.onScrollUp = onScrollUp
    }

    private val onCampaignClicked: (CampaignUiModel, Int) -> Unit = { campaign, position ->

    }


    private val onOverflowMenuClicked: (CampaignUiModel) -> Unit = { campaign ->

    }

    override fun createAdapter(): CampaignAdapter {
        return campaignAdapter
    }

    override fun getRecyclerView(view: View): RecyclerView? {
        return binding?.recyclerView
    }

    override fun getSwipeRefreshLayout(view: View): SwipeRefreshLayout? {
        return null
    }

    override fun getPerPage(): Int {
        return PAGE_SIZE
    }

    override fun addElementToAdapter(list: List<CampaignUiModel>) {
        adapter?.addData(list)
    }

    override fun loadData(page: Int) {
        binding?.globalError?.gone()

        getCampaigns(page)
    }

    private fun getCampaigns(page: Int) {
        val offset = if (isFirstLoad || page == FIRST_PAGE) {
            FIRST_PAGE
        } else {
            page * PAGE_SIZE
        }

        val searchKeyword = binding?.searchBar?.searchBarTextField?.text.toString().trim()

        viewModel.getCampaigns(
            PAGE_SIZE,
            offset,
            campaignStatusIds?.toList().orEmpty(),
            searchKeyword
        )
    }

    override fun clearAdapterData() {
        adapter?.clearData()
    }

    override fun onShowLoading() {
        adapter?.showLoading()
    }

    override fun onHideLoading() {
        adapter?.hideLoading()
    }

    override fun onDataEmpty() {
    }

    override fun onGetListError(message: String) {
        displayError(message)
    }


    private fun handleScrollDownEvent() {
        binding?.searchBar.slideDown()
    }

    private fun handleScrollUpEvent() {
        binding?.searchBar.slideUp()
    }

    private fun displayCampaigns(data: CampaignMeta) {
        if (data.totalCampaign == ZERO) {
            handleEmptyState(data.totalCampaign)
        } else {
            renderList(data.campaigns, data.campaigns.size == getPerPage())
        }
    }

    private fun handleEmptyState(totalCampaign : Int) {
        if (totalCampaign == ZERO) {
            showEmptyState()
        } else {
            hideEmptyState()
        }
    }

    private fun displayError(errorMessage: String) {
        binding?.run {
            globalError.visible()
            globalError.setType(GlobalError.SERVER_ERROR)
            globalError.setActionClickListener { getCampaigns(FIRST_PAGE) }
            root showError errorMessage
        }

    }

    private fun showEmptyState() {
        val title = getString(R.string.sfs_no_campaign_title)
        val description = getString(R.string.sfs_no_campaign_description)
        
        binding?.searchBar?.gone()
        binding?.recyclerView?.gone()

        binding?.emptyState?.visible()
        binding?.emptyState?.setImageUrl(EMPTY_STATE_IMAGE_URL)
        binding?.emptyState?.setTitle(title)
        binding?.emptyState?.setDescription(description)
    }

    private fun hideEmptyState() {
        binding?.emptyState?.gone()
    }

    private fun doOnDelayFinished(block: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(SCROLL_DISTANCE_DELAY_IN_MILLIS)
            block()
        }
    }

}