package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 01/07/22.
 */

data class TokoPlusBadgeResponse(
    @SerializedName("restrictValidateRestriction")
    val restrictValidateRestriction: RestrictValidateRestrictionModel
)

data class RestrictValidateRestrictionModel(
    @SerializedName("metaResponse")
    val metaResponse: List<RestrictMetaModel> = listOf()
)

data class RestrictMetaModel(
    @SerializedName("restrictionName")
    val restrictionName: String = "",
    @SerializedName("dataResponse")
    val metaData: List<RestrictMetaDataModel> = listOf()
)

data class RestrictMetaDataModel(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("metadata")
    val metaStatus: MetaStatusModel = MetaStatusModel()
)

data class MetaStatusModel(
    @SerializedName("bebasOngkirMetadata")
    val freeShippingTokoPlus: FreeShippingTokoPlusModel = FreeShippingTokoPlusModel()
)

data class FreeShippingTokoPlusModel(
    @SerializedName("badgeURL")
    val badgeUrl: String = ""
)