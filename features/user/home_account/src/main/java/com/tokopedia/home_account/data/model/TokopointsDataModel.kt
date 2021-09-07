package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/02/21.
 */

data class TokopointsDataModel(
    @Expose
    @SerializedName("tokopointsStatusFiltered")
    var tokopointsStatusFilteredDataModel: TokopointsStatusFilteredDataModel = TokopointsStatusFilteredDataModel()
)

data class TokopointsStatusFilteredDataModel(
    @Expose
    @SerializedName("resultStatus")
    var resultStatus: ResultStatusDataModel = ResultStatusDataModel(),
    @Expose
    @SerializedName("statusFilteredData")
    var statusFilteredDataModel: StatusFilteredDataModel = StatusFilteredDataModel()
)

data class ResultStatusDataModel(
    @Expose
    @SerializedName("code")
    var code: String = "",
    @Expose
    @SerializedName("status")
    var status: String = "",
    @Expose
    @SerializedName("message")
    var message: MutableList<String> = mutableListOf(),
)

data class StatusFilteredDataModel(
    @Expose
    @SerializedName("points")
    var pointDataModel: PointDataModel = PointDataModel(),
)

data class PointDataModel(
    @Expose
    @SerializedName("iconImageURL")
    var iconImageURL: String = "",
    @Expose
    @SerializedName("pointsAmount")
    var pointsAmount: String = "",
    @Expose
    @SerializedName("pointsAmountStr")
    var pointsAmountStr: String = "",
    @Expose
    @SerializedName("externalCurrencyAmount")
    var externalCurrencyAmount: String = "",
    @Expose
    @SerializedName("externalCurrencyAmountStr")
    var externalCurrencyAmountStr: String = "",
    @Expose
    @SerializedName("pointsSection")
    var pointsSection: PointsSectionDataModel = PointsSectionDataModel()
)

data class PointsSectionDataModel(
    @Expose
    @SerializedName("redirectURL")
    var redirectURL: String = "",
    @Expose
    @SerializedName("redirectAppLink")
    var redirectAppLink: String = "",
    @Expose
    @SerializedName("sectionContent")
    var sectionContent: MutableList<SectionContentDataModel> = mutableListOf(),
)

data class SectionContentDataModel(
    @Expose
    @SerializedName("type")
    var type: String = "",
    @Expose
    @SerializedName("textAttributes")
    var textAttributes: TextAttributesDataModel = TextAttributesDataModel(),
    @Expose
    @SerializedName("tagAttributes")
    var tagAttributes: TagAttributesDataModel = TagAttributesDataModel(),
)

data class TextAttributesDataModel(
    @Expose
    @SerializedName("text")
    var text: String = "",
    @Expose
    @SerializedName("color")
    var color: String = "",
    @Expose
    @SerializedName("isBold")
    var isBold: Boolean = false,
)

data class TagAttributesDataModel(
    @Expose
    @SerializedName("text")
    var text: String = "",
    @Expose
    @SerializedName("backgroundColor")
    var backgroundColor: String = "",
)