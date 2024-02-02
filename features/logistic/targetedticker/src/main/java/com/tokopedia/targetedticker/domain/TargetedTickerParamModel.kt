package com.tokopedia.targetedticker.domain

import android.annotation.SuppressLint

data class TargetedTickerParamModel(
    @SuppressLint("ParamFieldAnnotation")
    val page: String = "",
    @SuppressLint("ParamFieldAnnotation")
    val template: Template = Template(),
    @SuppressLint("ParamFieldAnnotation")
    val target: List<Target> = listOf()

) {

    data class Template(
        @SuppressLint("ParamFieldAnnotation")
        val contents: List<Content> = listOf()
    ) {
        data class Content(
            @SuppressLint("ParamFieldAnnotation")
            val key: String = "",
            @SuppressLint("ParamFieldAnnotation")
            val value: String = ""
        )
    }

    data class Target(
        @SuppressLint("ParamFieldAnnotation")
        val type: String = "",
        @SuppressLint("ParamFieldAnnotation")
        val values: List<String> = listOf()
    )
}
