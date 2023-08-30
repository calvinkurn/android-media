package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
import com.tokopedia.catalogcommon.util.textColorMapping

data class ExpertReviewUiModel(
    override val idWidget: String,
    override val widgetType: String,
    override val widgetName: String,
    override val widgetBackgroundColor: Int? = null,
    override val widgetTextColor: Int? = null,
    override val darkMode: Boolean = false,
    val content: List<ItemExpertReviewUiModel>
) : BaseCatalogUiModel(
    idWidget, widgetType, widgetName, widgetBackgroundColor, widgetTextColor,
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
    )

    companion object {
        private const val dummyDarkMode = false
        fun dummyExpertReview() = ExpertReviewUiModel(
            "dummy", "", "", "#FFFFFF".stringHexColorParseToInt(),darkMode = dummyDarkMode,
            content = listOf(
                ItemExpertReviewUiModel(
                    "https://upload.wikimedia.org/wikipedia/id/1/12/David_Brendi.jpeg",
                    "Boosts your product's credibility using endorsements from trusted industry experts and influencers. By spotlighting these reviews, the widget nurtures trust in potential buyers, aiding informed choices.",
                    "David Gadgetin",
                    "1 line credibility",
                    "https://youtu.be/a3KriqTgaQE?t=316",
                    textReviewColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textTitleColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textSubTitleColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588")
                ),
                ItemExpertReviewUiModel(
                    "https://upload.wikimedia.org/wikipedia/id/1/12/David_Brendi.jpeg",
                    "Boosts your product's credibility using endorsements from trusted industry experts and influencers. By spotlighting these reviews, the widget nurtures trust in potential buyers, aiding informed choices.",
                    "David Gadgetin",
                    "1 line credibility",
                    "https://youtu.be/a3KriqTgaQE?t=316",
                    textReviewColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textTitleColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textSubTitleColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588")
                ),
                ItemExpertReviewUiModel(
                    "https://upload.wikimedia.org/wikipedia/id/1/12/David_Brendi.jpeg",
                    "Boosts your product's credibility using endorsements from trusted industry experts and influencers. By spotlighting these reviews, the widget nurtures trust in potential buyers, aiding informed choices.",
                    "David Gadgetin",
                    "1 line credibility",
                    "https://youtu.be/a3KriqTgaQE?t=316",
                    textReviewColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textTitleColor = textColorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textSubTitleColor = textColorMapping(dummyDarkMode, "#AEB2BF", "#6D7588")
                )
            )
        )
    }
}
