package com.tokopedia.digital_checkout.data.model

import android.os.Parcelable
import com.tokopedia.digital_checkout.data.response.getcart.RechargeGetCart
import kotlinx.parcelize.Parcelize

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

        var mainInfo: List<CartItemDigital> = emptyList(),

        var additionalInfos: List<CartItemDigitalWithTitle> = emptyList(),

        var relationProduct: RelationshipData = RelationshipData(),

        var relationCategory: RelationshipData = RelationshipData(),

        var relationOperator: RelationshipData = RelationshipData(),

        var isForceRenderCart: Boolean = false,

        var isSubscribed: Boolean = false,

        var isSpecialProduct: Boolean = false,

        var channelId: String = "",

        var collectionPointId: String = "",

        var collectionPointVersion: String = "",

        var collectionDataElements: List<RechargeGetCart.CollectionDataElements> = emptyList()
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
    data class RelationshipData(
            var type: String = "",

            var id: String = ""
    ) : Parcelable
}
