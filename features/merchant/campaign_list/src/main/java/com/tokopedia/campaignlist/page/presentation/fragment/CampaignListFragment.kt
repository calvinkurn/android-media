package com.tokopedia.campaignlist.page.presentation.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaignlist.R
import com.tokopedia.campaignlist.common.analytics.CampaignListTracker
import com.tokopedia.campaignlist.common.constant.CampaignStatusIdTypeDef
import com.tokopedia.campaignlist.common.constant.ShopTypeDef
import com.tokopedia.campaignlist.common.data.model.response.GetMerchantCampaignBannerGeneratorData
import com.tokopedia.campaignlist.common.di.DaggerCampaignListComponent
import com.tokopedia.campaignlist.common.usecase.GetCampaignListUseCase
import com.tokopedia.campaignlist.common.util.onTextChanged
import com.tokopedia.campaignlist.databinding.FragmentCampaignListBinding
import com.tokopedia.campaignlist.page.presentation.adapter.ActiveCampaignListAdapter
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignStatusBottomSheet
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignTypeBottomSheet
import com.tokopedia.campaignlist.page.presentation.model.ActiveCampaign
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewholder.ActiveCampaignViewHolder
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel.Companion.NPL_ICON_URL
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.net.UnknownHostException
import javax.inject.Inject

class CampaignListFragment : BaseDaggerFragment(),
        CampaignStatusBottomSheet.OnApplyButtonClickListener,
        CampaignTypeBottomSheet.OnApplyButtonClickListener,
        ActiveCampaignViewHolder.OnShareButtonClickListener, ShareBottomsheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: CampaignListTracker

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CampaignListViewModel::class.java)
    }

    private var campaignTypeBottomSheet: CampaignTypeBottomSheet? = null
    private var campaignStatusBottomSheet: CampaignStatusBottomSheet? = null
    private var universalShareBottomSheet: UniversalShareBottomSheet? = null
    private var adapter: ActiveCampaignListAdapter? = null
    private var binding: FragmentCampaignListBinding? = null
    private var campaignTypeFilter: SortFilterItem? = null
    private var campaignStatusFilter: SortFilterItem? = null
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        @JvmStatic
        fun createInstance() = CampaignListFragment()
        private const val SHARE = "share"
        private const val EMPTY_SEARCH_KEYWORD = ""
        const val TICKER_STATE_PREFERENCE = "TICKER_STATE_PREFERENCE"
        const val IS_DISMISS_TICKER = "IS_DISMISS_TICKER"
        const val INDEX_ZERO = 0
        const val INDEX_ONE = 1
        const val INDEX_TWO = 2
        const val INDEX_THREE = 3
        const val INDEX_FOUR = 4
        const val POWER_MERCHANT = "Power Merchant"
        const val OFFICIAL_STORE = "Official Store"
        const val POWER_MERCHANT_PRO = "Power Merchant PRO"
        const val VALUE_SHARE_RS = "ShopRS" // Rilisan Spesial
        private const val DEFAULT_SELECTED_CAMPAIGN_TYPE_ID = 0
    }

    override fun getScreenName(): String {
        return getString(R.string.active_campaign_list)
    }

    override fun initInjector() {
        DaggerCampaignListComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val viewBinding = FragmentCampaignListBinding.inflate(inflater, container, false)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(TICKER_STATE_PREFERENCE, Context.MODE_PRIVATE)
        setupView(binding)
        observeLiveData()
        viewModel.getSellerMetaData()
        viewModel.getCampaignList()
    }

    override fun onShareButtonClicked(activeCampaign: ActiveCampaign) {
        viewModel.setSelectedActiveCampaign(activeCampaign)
        try { viewModel.getSellerBanner(activeCampaign.campaignId.toInt()) }
        catch (e: Exception) {
            // TODO log invalid campaign id
        }
        tracker.sendShareButtonClickEvent(
                viewModel.getCampaignTypeId(),
                activeCampaign.campaignId,
                userSession.shopId,
                userSession.userId,
        )
    }

    override fun onApplyCampaignTypeFilter(selectedCampaignType: CampaignTypeSelection) {
        campaignTypeFilter?.title = selectedCampaignType.campaignTypeName
        campaignTypeFilter?.type = ChipsUnify.TYPE_SELECTED
        var campaignTypeId = DEFAULT_SELECTED_CAMPAIGN_TYPE_ID
        try {
            campaignTypeId = selectedCampaignType.campaignTypeId.toInt()
        } catch (e: java.lang.Exception) {
            // TODO handle exception
        }
        viewModel.setCampaignTypeId(campaignTypeId)
        viewModel.getCampaignList(campaignTypeId = campaignTypeId)
        tracker.sendSelectCampaignTypeFilterClickEvent(selectedCampaignType.campaignTypeName, userSession.shopId)
    }

    override fun onApplyCampaignStatusFilter(selectedCampaignStatus: CampaignStatusSelection) {
        campaignStatusFilter?.type = ChipsUnify.TYPE_SELECTED
        campaignStatusFilter?.title = selectedCampaignStatus.statusText
        viewModel.setCampaignStatusId(selectedCampaignStatus.statusId)
        viewModel.getCampaignList(statusId = selectedCampaignStatus.statusId)
        tracker.sendSelectCampaignStatusClickEvent(selectedCampaignStatus.statusId, userSession.shopId)
    }

    override fun onNoCampaignStatusSelected() {
        campaignStatusFilter?.type = ChipsUnify.TYPE_NORMAL
        viewModel.getCampaignList(
            EMPTY_SEARCH_KEYWORD,
            viewModel.getCampaignTypeId(),
            GetCampaignListUseCase.NPL_LIST_TYPE,
            GetCampaignListUseCase.statusId
        )
    }

    private fun setupView(binding: FragmentCampaignListBinding?) {
        setupSearchBar(binding)
        setupActiveCampaignListView(binding)
        setupTicker(binding)
    }

    private fun setupTicker(binding: FragmentCampaignListBinding?) {
        val isHideTicker = isDismissTicker()?: false
        if (!isHideTicker) {
            binding?.tickerCampaignTypeWording?.visibility = View.VISIBLE
            binding?.tickerCampaignTypeWording?.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    TODO("Not yet implemented")
                }

                override fun onDismiss() {
                    saveDismissTickerState()
                }
            })
        } else {
            binding?.tickerCampaignTypeWording?.visibility = View.GONE
        }
    }

    private fun isDismissTicker(): Boolean? {
        return sharedPreferences?.getBoolean(IS_DISMISS_TICKER, false)
    }

    private fun saveDismissTickerState() {
        sharedPreferences?.edit()?.run {
            putBoolean(IS_DISMISS_TICKER, true)
        }?.apply()
    }

    private fun setupSearchBar(binding: FragmentCampaignListBinding?) {
        binding?.sbuCampaignList?.clearListener = { getCampaignListWithCurrentlySelectedFilter() }
        binding?.sbuCampaignList?.searchBarTextField?.onTextChanged { searchKeyword ->
            if (searchKeyword.isEmpty()) {
                getCampaignListWithCurrentlySelectedFilter()
            }
        }
        binding?.sbuCampaignList?.searchBarTextField?.setOnEditorActionListener { textView, actionId, event ->
            if (actionId == IME_ACTION_SEARCH || event.keyCode == KEYCODE_ENTER) {
                val query = textView.text.toString()
                viewModel.setCampaignName(query)
                viewModel.getCampaignList(campaignName = query)
                return@setOnEditorActionListener true
            } else return@setOnEditorActionListener false
        }
    }

    private fun setupCampaignListFilter(binding: FragmentCampaignListBinding?) {
        binding?.sfCampaignList?.apply {
            // setup campaign status filter
            val campaignStatusFilterTitle = getString(R.string.campaign_list_label_status)
            campaignStatusFilter = SortFilterItem(campaignStatusFilterTitle)
            campaignStatusFilter?.listener = {
                campaignStatusBottomSheet?.show(childFragmentManager)
                tracker.sendOpenCampaignStatusFilterClickEvent(userSession.shopId)
            }

            // setup campaign type filter
            val campaignTypeFilterTitle = viewModel.getSelectedCampaignTypeSelection()?.campaignTypeName ?: ""
            campaignTypeFilter = SortFilterItem(campaignTypeFilterTitle)
            campaignTypeFilter?.type = ChipsUnify.TYPE_SELECTED
            campaignTypeFilter?.listener = {
                campaignTypeBottomSheet?.show(childFragmentManager)
                tracker.sendOpenCampaignTypeFilterClickEvent(userSession.shopId)
            }
            val sortFilterItemList = ArrayList<SortFilterItem>()
            campaignStatusFilter?.run { sortFilterItemList.add(this) }
            campaignTypeFilter?.run { sortFilterItemList.add(this) }
            addItem(sortFilterItemList)
            campaignStatusFilter?.refChipUnify?.setChevronClickListener {
                campaignStatusFilter?.listener?.invoke()
            }
            campaignTypeFilter?.refChipUnify?.setChevronClickListener {
                campaignTypeFilter?.listener?.invoke()
            }
        }
        binding?.sfCampaignList?.parentListener = {
            /* No op. We need to specify this block, otherwise the clear filter chip will do nothing
               when clicked */
        }
        binding?.sfCampaignList?.dismissListener = { viewModel.getCampaignList() }
    }

    private fun setupActiveCampaignListView(binding: FragmentCampaignListBinding?) {
        adapter = ActiveCampaignListAdapter(this)
        adapter?.setCampaignImpression { campaign ->
            tracker.sendCampaignImpressionEvent(campaign.campaignId, userSession.shopId)
        }
        binding?.rvCampaignList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setupCampaignTypeBottomSheet(campaignTypeSelections: List<CampaignTypeSelection>) {
        campaignTypeBottomSheet = CampaignTypeBottomSheet.createInstance(campaignTypeSelections, this)
    }

    private fun setupCampaignStatusBottomSheet(campaignStatusSelections: List<CampaignStatusSelection>) {
        campaignStatusBottomSheet = CampaignStatusBottomSheet.createInstance(campaignStatusSelections, this)
    }

    private fun setupUniversalShareBottomSheet(merchantBannerData: GetMerchantCampaignBannerGeneratorData) {
        val campaignData = merchantBannerData.campaign
        val productData = campaignData.highlightProducts.Products
        val shopData = merchantBannerData.shopData

        universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@CampaignListFragment)
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.RILISAN_SPESIAL)

            setMetaData(
                    tnTitle = viewModel.getShareBottomSheetTitle(shopData.name),
                    tnImage = NPL_ICON_URL
            )

            val campaignType = viewModel.getSelectedActiveCampaign()?.campaignTypeId ?: return
            val campaignId = viewModel.getSelectedActiveCampaign()?.campaignId ?: return

            setUtmCampaignData(
                pageName = VALUE_SHARE_RS,
                userId = userSession.userId,
                pageIdConstituents = listOf(userSession.shopId, campaignType, campaignId),
                feature = SHARE
            )

            val _totalProducts = viewModel.getProductCount(merchantBannerData.campaign.totalProduct)
            val _isOngoing = if (validateIsOngoingCampaign(merchantBannerData)) 1 else 0

            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.CAMPAIGN_NAME , value = campaignData.name)
            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.CAMPAIGN_INFO , value = campaignData.discountPercentageText)
            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_LOGO , value = shopData.logo)
            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.SHOP_NAME , value = shopData.name)
            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.BADGE , value = validateShopType(shopData.badge.Title))
            addImageGeneratorData(
                    key = ImageGeneratorConstants.ImageGeneratorKeys.DATE ,
                    value = if (validateIsOngoingCampaign(merchantBannerData))
                        merchantBannerData.formattedSharingEndDate else merchantBannerData.formattedSharingStartDate
            )
            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.ONGOING , value = _isOngoing.toString())
            addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCTS_COUNT , value = _totalProducts.toString())
            addImageGeneratorData(
                key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCTS_OVERLOAD,
                value = viewModel.calculateOverloadProductCount(_totalProducts).toString()
            )
            if (_totalProducts >= INDEX_ONE) {
                val _imgUrl = productData.get(INDEX_ZERO).imageUrl
                val _originalPrice = productData.get(INDEX_ZERO).productCampaign.originalPriceFmt
                val _discountedPrice = productData.get(INDEX_ZERO).productCampaign.discountedPriceFmt
                val _discount = productData.get(INDEX_ZERO).productCampaign.discountPercentage.toString()

                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1 , value = _imgUrl)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_BEFORE , value = _originalPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_PRICE_AFTER , value = _discountedPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_1_DISCOUNT , value = _discount)
            }

            if (_totalProducts >= INDEX_TWO) {
                val _imgUrl = productData.get(INDEX_ONE).imageUrl
                val _originalPrice = productData.get(INDEX_ONE).productCampaign.originalPriceFmt
                val _discountedPrice = productData.get(INDEX_ONE).productCampaign.discountedPriceFmt
                val _discount = productData.get(INDEX_ONE).productCampaign.discountPercentage.toString()

                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2 , value = _imgUrl)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_BEFORE , value = _originalPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_PRICE_AFTER , value = _discountedPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_2_DISCOUNT , value = _discount)
            }

            if (_totalProducts >= INDEX_THREE) {
                val _imgUrl = productData.get(INDEX_TWO).imageUrl
                val _originalPrice = productData.get(INDEX_TWO).productCampaign.originalPriceFmt
                val _discountedPrice = productData.get(INDEX_TWO).productCampaign.discountedPriceFmt
                val _discount = productData.get(INDEX_TWO).productCampaign.discountPercentage.toString()

                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3 , value = _imgUrl)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_BEFORE , value = _originalPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_PRICE_AFTER , value = _discountedPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_3_DISCOUNT , value = _discount)
            }

            if (_totalProducts >= INDEX_FOUR) {
                val _imgUrl = productData.get(INDEX_THREE).imageUrl
                val _originalPrice = productData.get(INDEX_THREE).productCampaign.originalPriceFmt
                val _discountedPrice = productData.get(INDEX_THREE).productCampaign.discountedPriceFmt
                val _discount = productData.get(INDEX_THREE).productCampaign.discountPercentage.toString()

                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4 , value = _imgUrl)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_BEFORE , value = _originalPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_PRICE_AFTER , value = _discountedPrice)
                addImageGeneratorData(key = ImageGeneratorConstants.ImageGeneratorKeys.PRODUCT_4_DISCOUNT , value = _discount)
            }
        }
    }

    private fun validateIsOngoingCampaign(merchantBannerData: GetMerchantCampaignBannerGeneratorData): Boolean {
        return merchantBannerData.campaign.statusId == CampaignStatusIdTypeDef.BERLANGSUNG
    }

    private fun validateShopType(shopType: String): String {
        return when (shopType) {
            POWER_MERCHANT -> ShopTypeDef.POWER_MERCHANT
            POWER_MERCHANT_PRO -> ShopTypeDef.POWER_MERCHANT_PRO
            OFFICIAL_STORE -> ShopTypeDef.OFFICIAL_STORE
            else -> ShopTypeDef.REGULAR_MERCHANT // REGULAR MERCHANT
        }
    }

    private fun observeLiveData() {
        viewModel.getCampaignListResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    val activeCampaignList = viewModel.mapCampaignListDataToActiveCampaignList(result.data.getCampaignListV2.campaignList)
                    adapter?.setActiveCampaignList(activeCampaignList)
                }
                is Fail -> {
                    if (result.throwable is UnknownHostException) {
                        displayGetCampaignListError(R.string.no_internet_error_message)
                        return@observe
                    }
                    displayGetCampaignListError(R.string.campaign_error_fetch_campaign_list)
                }
            }
        })

        viewModel.getMerchantBannerResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    if (UniversalShareBottomSheet.isCustomSharingEnabled(context)) {
                        // show share bottom sheet
                        val merchantBannerData = result.data.getMerchantCampaignBannerGeneratorData
                        viewModel.setMerchantBannerData(merchantBannerData)
                        setupUniversalShareBottomSheet(merchantBannerData)
                        universalShareBottomSheet?.show(childFragmentManager, this)
                        val selectedActiveCampaign = viewModel.getSelectedActiveCampaign()
                        selectedActiveCampaign?.run {
                            tracker.sendShareBottomSheetDisplayedEvent(
                                    viewModel.getCampaignTypeId(),
                                    this.campaignId,
                                    userSession.userId,
                                    userSession.shopId
                            )
                        }
                    }
                }
                is Fail -> {
                    displayGetDataError()
                }
            }
        })

        viewModel.getSellerMetaDataResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Success -> {
                    // setup campaign type bottom sheet
                    val campaignTypeData = result.data.getSellerCampaignSellerAppMeta.campaignTypeData
                    val campaignTypeSelections = viewModel.mapCampaignTypeDataToCampaignTypeSelections(campaignTypeData)
                    setupCampaignTypeBottomSheet(campaignTypeSelections)
                    // setup campaign status bottom sheet
                    val campaignStatus = result.data.getSellerCampaignSellerAppMeta.campaignStatus
                    val campaignStatusSelections = viewModel.mapCampaignStatusToCampaignStatusSelections(campaignStatus)
                    setupCampaignStatusBottomSheet(campaignStatusSelections)
                    // set default selection for campaign type
                    viewModel.setDefaultCampaignTypeSelection(campaignTypeSelections)
                    setupCampaignListFilter(binding)
                }
                is Fail -> {
                    displayGetDataError()
                }
            }
        })
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val merchantBannerData = viewModel.getMerchantBannerData()
        merchantBannerData?.run {
            val shopData = merchantBannerData.shopData
            val campaignData = merchantBannerData.campaign
            val campaignStatusId = viewModel.getSelectedActiveCampaign()?.campaignStatusId ?: return
            val linkerShareData = viewModel.generateLinkerShareData(shopData, campaignData, shareModel, campaignStatusId)
            LinkerManager.getInstance().executeShareRequest(
                    LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                        override fun urlCreated(linkerShareData: LinkerShareResult?) {
                            val shareWording = viewModel.getShareDescriptionWording(
                                    shopData = shopData,
                                    campaignData = campaignData,
                                    merchantBannerData = merchantBannerData,
                                    shareUri = linkerShareData?.shareUri,
                                    campaignStatusId = campaignStatusId
                            )
                            SharingUtil.executeShareIntent(shareModel, linkerShareData, activity, view, shareWording)
                            universalShareBottomSheet?.dismiss()
                        }

                        override fun onError(linkerError: LinkerError?) {}
                    })
            )
        }
        val selectedActiveCampaign = viewModel.getSelectedActiveCampaign()
        selectedActiveCampaign?.run {
            tracker.sendSelectShareChannelClickEvent(
                    shareModel.channel.orEmpty(),
                    viewModel.getCampaignTypeId(),
                    this.campaignId,
                    userSession.userId,
                    userSession.shopId
            )

        }
    }

    override fun onCloseOptionClicked() {
        val selectedActiveCampaign = viewModel.getSelectedActiveCampaign()
        selectedActiveCampaign?.run {
            tracker.sendShareBottomSheetDismissClickEvent(
                    viewModel.getCampaignTypeId(),
                    this.campaignId,
                    userSession.userId,
                    userSession.shopId
            )
        }
    }

    private fun displayGetCampaignListError(@StringRes stringResourceId: Int) {
        Toaster.build(
                view = binding?.root ?: return,
                text = getString(stringResourceId),
                actionText = getString(R.string.retry),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR,
                clickListener = {
                    //Retry to get campaign list data with currently selected filter and search bar query
                    viewModel.getCampaignList(
                            campaignName = viewModel.getCampaignName(),
                            campaignTypeId = viewModel.getCampaignTypeId(),
                            statusId = viewModel.getCampaignStatusId()
                    )
                }
        ).show()
    }

    private fun displayGetDataError() {
        Toaster.build(
                view = binding?.root ?: return,
                text = getString(R.string.campaign_error_fetch_metadata),
                actionText = getString(R.string.ok),
                duration = Toaster.LENGTH_LONG,
                type = Toaster.TYPE_ERROR
        ).show()
    }

    private fun getCampaignListWithCurrentlySelectedFilter() {
        viewModel.getCampaignList(
            EMPTY_SEARCH_KEYWORD,
            viewModel.getCampaignTypeId(),
            GetCampaignListUseCase.NPL_LIST_TYPE,
            viewModel.getCampaignStatusId()
        )
    }
}