package com.tokopedia.checkout.subfeature.multiple_address.domain.model.cartlist
import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import java.util.*

/**
 * @author anggaprasetiyo on 15/02/18.
 */

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

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readParcelable(TickerData::class.java.classLoader),
            parcel.createTypedArrayList(ShopGroupAvailableData),
            parcel.createTypedArrayList(ShopGroupWithErrorData),
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(CartTickerErrorData::class.java.classLoader),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(CartTickerErrorData::class.java.classLoader),
            parcel.readParcelable(LastApplyUiModel::class.java.classLoader)) {
    }

    override fun equals(obj: Any?): Boolean {
        if (obj is CartListData) {
            val `object` = obj as CartListData?
            return `object`?.isError == isError &&
                    `object`.isAllSelected == isAllSelected &&
                    `object`.shopGroupWithErrorDataList == shopGroupWithErrorDataList &&
                    `object`.shopGroupAvailableDataList == shopGroupAvailableDataList &&
                    `object`.isShowOnboarding == isShowOnboarding &&
                    `object`.isPromoCouponActive == isPromoCouponActive
        }
        return super.equals(obj)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isError) 1 else 0)
        parcel.writeString(errorMessage)
        parcel.writeParcelable(tickerData, flags)
        parcel.writeTypedList(shopGroupAvailableDataList)
        parcel.writeTypedList(shopGroupWithErrorDataList)
        parcel.writeByte(if (isPromoCouponActive) 1 else 0)
        parcel.writeParcelable(cartTickerErrorData, flags)
        parcel.writeString(defaultPromoDialogTab)
        parcel.writeByte(if (isAllSelected) 1 else 0)
        parcel.writeByte(if (isShowOnboarding) 1 else 0)
        parcel.writeString(promoBenefitInfo)
        parcel.writeString(promoUsageInfo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartListData> {
        override fun createFromParcel(parcel: Parcel): CartListData {
            return CartListData(parcel)
        }

        override fun newArray(size: Int): Array<CartListData?> {
            return arrayOfNulls(size)
        }
    }

}
