package com.tokopedia.home.account.revamp.domain.data.mapper

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.home.account.AccountConstants
import com.tokopedia.home.account.AccountConstants.Analytics.LOAN
import com.tokopedia.home.account.AccountConstants.Analytics.PEMBELI
import com.tokopedia.home.account.AccountConstants.Analytics.PENJUAL
import com.tokopedia.home.account.R
import com.tokopedia.home.account.constant.SettingConstant
import com.tokopedia.home.account.data.model.ShopInfoLocation
import com.tokopedia.home.account.presentation.util.AccountHomeErrorHandler
import com.tokopedia.home.account.presentation.viewmodel.*
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import com.tokopedia.home.account.presentation.viewmodel.base.SellerViewModel
import com.tokopedia.home.account.revamp.domain.data.model.AccountDataModel
import com.tokopedia.home.account.revamp.domain.data.model.DepositDataModel
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.navigation_common.model.FieldDataModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.unifycomponents.Label
import com.tokopedia.user.session.UserSession
import com.tokopedia.user_identification_common.KYCConstant
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

class SellerAccountMapper @Inject constructor(
        @ApplicationContext private val context: Context,
        private val userSession: UserSession
) : Func1<GraphqlResponse, SellerViewModel> {

    private val remoteConfig: RemoteConfig

    init {
        remoteConfig = FirebaseRemoteConfigImpl(context)
    }

    override fun call(graphqlResponse: GraphqlResponse): SellerViewModel {
        val sellerViewModel: SellerViewModel
        var accountDataModel: AccountDataModel? = graphqlResponse.getData(AccountDataModel::class.java)

        if (accountDataModel == null) {
            accountDataModel = AccountDataModel()
            AccountHomeErrorHandler.logDataNull("SellerAccountMapper", Throwable("AccountDataModel"))
        }

        if (accountDataModel.shopInfo.info.shopId != "-1" && isShopHaveProvinceId(graphqlResponse)) {
            sellerViewModel = getSellerModel(context, accountDataModel, getDataDeposit(graphqlResponse))
            sellerViewModel.seller = true
        } else {
            sellerViewModel = emptySellerModel()
            sellerViewModel.seller = false
        }
        return sellerViewModel
    }

    private fun isShopHaveProvinceId(graphqlResponse: GraphqlResponse): Boolean {
        val error: List<GraphqlError>? = graphqlResponse.getError(ShopInfoLocation::class.java)
        if (error.isNullOrEmpty()) {
            var data : ShopInfoLocation? = graphqlResponse.getData(ShopInfoLocation::class.java)
            if (data == null) {
                data = ShopInfoLocation()
                AccountHomeErrorHandler.logDataNull("SellerAccountMapper", Throwable("ShopInfoLocation"))
            }

            if(data.shopInfoByID.result.isNotEmpty()) {
                return data.shopInfoByID.result[0].shippingLoc.provinceID != 0
            }
        }
        return false
    }

    private fun getDataDeposit(graphqlResponse: GraphqlResponse): DataDeposit {
        val error = graphqlResponse.getError(DataDeposit.Response::class.java)
        if (error.isNullOrEmpty()) {
            var data : DataDeposit.Response? = graphqlResponse.getData(DataDeposit.Response::class.java)
            if (data == null) {
                data = DataDeposit.Response()
                AccountHomeErrorHandler.logDataNull("SellerAccountMapper", Throwable("DataDeposit.Response"))
            }
            return data.dataResponse.dataDeposit
        }
        return DataDeposit()
    }

    private fun getSellerModel(context: Context, accountDataModel: AccountDataModel, dataDeposit: DataDeposit): SellerViewModel {
        val sellerViewModel = SellerViewModel()
        val items: MutableList<ParcelableViewModel<*>> = ArrayList()
        val showPinjamanModalOnTop = remoteConfig.getBoolean(RemoteConfigKey.PINJAMAN_MODAL_AKUN_PAGE_POSITION_TOP, false)
        var mitraTopperMaxLoan = getPreApproveData(accountDataModel).preApp.partnerMaxLoan
        val mitraTopperUrl = getPreApproveData(accountDataModel).url

        val tickerViewModel = parseTickerSeller(context, accountDataModel)
        tickerViewModel?.let {
            if (!it.listMessage.isNullOrEmpty()) {
                items.add(it)
            }
        }

        items.add(getShopInfoMenu(accountDataModel, dataDeposit))

        if (accountDataModel.saldo.depositLong != 0L) {
            items.add(getSaldoInfo(accountDataModel.saldo))
        }

        if (mitraTopperMaxLoan.isNotEmpty() && mitraTopperMaxLoan.toLongOrZero() > 0) {
            mitraTopperMaxLoan = CurrencyFormatUtil.convertPriceValueToIdrFormat(mitraTopperMaxLoan.toLong(), true)
        }

        if (showPinjamanModalOnTop) {
            if (mitraTopperMaxLoan.isNotEmpty() && mitraTopperMaxLoan != "0" && mitraTopperUrl.isNotEmpty()) {
                items.add(getInfoCardMenu(mitraTopperMaxLoan, mitraTopperUrl))
            }
        }

        items.add(getSalesMenu(accountDataModel))
        items.add(getSellerResolutionMenu(accountDataModel))
        items.add(MenuTitleViewModel(context.getString(R.string.title_menu_product)))
        items.add(AddProductViewModel())
        items.add(getMyProductMenu())
        items.add(getProductFeatureMenu())
        items.add(getProductDraftMenu())
        items.add(MenuTitleViewModel(context.getString(R.string.title_menu_other_features)))

        if (accountDataModel.shopInfo.info.shopIsOfficial != "1") {
            items.add(getPowerMerchantSettingMenu())
        }

        // update userSession
        val _shopName = accountDataModel.shopInfo.info.shopName
        val _shopAvatar = accountDataModel.shopInfo.info.shopAvatar
        userSession.shopName = _shopName
        userSession.shopAvatar = _shopAvatar

        items.add(getTopAdsMenu())
        items.add(getShopVoucherMenu())

        val menuList = getRekeningPremiumAccountMenu(accountDataModel)
        if (menuList != null) {
            items.add(menuList)
        }

        items.add(getSellerCenterMenu())
        items.add(MenuTitleViewModel(context.getString(R.string.tokopedia_care)))
        items.add(getResolutionCenter())

        if (!showPinjamanModalOnTop) {
            if (mitraTopperMaxLoan.isNotEmpty() && mitraTopperMaxLoan != "0" && mitraTopperUrl.isNotEmpty()) {
                items.add(getInfoCardMenu(mitraTopperMaxLoan, mitraTopperUrl))
            }
        }

        sellerViewModel.items = items
        return sellerViewModel
    }

    private fun getPreApproveData(accountDataModel: AccountDataModel): FieldDataModel {
        accountDataModel?.lePreapprove?.fieldData?.let { data ->
            return data
        }
        return FieldDataModel()
    }

    private fun parseTickerSeller(context: Context, accountDataModel: AccountDataModel): TickerViewModel? {
        val sellerTickerModel = TickerViewModel(ArrayList())
        if (accountDataModel?.kycStatusPojo?.kycStatusDetailPojo?.isSuccess == KYCConstant.IS_SUCCESS_GET_STATUS
                && accountDataModel?.kycStatusPojo?.kycStatusDetailPojo?.status == KYCConstant.STATUS_NOT_VERIFIED) {
            sellerTickerModel.listMessage.add(context.getString(R.string.ticker_unverified))
        } else if (!(accountDataModel?.shopInfo?.owner?.goldMerchant)) {
            val tickerMessage: String? = remoteConfig.getString(RemoteConfigKey.SELLER_ACCOUNT_TICKER_MSG, "")
            if (!tickerMessage.isNullOrEmpty()) {
                sellerTickerModel.listMessage.add(tickerMessage)
            }
        }
        return sellerTickerModel
    }

    private fun setKycToModel(shopCard: ShopCardViewModel, accountDataModel: AccountDataModel) {
        if (accountDataModel?.kycStatusPojo?.kycStatusDetailPojo?.isSuccess == KYCConstant.IS_SUCCESS_GET_STATUS) {
            shopCard.verificationStatus = accountDataModel?.kycStatusPojo?.kycStatusDetailPojo?.status
            shopCard.verificationStatusName = accountDataModel?.kycStatusPojo?.kycStatusDetailPojo?.statusName
        } else {
            shopCard.verificationStatus = KYCConstant.STATUS_ERROR
            shopCard.verificationStatusName = ""
        }
    }

    private fun emptySellerModel(): SellerViewModel {
        val sellerViewModel = SellerViewModel()
        val items: MutableList<ParcelableViewModel<*>> = ArrayList()
        items.add(SellerEmptyViewModel())
        sellerViewModel.items = items
        return sellerViewModel
    }

    private fun getSalesMenu(accountDataModel: AccountDataModel): MenuGridViewModel {
        return MenuGridViewModel().apply {
            title = context.getString(R.string.title_menu_sales)
            linkText = context.getString(R.string.label_menu_show_history)
            applinkUrl = ApplinkConst.SELLER_HISTORY
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_sales)
            items = getSellerOrderMenu(accountDataModel.notifications.sellerOrder != null, accountDataModel
            )
        }
    }

    private fun getInfoCardMenu(mitraTopperMaxLoan : String, mitraTopperUrl: String): InfoCardViewModel {
        return InfoCardViewModel().apply {
            iconRes = R.drawable.ic_personal_loan
            titleTrack = PENJUAL
            sectionTrack = LOAN
            itemTrack = LOAN
            mainText = context.getString(R.string.title_menu_loan)
            secondaryText = String.format("%s %s", context.getString(R.string.label_menu_loan), mitraTopperMaxLoan)
            applink = String.format("%s?url=%s", ApplinkConst.WEBVIEW, mitraTopperUrl)
        }
    }

    private fun getShopInfoMenu(accountDataModel: AccountDataModel, _dataDeposit: DataDeposit): ShopCardViewModel {
        return ShopCardViewModel().apply {
            shopImageUrl = accountDataModel.shopInfo.info.shopId
            shopId = accountDataModel.shopInfo.info.shopId
            shopName = accountDataModel.shopInfo.info.shopName
            shopImageUrl = accountDataModel.shopInfo.info.shopAvatar
            goldMerchant = accountDataModel.shopInfo.owner.goldMerchant
            shopIsOfficial = accountDataModel.shopInfo.info.shopIsOfficial
            dataDeposit = _dataDeposit
            if (accountDataModel.reputationShops.isNotEmpty()) {
                reputationImageUrl = accountDataModel.reputationShops[0].getBadgeHd()
            }
            setKycToModel(this, accountDataModel)
        }
    }

    private fun getMyProductMenu(): MenuListViewModel {
        return MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_product_list)
            menuDescription = context.getString(R.string.label_menu_product_list)
            applink = ApplinkConst.PRODUCT_MANAGE
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_product)
        }
    }

    private fun getProductFeatureMenu(): MenuListViewModel {
        return LabelledMenuListUiModel().apply {
                menu = context.getString(R.string.title_menu_product_feature)
                label = context.getString(com.tokopedia.seller_migration_common.R.string.seller_migration_label_seller_app_only)
                labelType = Label.GENERAL_LIGHT_GREEN
                menuDescription = context.getString(R.string.label_menu_product_feature)
                applink = ApplinkConst.PRODUCT_MANAGE
                titleTrack = PENJUAL
                sectionTrack = context.getString(R.string.title_menu_product)
                isShowRightButton = true
        }
    }

    private fun getProductDraftMenu(): MenuListViewModel {
        return MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_product_draft)
            menuDescription = context.getString(R.string.label_menu_product_draft)
            applink = ApplinkConst.PRODUCT_DRAFT
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_product)
        }
    }

    private fun getPowerMerchantSettingMenu(): PowerMerchantCardViewModel {
        return PowerMerchantCardViewModel().apply {
            titleText = context.getString(R.string.title_pm_card)
            descText = context.getString(R.string.desc_pm_card)
            iconRes = R.drawable.ic_pm_line_shades
        }
    }

    private fun getTopAdsMenu(): MenuListViewModel {
        return MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_topads)
            menuDescription = context.getString(R.string.label_menu_topads)
            applink = AccountConstants.Navigation.TOPADS
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_other_features)
        }
    }

    private fun getShopVoucherMenu(): LabelledMenuListUiModel {
        return LabelledMenuListUiModel().apply {
            menu = context.getString(R.string.title_menu_voucher_toko)
            label = context.getString(com.tokopedia.seller_migration_common.R.string.seller_migration_label_seller_app_only)
            labelType = Label.GENERAL_LIGHT_GREEN
            menuDescription = context.getString(R.string.description_menu_voucher_toko)
            applink = ""
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_other_features)
            isShowRightButton = true
        }
    }

    private fun getSellerCenterMenu(): MenuListViewModel {
        return MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_seller_center)
            menuDescription = context.getString(R.string.label_menu_seller_center)
            applink = ApplinkConst.SELLER_CENTER
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_other_features)
        }
    }

    private fun getResolutionCenter(): MenuListViewModel {
        return MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_resolution_center)
            menuDescription = context.getString(R.string.label_menu_resolution_center)
            applink = ApplinkConst.CONTACT_US_NATIVE
            titleTrack = PEMBELI
            sectionTrack = context.getString(R.string.title_menu_help)
        }
    }

    private fun getSaldoInfo(depositDataModel: DepositDataModel): SellerSaldoViewModel {
        val sellerSaldoCard = SellerSaldoViewModel()
        if (depositDataModel.depositLong != 0L) {
            sellerSaldoCard.setBalance(CurrencyFormatUtil.convertPriceValueToIdrFormat(depositDataModel.depositLong, false))
        }
        return sellerSaldoCard
    }

    private fun getSellerOrderMenu(isNotNull: Boolean, accountDataModel: AccountDataModel?): List<MenuGridItemViewModel> {
        val menuGridItems: MutableList<MenuGridItemViewModel> = ArrayList()
        var gridItem = MenuGridItemViewModel(
                R.drawable.ic_new_order,
                context.getString(R.string.label_menu_new_order),
                ApplinkConst.SELLER_NEW_ORDER,
                if (isNotNull) accountDataModel!!.notifications.sellerOrder.newOrder else 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        )
        menuGridItems.add(gridItem)
        gridItem = MenuGridItemViewModel(
                R.drawable.ic_ready_to_ship,
                context.getString(R.string.label_menu_ready_to_ship),
                ApplinkConst.SELLER_PURCHASE_READY_TO_SHIP,
                if (isNotNull) accountDataModel!!.notifications.sellerOrder.readyToShip else 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        )
        menuGridItems.add(gridItem)
        gridItem = MenuGridItemViewModel(
                R.drawable.ic_shipped,
                context.getString(R.string.label_menu_shipped),
                ApplinkConst.SELLER_PURCHASE_SHIPPED,
                if (isNotNull) accountDataModel!!.notifications.sellerOrder.shipped else 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        )
        menuGridItems.add(gridItem)
        gridItem = MenuGridItemViewModel(
                R.drawable.ic_delivered,
                context.getString(R.string.label_menu_arrive_at_destination),
                ApplinkConst.SELLER_PURCHASE_DELIVERED,
                if (isNotNull) accountDataModel!!.notifications.sellerOrder.arriveAtDestination else 0,
                PENJUAL,
                context.getString(R.string.title_menu_sales)
        )
        menuGridItems.add(gridItem)
        return menuGridItems
    }

    private fun getSellerResolutionMenu(accountDataModel: AccountDataModel): ParcelableViewModel<*> {
        return MenuListViewModel().apply {
            menu = context.getString(R.string.title_menu_seller_complain)
            menuDescription = context.getString(R.string.label_menu_seller_complain)
            if (accountDataModel.notifications.resolution != null) {
                count = accountDataModel.notifications.resolution.seller
            }
            applink = SettingConstant.RESCENTER_SELLER
            titleTrack = PENJUAL
            sectionTrack = context.getString(R.string.title_menu_sales)
        }
    }

    private fun getRekeningPremiumAccountMenu(accountDataModel: AccountDataModel): ParcelableViewModel<*>? {
        accountDataModel.premiumAccountResponse.let {
            it.data.copyWriting.let { copyWriting ->
                if (copyWriting.title.isNotEmpty()) {
                    return RekeningPremiumViewModel().apply {
                        menu = copyWriting.title
                        menuDescription = copyWriting.subtitle
                        webLink = copyWriting.url
                        titleTrack = PENJUAL
                        sectionTrack = context.getString(R.string.title_menu_other_features)
                    }
                }
            }
        }
        return null
    }
}