package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play.broadcaster.type.EtalaseType

/**
 * Created by jegul on 26/05/20
 */

data class PlayEtalaseUiModel(
        val id: Long,
        val name: String,
        val productMap: MutableMap<Int, List<ProductContentUiModel>>,
        val totalProduct: Int,
        val stillHasProduct: Boolean,
        val type: EtalaseType
) {

    companion object {

        fun Empty(id: Long = -1, name: String = "") =
                PlayEtalaseUiModel(id, name, mutableMapOf(), 0, false, EtalaseType.Unknown)
    }
}