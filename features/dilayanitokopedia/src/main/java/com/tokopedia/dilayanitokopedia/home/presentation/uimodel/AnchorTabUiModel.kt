package com.tokopedia.dilayanitokopedia.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by irpan on 02/11/22.
 */
data class AnchorTabUiModel(
    val id: String = "",
    var title: String = "",
    val imageUrl: String,
    val groupId: String,
    var visitable: Visitable<*>? = null
)
