package com.tokopedia.play.broadcaster.view.uimodel

/**
 * Created by jegul on 26/05/20
 */
data class PlayEtalaseUiModel(
        val id: Long,
        val name: String,
        val productList: List<ProductUiModel>,
        val totalProduct: Int
)