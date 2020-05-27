package com.tokopedia.play.broadcaster.view.uimodel

/**
 * Created by jegul on 26/05/20
 */
data class ProductUiModel(
        val id: Long,
        val name: String,
        val imageUrl: String,
        val stock: Int,
        val isSelected: Boolean
) {

    val hasStock: Boolean
        get() = stock != 0
}