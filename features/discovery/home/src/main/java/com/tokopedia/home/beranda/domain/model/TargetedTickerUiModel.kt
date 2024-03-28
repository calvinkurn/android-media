package com.tokopedia.home.beranda.domain.model

import com.tokopedia.home_component.model.AtfContent

class TargetedTickerUiModel(
    val id: Int,
    val title: String,
    val content: String,
    val type: String,
    val priority: Int,
    val action: Action
) : AtfContent {

    fun getTickerType() =
        when(type) {
            TYPE_INFO -> Type.Info
            TYPE_WARNING -> Type.Warning
            else -> Type.Danger
        }

    data class Action(
        val label: String,
        val type: String,
        val appLink: String,
        val url: String,
    )

    sealed class Type {
        object Info : Type()
        object Warning : Type()
        object Danger : Type()
    }

    companion object {
        private const val TYPE_INFO = "info"
        private const val TYPE_WARNING = "warning"
    }
}
