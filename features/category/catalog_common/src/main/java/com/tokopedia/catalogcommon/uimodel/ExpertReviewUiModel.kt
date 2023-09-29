package com.tokopedia.catalogcommon.uimodel

import com.tokopedia.catalogcommon.adapter.CatalogAdapterFactory
import com.tokopedia.catalogcommon.util.colorMapping
import com.tokopedia.catalogcommon.util.stringHexColorParseToInt
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
        val backgroundColor: Int = catalogcommonR.drawable.bg_rounded_border_light,
        val styleIconPlay: StyleIconPlay = StyleIconPlay()
    )

    data class StyleIconPlay(
        val iconColor: Int = unifyprinciplesR.color.Unify_Static_White,
        val background: Int = catalogcommonR.drawable.bg_circle_border_dark
    )

    companion object {
        private const val dummyDarkMode = false
        fun dummyExpertReview() = ExpertReviewUiModel(
            "dummy", "", "", "#FFFFFF".stringHexColorParseToInt(), darkMode = dummyDarkMode,
            content = listOf(
                ItemExpertReviewUiModel(
                    "https://upload.wikimedia.org/wikipedia/id/1/12/David_Brendi.jpeg",
                    "Boosts your product's credibility using endorsements from trusted industry experts and influencers. By spotlighting these reviews, the widget nurtures trust in potential buyers, aiding informed choices.",
                    "David Gadgetin",
                    "1 line credibility",
                    "https://www.youtube.com/watch?v=vyZTwO74gQo",
                    textReviewColor = colorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textTitleColor = colorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textSubTitleColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = colorMapping(
                        dummyDarkMode,
                        catalogcommonR.drawable.bg_rounded_border_dark,
                        catalogcommonR.drawable.bg_rounded_border_light
                    ),
                    styleIconPlay = StyleIconPlay(
                        iconColor = colorMapping(
                            dummyDarkMode,
                            unifyprinciplesR.color.Unify_Static_White,
                            unifyprinciplesR.color.Unify_Static_Black
                        ),
                        background = colorMapping(
                            dummyDarkMode,
                            catalogcommonR.drawable.bg_circle_border_dark,
                            catalogcommonR.drawable.bg_circle_border_light,
                        )
                    )
                ),
                ItemExpertReviewUiModel(
                    "https://upload.wikimedia.org/wikipedia/id/1/12/David_Brendi.jpeg",
                    "Boosts your product's credibility using endorsements from trusted industry experts and influencers. By spotlighting these reviews, the widget nurtures trust in potential buyers, aiding informed choices.",
                    "David Gadgetin",
                    "1 line credibility",
                    "https://www.youtube.com/watch?v=vyZTwO74gQo",
                    textReviewColor = colorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textTitleColor = colorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textSubTitleColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = colorMapping(
                        dummyDarkMode,
                        catalogcommonR.drawable.bg_rounded_border_dark,
                        catalogcommonR.drawable.bg_rounded_border_light
                    ),
                    styleIconPlay = StyleIconPlay(
                        iconColor = colorMapping(
                            dummyDarkMode,
                            unifyprinciplesR.color.Unify_Static_White,
                            unifyprinciplesR.color.Unify_Static_Black
                        ),
                        background = colorMapping(
                            dummyDarkMode,
                            catalogcommonR.drawable.bg_circle_border_dark,
                            catalogcommonR.drawable.bg_circle_border_light,
                        )
                    )
                ),
                ItemExpertReviewUiModel(
                    "https://upload.wikimedia.org/wikipedia/id/1/12/David_Brendi.jpeg",
                    "Boosts your product's credibility using endorsements from trusted industry experts and influencers. By spotlighting these reviews, the widget nurtures trust in potential buyers, aiding informed choices.",
                    "David Gadgetin",
                    "1 line credibility",
                    "https://www.youtube.com/watch?v=vyZTwO74gQo",
                    textReviewColor = colorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textTitleColor = colorMapping(dummyDarkMode, "#F5F6FF", "#212121"),
                    textSubTitleColor = colorMapping(dummyDarkMode, "#AEB2BF", "#6D7588"),
                    backgroundColor = colorMapping(
                        dummyDarkMode,
                        catalogcommonR.drawable.bg_rounded_border_dark,
                        catalogcommonR.drawable.bg_rounded_border_light
                    ),
                    styleIconPlay = StyleIconPlay(
                        iconColor = colorMapping(
                            dummyDarkMode,
                            unifyprinciplesR.color.Unify_Static_White,
                            unifyprinciplesR.color.Unify_Static_Black
                        ),
                        background = colorMapping(
                            dummyDarkMode,
                            catalogcommonR.drawable.bg_circle_border_dark,
                            catalogcommonR.drawable.bg_circle_border_light,
                        )
                    )
                )
            )

        )

    }
}
