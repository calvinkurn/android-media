package com.tokopedia.digital_checkout.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDigitalInfoData(
        var type: String = "",

        var id: String = "",

        var productId: String = "",

        var attributes: AttributesDigitalData = AttributesDigitalData(),

        var title: String = "",

        var isInstantCheckout: Boolean = false,

        var isNeedOtp: Boolean = false,

        var smsState: String = "",

        var mainInfo: List<CartItemDigital> = listOf(),

        var additionalInfos: List<CartItemDigitalWithTitle> = listOf(),

        var relationProduct: RelationshipData = RelationshipData(),

        var relationCategory: RelationshipData = RelationshipData(),

        var relationOperator: RelationshipData = RelationshipData(),

        var isForceRenderCart: Boolean = false,

        var crossSellingType: Int = 0,

        var showSubscriptionsView: Boolean = false,

        var crossSellingConfig: CrossSellingConfig = CrossSellingConfig()
) : Parcelable {

    @Parcelize
    data class CartItemDigital(
            val label: String = "",

            val value: String = "",
    ) : Parcelable

    @Parcelize
    data class CartItemDigitalWithTitle(
            val title: String = "",

            val items: List<CartItemDigital> = listOf()
    ) : Parcelable

    @Parcelize
    data class CrossSellingConfig(
            var isSkipAble: Boolean = false,

            var isChecked: Boolean = false,

            var headerTitle: String = "",

            var bodyTitle: String = "",

            var bodyContentBefore: String = "",

            var bodyContentAfter: String = "",

            var checkoutButtonText: String = ""
    ) : Parcelable

    @Parcelize
    data class RelationshipData(
            var type: String = "",

            var id: String = ""
    ) : Parcelable
}