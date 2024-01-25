package com.tokopedia.mvcwidget.data.entity


import com.google.gson.annotations.SerializedName

data class PromoCatalogResponse(
    @SerializedName("promoCatalogGetPDEBottomSheet")
    val promoCatalogGetPDEBottomSheet: PromoCatalogGetPDEBottomSheet = PromoCatalogGetPDEBottomSheet()
) {
    data class PromoCatalogGetPDEBottomSheet(
        @SerializedName("resultList")
        val resultList: List<Result> = listOf(),
        @SerializedName("resultStatus")
        val resultStatus: ResultStatus = ResultStatus()
    ) {
        data class Result(
            @SerializedName("productID")
            val productID: String = "",
            @SerializedName("widgetList")
            val widgetList: List<Widget> = listOf()
        ) {
            data class Widget(
                @SerializedName("componentList")
                val componentList: List<Component> = listOf(),
                @SerializedName("id")
                val id: String = "",
                @SerializedName("widgetType")
                val widgetType: String = ""
            ) {
                data class Component(
                    @SerializedName("attributeList")
                    val attributeList: List<Attribute> = listOf(),
                    @SerializedName("componentType")
                    val componentType: String = "",
                    @SerializedName("id")
                    val id: String = ""
                ) {
                    data class Attribute(
                        @SerializedName("type")
                        val type: String = "",
                        @SerializedName("value")
                        val value: String = ""
                    )
                }
            }
        }

        data class ResultStatus(
            @SerializedName("code")
            val code: String = "",
            @SerializedName("message")
            val message: List<String> = listOf(),
            @SerializedName("status")
            val status: String = ""
        )
    }
}
