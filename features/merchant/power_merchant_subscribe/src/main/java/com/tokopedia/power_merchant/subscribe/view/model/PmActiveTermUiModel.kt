package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 03/03/21
 */

sealed class PmActiveTermUiModel(
    open val title: String,
    open val descriptionHtml: String,
    open val resDrawableIcon: Int,
    open val isChecked: Boolean,
    open val clickableText: String? = null,
    open val appLinkOrUrl: String? = null,
    val priority: Int = PRIORITY_0
) {

    companion object {
        private const val PRIORITY_0 = 0
        private const val PRIORITY_1 = 1
        private const val PRIORITY_2 = 2
        private const val PRIORITY_3 = 3
    }

    data class ShopScore(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
    ) : PmActiveTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        PRIORITY_1
    )

    data class Order(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
    ) : PmActiveTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        PRIORITY_2
    )

    data class NetItemValue(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
    ) : PmActiveTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        PRIORITY_3
    )
}