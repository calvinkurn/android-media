package com.tokopedia.campaignlist.page.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
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
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignStatusBottomSheet
import com.tokopedia.campaignlist.page.presentation.bottomsheet.CampaignTypeBottomSheet
import com.tokopedia.campaignlist.page.presentation.model.CampaignStatusSelection
import com.tokopedia.campaignlist.page.presentation.model.CampaignTypeSelection
import com.tokopedia.campaignlist.page.presentation.viewmodel.CampaignListViewModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.interfaces.ShareCallback
import com.tokopedia.linker.model.LinkerError
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.universal_sharing.constants.ImageGeneratorConstants
import com.tokopedia.universal_sharing.view.bottomsheet.SharingUtil
import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import com.tokopedia.universal_sharing.view.bottomsheet.listener.ShareBottomsheetListener
import com.tokopedia.universal_sharing.view.model.ShareModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class CampaignListComposeFragment : BaseDaggerFragment(), ShareBottomsheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: CampaignListTracker

    private var universalShareBottomSheet: UniversalShareBottomSheet? = null

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CampaignListViewModel::class.java)
    }

    companion object {
        @JvmStatic
        fun createInstance() = CampaignListComposeFragment()
        private const val SHARE = "share"
        private const val EMPTY_SEARCH_KEYWORD = ""
        private const val INDEX_ZERO = 0
        private const val INDEX_ONE = 1
        private const val INDEX_TWO = 2
        private const val INDEX_THREE = 3
        private const val INDEX_FOUR = 4
        private const val POWER_MERCHANT = "Power Merchant"
        private const val OFFICIAL_STORE = "Official Store"
        private const val POWER_MERCHANT_PRO = "Power Merchant PRO"
        private const val VALUE_SHARE_RS = "ShopRS" // Rilisan Spesial
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
        setupViewTreeOwners()

        viewModel.checkTickerState()
        viewModel.getSellerMetaData()
        viewModel.getCampaignList()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CampaignListScreen(
                    viewModel = viewModel,
                    onTapCampaignStatusFilter = { campaignStatuses -> showCampaignStatusBottomSheet(campaignStatuses) },
                    onTapCampaignTypeFilter = { campaignType -> showCampaignTypeBottomSheet(campaignType) },
                    onTapShareCampaignButton = { campaign ->
                        viewModel.setSelectedActiveCampaign(campaign)
                        viewModel.getSellerBanner(campaign.campaignId.toIntOrZero())
                    },
                    onClearFilter = { viewModel.getCampaignList() },
                    onSearchBarKeywordSubmit = { searchQuery ->
                        viewModel.setCampaignName(searchQuery)
                        val campaignTypeId = viewModel.getCampaignTypeId()
                        val campaignStatusId = viewModel.getCampaignStatusId()
                        viewModel.getCampaignList(
                            campaignName = searchQuery,
                            campaignTypeId = campaignTypeId,
                            statusId = campaignStatusId
                        )
                    },
                    onSearchbarCleared = { viewModel.getCampaignList() },
                    onDisplayShareBottomSheet = { banner ->
                        viewModel.setMerchantBannerData(banner)
                        showShareBottomSheet(banner)
                    },
                    onTickerDismissed = {
                        viewModel.onEvent(CampaignListViewModel.UiEvent.DismissTicker)
                    }
                )
            }
        }
    }


    private fun setupViewTreeOwners() {
        val decorView = requireActivity().window.decorView
        ViewTreeLifecycleOwner.set(decorView, this)
        ViewTreeViewModelStoreOwner.set(decorView, this)
        ViewTreeSavedStateRegistryOwner.set(decorView, this)
    }

    private fun showCampaignStatusBottomSheet(campaignStatusSelections: List<CampaignStatusSelection>) {
        val bottomSheet = CampaignStatusBottomSheet.createInstance(campaignStatusSelections, object : CampaignStatusBottomSheet.OnApplyButtonClickListener {
            override fun onApplyCampaignStatusFilter(selectedCampaignStatus: CampaignStatusSelection) {
                viewModel.onEvent(CampaignListViewModel.UiEvent.CampaignStatusFilterApplied(selectedCampaignStatus))
                viewModel.setCampaignStatusId(selectedCampaignStatus.statusId)
                viewModel.getCampaignList(statusId = selectedCampaignStatus.statusId)
                tracker.sendSelectCampaignStatusClickEvent(selectedCampaignStatus.statusId, userSession.shopId)
            }

            override fun onNoCampaignStatusSelected() {
                viewModel.onEvent(CampaignListViewModel.UiEvent.NoCampaignStatusFilterApplied)
                viewModel.getCampaignList(
                    EMPTY_SEARCH_KEYWORD,
                    viewModel.getCampaignTypeId(),
                    GetCampaignListUseCase.NPL_LIST_TYPE,
                    GetCampaignListUseCase.statusId
                )
            }

        })
        bottomSheet.show(childFragmentManager)
    }

    private fun showCampaignTypeBottomSheet(campaignTypeSelection: List<CampaignTypeSelection>) {
        val bottomSheet = CampaignTypeBottomSheet.createInstance(campaignTypeSelection, object : CampaignTypeBottomSheet.OnApplyButtonClickListener {
            override fun onApplyCampaignTypeFilter(selectedCampaignType: CampaignTypeSelection) {
                viewModel.onEvent(CampaignListViewModel.UiEvent.CampaignTypeFilterApplied(selectedCampaignType))

                val campaignTypeId = selectedCampaignType.campaignTypeId.toIntOrZero()

                viewModel.setCampaignTypeId(campaignTypeId)
                viewModel.getCampaignList(campaignTypeId = campaignTypeId)
                tracker.sendSelectCampaignTypeFilterClickEvent(selectedCampaignType.campaignTypeName, userSession.shopId)
            }
        })
        bottomSheet.show(childFragmentManager)
    }

    private fun showShareBottomSheet(merchantBannerData: GetMerchantCampaignBannerGeneratorData) {
        val campaignData = merchantBannerData.campaign
        val productData = campaignData.highlightProducts.Products
        val shopData = merchantBannerData.shopData

        val universalShareBottomSheet = UniversalShareBottomSheet.createInstance().apply {
            init(this@CampaignListComposeFragment)
            getImageFromMedia(getImageFromMediaFlag = true)
            setMediaPageSourceId(pageSourceId = ImageGeneratorConstants.ImageGeneratorSourceId.RILISAN_SPESIAL)

            setMetaData(
                tnTitle = viewModel.getShareBottomSheetTitle(shopData.name),
                tnImage = CampaignListViewModel.NPL_ICON_URL
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
        universalShareBottomSheet.show(childFragmentManager, universalShareBottomSheet.tag)
    }

    private fun validateIsOngoingCampaign(merchantBannerData: GetMerchantCampaignBannerGeneratorData): Boolean {
        return merchantBannerData.campaign.statusId == CampaignStatusIdTypeDef.BERLANGSUNG
    }

    private fun validateShopType(shopType: String): String {
        return when (shopType) {
            POWER_MERCHANT -> ShopTypeDef.POWER_MERCHANT
            POWER_MERCHANT_PRO -> ShopTypeDef.POWER_MERCHANT_PRO
            OFFICIAL_STORE -> ShopTypeDef.OFFICIAL_STORE
            else -> ShopTypeDef.REGULAR_MERCHANT
        }
    }

    override fun onShareOptionClicked(shareModel: ShareModel) {
        val banner = viewModel.getMerchantBannerData() ?: return

        val shopData = banner.shopData
        val campaignData = banner.campaign
        val campaignStatusId = viewModel.getSelectedActiveCampaign()?.campaignStatusId ?: return
        val linkerShareData = viewModel.generateLinkerShareData(shopData, campaignData, shareModel, campaignStatusId)

        LinkerManager.getInstance().executeShareRequest(
            LinkerUtils.createShareRequest(0, linkerShareData, object : ShareCallback {
                override fun urlCreated(linkerShareData: LinkerShareResult?) {
                    val shareWording = viewModel.getShareDescriptionWording(
                        shopData = shopData,
                        campaignData = campaignData,
                        merchantBannerData = banner,
                        shareUri = linkerShareData?.shareUri,
                        campaignStatusId = campaignStatusId
                    )
                    SharingUtil.executeShareIntent(
                        shareModel,
                        linkerShareData,
                        activity,
                        view,
                        shareWording
                    )
                    universalShareBottomSheet?.dismiss()
                }

                override fun onError(linkerError: LinkerError?) {}
            })
        )

    }

    override fun onCloseOptionClicked() {

    }

}