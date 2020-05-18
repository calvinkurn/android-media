package com.tokopedia.sellerhome.settings.view.uimodel.base

sealed class DividerType {
    object THICK : DividerType()
    object THIN_FULL : DividerType()
    object THIN_PARTIAL : DividerType()
    object THIN_INDENTED : DividerType()
}