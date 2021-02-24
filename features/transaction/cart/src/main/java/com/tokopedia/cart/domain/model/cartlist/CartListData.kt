package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.button.ABTestButton
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
        var errorMessage: String? = null,
        var tickerData: TickerData? = null,
        var shopGroupAvailableDataList: List<ShopGroupAvailableData> = ArrayList(),
        var unavailableGroupData: List<UnavailableGroupData> = emptyList(),
        var isPromoCouponActive: Boolean = false,
        var cartTickerErrorData: CartTickerErrorData? = null,
        var defaultPromoDialogTab: String? = null,
        var isAllSelected: Boolean = false,
        var isShowOnboarding: Boolean = false,
        var promoBenefitInfo: String? = null,
        var promoUsageInfo: String? = null,
        var errorDefault: PromoCheckoutErrorDefault? = null,
        var lastApplyShopGroupSimplifiedData: LastApplyUiModel? = null,
        var shoppingSummaryData: ShoppingSummaryData = ShoppingSummaryData(),
        var promoSummaryData: PromoSummaryData = PromoSummaryData(),
        var outOfServiceData: OutOfServiceData = OutOfServiceData(),
        var showLessUnavailableDataWording: String = "",
        var showMoreUnavailableDataWording: String = "",
        var abTestButton: ABTestButton = ABTestButton(),
        var localizationChooseAddressData: LocalizationChooseAddressData = LocalizationChooseAddressData()
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is CartListData) {
            val `object` = other as CartListData?
            return `object`?.isError == isError &&
                    `object`.isAllSelected == isAllSelected &&
                    `object`.unavailableGroupData == unavailableGroupData &&
                    `object`.shopGroupAvailableDataList == shopGroupAvailableDataList &&
                    `object`.isShowOnboarding == isShowOnboarding &&
                    `object`.isPromoCouponActive == isPromoCouponActive
        }
        return super.equals(other)
    }

}
