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
                var uiName: String = "",
                @SerializedName("uiDescription")
                var uiDescription: String = "",
                @SerializedName("alwaysMandatory")
                var alwaysMandatory: String = "",
                @SerializedName("personalDataType")
                var personalDataType: List<String> = listOf()
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
            @SerializedName("collectionPointPurposeRequirement")
            var collectionPointPurposeRequirement: String = "",
            @SerializedName("collectionPointStatementOnlyFlag")
            var collectionPointStatementOnlyFlag: String = "",
            @SerializedName("policyNoticeType")
            var policyNoticeType: String = "",
            @SerializedName("PolicyNoticeTnCPageID")
            var policyNoticeTnCPageID: String = "",
            @SerializedName("PolicyNoticePolicyPageID")
            var policyNoticePolicyPageID: String = "",
        )
    }
}