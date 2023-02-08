package com.tokopedia.usercomponents.userconsent.common

import com.google.gson.annotations.SerializedName

data class UserConsentCollectionDataModel(
    @SerializedName("success")
    var success: Boolean = false,
    @SerializedName("refID")
    var refID: String = "",
    @SerializedName("errorMessages")
    var errorMessages: List<String> = listOf(),
    @SerializedName("collectionPoints")
    var collectionPoints: MutableList<CollectionPointDataModel> = mutableListOf()
) {
    data class CollectionPointDataModel(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("version")
        var version: String = "",
        @SerializedName("consentType")
        var consentType: String = "",
        @SerializedName("purposes")
        var purposes: MutableList<PurposeDataModel> = mutableListOf(),
        @SerializedName("notices")
        var notices: MutableList<NoticeDataModel> = mutableListOf(),
        @SerializedName("attributes")
        var attributes: AttributeDataModel = AttributeDataModel(),
        @SerializedName("needConsent")
        var needConsent: Boolean? = null
    ) {
        data class PurposeDataModel(
            @SerializedName("id")
            var id: String = "",
            @SerializedName("label")
            var label: String = "",
            @SerializedName("description")
            var description: String = "",
            @SerializedName("version")
            var version: String = "",
            @SerializedName("purposeType")
            var purposeType: String = "",
            @SerializedName("attributes")
            var attribute: AttributeDataModel = AttributeDataModel(),
        ) {
            data class AttributeDataModel(
                @SerializedName("uiName")
                var uiName: String = ""
            )
        }

        data class NoticeDataModel(
            @SerializedName("id")
            var id: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("url")
            var url: String = "",
            @SerializedName("privacyNoticeGuid")
            var privacyNoticeGuid: String = "",
            @SerializedName("type")
            var type: String = ""
        )

        data class AttributeDataModel(
            @SerializedName("collectionPointPurposeRequirement", alternate = ["requirementType"])
            var collectionPointPurposeRequirement: String = "",
            @SerializedName("collectionPointStatementOnlyFlag", alternate = ["usingCheckbox"])
            var collectionPointStatementOnlyFlag: String = "",
            @SerializedName("policyNoticeType")
            var policyNoticeType: String = "",
            @SerializedName("PolicyNoticeTnCPageID", alternate = ["tncPageID"])
            var policyNoticeTnCPageID: String = ""
        )
    }
}
