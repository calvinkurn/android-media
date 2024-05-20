package com.tokopedia.home_component.widget.card

data class SmallProductModel(
    val bannerImageUrl: String,
    val stockBar: StockBar = StockBar(),
    val labelGroupList: List<LabelGroup> = listOf(),
) {

    // default
    fun title() = extractLabelStyleable(LabelGroupPosition.TITLE)
    fun subtitle() = extractLabelStyleable(LabelGroupPosition.SUBTITLE)

    // ribbon
    fun ribbon(): Ribbon? {
        val labelGroup = labelGroup(LabelGroupPosition.RIBBON)
            ?.takeIf(LabelGroup::hasTitle)
            ?: return null

        return Ribbon(
            labelGroup.title,
            if (labelGroup.type == DEF_STYLE_RIBBON_RED) {
                Ribbon.Type.Red
            } else {
                Ribbon.Type.Gold
            }
        )
    }

    private fun extractLabelStyleable(position: String): Pair<String, TextStyle> {
        val labelGroup = labelGroup(position)

        return Pair(
            labelGroup?.title.orEmpty(),
            TextStyle(
                url = labelGroup?.url.orEmpty(),
                isBold = labelGroup?.isTextBold() == true,
                isTextStrikethrough = labelGroup?.isTextStrikethrough() == true,
                textColor = labelGroup?.textColor().orEmpty(),
                shouldRenderHtmlFormat = labelGroup?.hashTextFormat() == false
            )
        )
    }

    private fun labelGroup(position: String) = labelGroupList.find { it.position == position }

    data class Ribbon(
        val text: String,
        val type: Type,
    ) {

        sealed class Type {
            object Red : Type()
            object Gold : Type()
        }
    }

    data class TextStyle(
        val isBold: Boolean = false,
        val isTextStrikethrough: Boolean = false,
        val textColor: String = "",
        val url: String = "",
        val shouldRenderHtmlFormat: Boolean = false
    )

    data class StockBar(
        val isEnabled: Boolean = false,
        val percentage: Int = 0
    ) {

        fun shouldHandleFireIconVisibility(): Type {
            return when(percentage) {
                0 -> Type.Inactive
                in MIN_THRESHOLD..MAX_THRESHOLD -> Type.ActiveWithFire
                else -> Type.ActiveWithoutFire
            }
        }

        fun percentageOnFireRange() =
            percentage in MIN_THRESHOLD..MAX_THRESHOLD

        sealed class Type {
            object ActiveWithFire : Type()
            object ActiveWithoutFire : Type()
            object Inactive : Type()
        }

        companion object {
            const val MIN_THRESHOLD = 20
            const val MAX_THRESHOLD = 95
        }
    }

    data class LabelGroup(
        val position: String,
        val title: String,
        val type: String,
        val url: String,
        val styles: List<Styles> = listOf(),
    ) {

        private val style = styles.associate { it.key to it.value }

        fun hasTitle() = title.isNotBlank()
        fun hashTextFormat() = style[LabelGroupStyle.TEXT_FORMAT] != null
        fun isTextBold() = style[LabelGroupStyle.TEXT_FORMAT] == DEF_STYLE_TEXT_BOLD
        fun isTextStrikethrough() = style[LabelGroupStyle.TEXT_FORMAT] == DEF_STYLE_TEXT_STRIKETHROUGH

        fun textColor() = style[LabelGroupStyle.TEXT_COLOR]
        fun backgroundColor() = style[LabelGroupStyle.BACKGROUND_COLOR]
        fun backgroundOpacity() = style[LabelGroupStyle.BACKGROUND_OPACITY]?.toFloatOrNull()
        fun outlineColor(): String? = style[LabelGroupStyle.OUTLINE_COLOR]

        data class Styles(val key: String = "", val value: String = "") {

            companion object {
                internal fun from(style: Styles) = Styles(style.key, style.value)
            }
        }

        companion object {
            internal fun from(group: LabelGroup): LabelGroup {
                return LabelGroup(
                    position = group.position,
                    title = group.title,
                    type = group.type,
                    url = group.url,
                    styles = group.styles.map(Styles::from),
                )
            }
        }
    }
}
