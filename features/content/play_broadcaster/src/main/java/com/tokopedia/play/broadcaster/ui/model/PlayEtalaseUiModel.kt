package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 26/05/20
 */
data class PlayEtalaseUiModel(
        val id: Long,
        val name: String,
        val productMap: MutableMap<Int, List<ProductUiModel>>,
        val totalProduct: Int,
        val stillHasProduct: Boolean
) {

    companion object {

        val EMPTY = PlayEtalaseUiModel(-1, "", mutableMapOf(), 0, false)
    }
}