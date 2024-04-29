package com.tokopedia.home.beranda.domain.interactor.repository.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class TargetedTickerParam(
    @SerializedName("input")
    val input: TargetedTickerParamInput = TargetedTickerParamInput()
) : GqlParam {

    companion object {
        fun onlyNeedPageSource(source: String) =
            TargetedTickerParam(
                TargetedTickerParamInput(
                    page = source
                )
            )
    }
}

data class TargetedTickerParamInput(
    @SerializedName("Page")
    val page: String = "",

    @SerializedName("Target")
    val target: Target = Target(),

    @SerializedName("Template")
    val template: Template = Template(),
) {

    data class Target(
        @SerializedName("Type")
        val type: String = "",

        @SerializedName("Values")
        val values: List<String> = emptyList()
    )

    data class Template(
        @SerializedName("Contents")
        val contents: List<TemplateContent> = emptyList(),
    )

    data class TemplateContent(
        @SerializedName("Key")
        val key: String = "",

        @SerializedName("Value")
        val value: String = "",
    )
}
