package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.outofservice.OutOfServiceData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * @author anggaprasetiyo on 15/02/18.
 */
@Parcelize
data class CartListData(
        var isError: Boolean = false,
        var tickerData: TickerData = TickerData(),
        var shopGroupAvailableDataList: List<ShopGroupAvailableData> = ArrayList(),
        var unavailableGroupData: List<UnavailableGroupData> = emptyList(),
        var isPromoCouponActive: Boolean = false,
        var cartTickerErrorData: CartTickerErrorData = CartTickerErrorData(),
        var isAllSelected: Boolean = false,
        var isShowOnboarding: Boolean = false,
        var errorDefault: PromoCheckoutErrorDefault = PromoCheckoutErrorDefault(),
        var lastApplyShopGroupSimplifiedData: LastApplyUiModel = LastApplyUiModel(),
        var shoppingSummaryData: ShoppingSummaryData = ShoppingSummaryData(),
        var promoSummaryData: PromoSummaryData = PromoSummaryData(),
        var outOfServiceData: OutOfServiceData = OutOfServiceData(),
        var showLessUnavailableDataWording: String = "",
        var showMoreUnavailableDataWording: String = "",
        var localizationChooseAddressData: LocalizationChooseAddressData = LocalizationChooseAddressData(),
        var popUpMessage: String = "",
        var popupErrorMessage: String = ""
) : Parcelable
