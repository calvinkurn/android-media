package com.tokopedia.checkout.subfeature.multiple_address.domain.model.cartlist
import android.os.Parcelable
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
        var shopGroupWithErrorDataList: List<ShopGroupWithErrorData> = ArrayList(),
        var isPromoCouponActive: Boolean = false,
        var cartTickerErrorData: CartTickerErrorData? = null,
        var defaultPromoDialogTab: String? = null,
        var isAllSelected: Boolean = false,
        var isShowOnboarding: Boolean = false,
        var promoBenefitInfo: String? = null,
        var promoUsageInfo: String? = null,
        var errorDefault: PromoCheckoutErrorDefault? = null,
        var lastApplyShopGroupSimplifiedData: LastApplyUiModel? = null
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (other is CartListData) {
            val `object` = other as CartListData?
            return `object`?.isError == isError &&
                    `object`.isAllSelected == isAllSelected &&
                    `object`.shopGroupWithErrorDataList == shopGroupWithErrorDataList &&
                    `object`.shopGroupAvailableDataList == shopGroupAvailableDataList &&
                    `object`.isShowOnboarding == isShowOnboarding &&
                    `object`.isPromoCouponActive == isPromoCouponActive
        }
        return super.equals(other)
    }

}
