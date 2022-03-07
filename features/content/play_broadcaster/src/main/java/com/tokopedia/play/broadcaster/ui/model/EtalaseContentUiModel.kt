package com.tokopedia.play.broadcaster.ui.model

/**
 * Created by jegul on 26/05/20
 */
sealed class EtalaseUiModel

data class EtalaseContentUiModel(
        val id: String,
        val name: String,
        val productMap: MutableMap<Int, List<ProductContentUiModel>>,
        val totalProduct: Int,
        val stillHasProduct: Boolean
) : EtalaseUiModel() {

    companion object {

        fun empty(id: String = "-1", name: String = "") =
                EtalaseContentUiModel(id, name, mutableMapOf(), 0, false)
    }
}

object EtalaseLoadingUiModel : EtalaseUiModel()