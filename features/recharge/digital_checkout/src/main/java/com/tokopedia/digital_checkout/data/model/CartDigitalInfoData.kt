package com.tokopedia.digital_checkout.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDigitalInfoData(
        var type: String? = null,

        var id: String? = null,

        var attributes: AttributesDigitalData? = null,

        var title: String? = null,

        var isInstantCheckout: Boolean = false,

        var isNeedOtp: Boolean = false,

        var smsState: String? = null,

        var mainInfo: List<CartItemDigital>? = null,

        var additionalInfos: List<CartItemDigitalWithTitle>? = null,

        var relationProduct: RelationshipData? = null,

        var relationCategory: RelationshipData? = null,

        var relationOperator: RelationshipData? = null,

        var isForceRenderCart: Boolean = false,

        var crossSellingType: Int = 0,

        var crossSellingConfig: CrossSellingConfig? = null
) : Parcelable {

    @Parcelize
    data class CartItemDigital(
            val label: String,

            val value: String,
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

            var headerTitle: String? = null,

            var bodyTitle: String? = null,

            var bodyContentBefore: String? = null,

            var bodyContentAfter: String? = null,

            var checkoutButtonText: String? = null
    ) : Parcelable

    @Parcelize
    data class RelationshipData(
            var type: String? = null,

            var id: String? = null
    ) : Parcelable
}