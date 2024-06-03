package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.ColorConst.DMS_COLOR_WHITE
import com.tokopedia.catalogcommon.R as catalogcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

data class ExpertReviewUiModel(
    override var idWidget: String = "",
    override var widgetType: String = "",
    override var widgetName: String = "",
    override var widgetBackgroundColor: Int? = null,
    override var widgetTextColor: Int? = null,
    override var darkMode: Boolean = false,
    val content: List<ItemExpertReviewUiModel>
) : BaseCatalogUiModel(
    idWidget,
    widgetType,
    widgetName,
    widgetBackgroundColor,
    widgetTextColor,
    darkMode
) {

    override fun type(typeFactory: CatalogAdapterFactory): Int {
        return typeFactory.type(this)
    }

    data class ItemExpertReviewUiModel(
        val imageReviewer: String,
        val reviewText: String,
        val title: String,
        val subTitle: String,
        val videoLink: String,
        val textReviewColor: Int,
        val textTitleColor: Int,
        val textSubTitleColor: Int,
        val backgroundColor: String = DMS_COLOR_WHITE,
        val styleIconPlay: StyleIconPlay = StyleIconPlay()
    )

    data class StyleIconPlay(
        val iconColor: Int = unifyprinciplesR.color.Unify_Static_White,
        val background: Int = unifyprinciplesR.color.Unify_Static_White
    )
}
