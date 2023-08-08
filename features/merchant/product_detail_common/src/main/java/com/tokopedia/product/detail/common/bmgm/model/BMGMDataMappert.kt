package com.tokopedia.product.detail.common.bmgm.model

import com.tokopedia.product.detail.common.bmgm.ui.model.BMGMUiModel

/**
 * Created by yovi.putra on 02/08/23"
 * Project name: android-tokopedia-core
 **/

fun BMGMData.Data.Content.asUiModel(loadMoreText: String) = BMGMUiModel.Product(
    imageUrl = imageUrl,
    loadMoreText = loadMoreText
)

fun List<BMGMData.Data.Content>.asUiModel(loadMoreText: String) = mapIndexed { index, content ->
    content.asUiModel(
        loadMoreText = if (index == size.dec()) {
            loadMoreText
        } else {
            ""
        }
    )
}

fun BMGMData.Data.Action.asUiModel() = BMGMUiModel.Action(
    type = type,
    link = link
)

fun BMGMData.Data.asUiModel(separator: String) = BMGMUiModel(
    title = title,
    iconUrl = iconUrl,
    products = contents.asUiModel(loadMoreText = loadMoreText),
    backgroundColor = backgroundColor,
    action = action.asUiModel(),
    titleColor = titleColor,
    separator = separator
)
