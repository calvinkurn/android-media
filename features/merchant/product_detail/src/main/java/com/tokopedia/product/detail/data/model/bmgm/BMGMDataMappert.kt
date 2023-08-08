package com.tokopedia.product.detail.data.model.bmgm

import com.tokopedia.product.detail.view.viewholder.bmgm.model.BMGMWidgetUiModel

/**
 * Created by yovi.putra on 02/08/23"
 * Project name: android-tokopedia-core
 **/

fun BMGMData.Data.Content.asUiModel(loadMoreText: String) = BMGMWidgetUiModel.Product(
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

fun BMGMData.Data.Action.asUiModel() = BMGMWidgetUiModel.Action(
    type = type,
    link = link
)

fun BMGMData.Data.asUiModel(separator: String) =
    BMGMWidgetUiModel(
        title = title,
        iconUrl = iconUrl,
        products = contents.asUiModel(loadMoreText = loadMoreText),
        backgroundColor = backgroundColor,
        action = action.asUiModel(),
        titleColor = titleColor,
        separator = separator
    )
