package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 26/05/20
 */

data class PlayEtalaseUiModel(
        val id: String,
        val name: String,
        val productMap: MutableMap<Int, List<ProductContentUiModel>>,
        val totalProduct: Int,
        val stillHasProduct: Boolean
) {

    companion object {

        fun Empty(id: String = "-1", name: String = "") =
                PlayEtalaseUiModel(id, name, mutableMapOf(), 0, false)
    }
}