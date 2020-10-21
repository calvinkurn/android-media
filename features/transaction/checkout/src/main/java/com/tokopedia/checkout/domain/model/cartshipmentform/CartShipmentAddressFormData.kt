package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData
import com.tokopedia.checkout.domain.model.cartshipmentform.DisabledFeaturesDetailData
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.purchase_platform.common.feature.button.ABTestButton
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData

/**
 * @author anggaprasetiyo on 21/02/18.
 */

data class CartShipmentAddressFormData(
        var isHasError: Boolean = false,
        var isError: Boolean = false,
        var errorMessage: String? = null,
        var errorCode: Int = 0,
        var isShowOnboarding: Boolean = false,
        var isDropshipperDisable: Boolean = false,
        var isOrderPrioritasDisable: Boolean = false,
        var groupAddress: List<GroupAddress> = ArrayList(),
        var keroToken: String? = null,
        var keroDiscomToken: String? = null,
        var keroUnixTime: Int = 0,
        var donation: Donation? = null,
        var cod: CodModel? = null,
        var isUseCourierRecommendation: Boolean = false,
        var isHidingCourier: Boolean = false,
        var isBlackbox: Boolean = false,
        var egoldAttributes: EgoldAttributeModel? = null,
        var isIneligiblePromoDialogEnabled: Boolean = false,
        var tickerData: TickerData? = null,
        var addressesData: AddressesData? = null,
        var disabledFeaturesDetailData: DisabledFeaturesDetailData? = null,
        var campaignTimerUi: CampaignTimerUi? = null,
        var lastApplyData: LastApplyUiModel? = null,
        var promoCheckoutErrorDefault: PromoCheckoutErrorDefault? = null,
        var isOpenPrerequisiteSite: Boolean = false,
        var isEligibleNewShippingExperience: Boolean = false,
        var abTestButton: ABTestButton = ABTestButton()
) : Parcelable {

    val isAvailablePurchaseProtection: Boolean
        get() {
            for (address in groupAddress) {
                for (groupShop in address.groupShop) {
                    for (product in groupShop.products) {
                        if (product.purchaseProtectionPlanData != null &&
                                product.purchaseProtectionPlanData.isProtectionAvailable) {
                            return true
                        }
                    }
                }
            }
            return false
        }

    constructor(parcel: Parcel) : this() {
        isHasError = parcel.readByte() != 0.toByte()
        isError = parcel.readByte() != 0.toByte()
        errorMessage = parcel.readString()
        errorCode = parcel.readInt()
        isShowOnboarding = parcel.readByte() != 0.toByte()
        isDropshipperDisable = parcel.readByte() != 0.toByte()
        isOrderPrioritasDisable = parcel.readByte() != 0.toByte()
        groupAddress = parcel.createTypedArrayList(GroupAddress.CREATOR) ?: ArrayList()
        keroToken = parcel.readString()
        keroDiscomToken = parcel.readString()
        keroUnixTime = parcel.readInt()
        donation = parcel.readParcelable(Donation::class.java.classLoader)
        isUseCourierRecommendation = parcel.readByte() != 0.toByte()
        isHidingCourier = parcel.readByte() != 0.toByte()
        isBlackbox = parcel.readByte() != 0.toByte()
        egoldAttributes = parcel.readParcelable(EgoldAttributeModel::class.java.classLoader)
        isIneligiblePromoDialogEnabled = parcel.readByte() != 0.toByte()
        tickerData = parcel.readParcelable(TickerData::class.java.classLoader)
        addressesData = parcel.readParcelable(AddressesData::class.java.classLoader)
        disabledFeaturesDetailData = parcel.readParcelable(DisabledFeaturesDetailData::class.java.classLoader)
        campaignTimerUi = parcel.readParcelable(CampaignTimerUi::class.java.classLoader)
        lastApplyData = parcel.readParcelable(LastApplyUiModel::class.java.classLoader)
        promoCheckoutErrorDefault = parcel.readParcelable(PromoCheckoutErrorDefault::class.java.classLoader)
        isOpenPrerequisiteSite = parcel.readByte() != 0.toByte()
        isEligibleNewShippingExperience = parcel.readByte() != 0.toByte()
        abTestButton = parcel.readParcelable(ABTestButton::class.java.classLoader) ?: ABTestButton()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isHasError) 1 else 0)
        parcel.writeByte(if (isError) 1 else 0)
        parcel.writeString(errorMessage)
        parcel.writeInt(errorCode)
        parcel.writeByte(if (isShowOnboarding) 1 else 0)
        parcel.writeByte(if (isDropshipperDisable) 1 else 0)
        parcel.writeByte(if (isOrderPrioritasDisable) 1 else 0)
        parcel.writeTypedList(groupAddress)
        parcel.writeString(keroToken)
        parcel.writeString(keroDiscomToken)
        parcel.writeInt(keroUnixTime)
        parcel.writeParcelable(donation, flags)
        parcel.writeByte(if (isUseCourierRecommendation) 1 else 0)
        parcel.writeByte(if (isHidingCourier) 1 else 0)
        parcel.writeByte(if (isBlackbox) 1 else 0)
        parcel.writeParcelable(egoldAttributes, flags)
        parcel.writeByte(if (isIneligiblePromoDialogEnabled) 1 else 0)
        parcel.writeParcelable(tickerData, flags)
        parcel.writeParcelable(addressesData, flags)
        parcel.writeParcelable(disabledFeaturesDetailData, flags)
        parcel.writeParcelable(campaignTimerUi, flags)
        parcel.writeParcelable(lastApplyData, flags)
        parcel.writeParcelable(promoCheckoutErrorDefault, flags)
        parcel.writeByte(if (isOpenPrerequisiteSite) 1 else 0)
        parcel.writeByte(if (isEligibleNewShippingExperience) 1 else 0)
        parcel.writeParcelable(abTestButton, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartShipmentAddressFormData> {
        override fun createFromParcel(parcel: Parcel): CartShipmentAddressFormData {
            return CartShipmentAddressFormData(parcel)
        }

        override fun newArray(size: Int): Array<CartShipmentAddressFormData?> {
            return arrayOfNulls(size)
        }
    }
}