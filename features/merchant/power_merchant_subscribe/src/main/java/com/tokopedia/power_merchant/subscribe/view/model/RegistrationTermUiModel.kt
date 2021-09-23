package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 03/03/21
 */

sealed class RegistrationTermUiModel(
    open val title: String,
    open val descriptionHtml: String,
    open val resDrawableIcon: Int,
    open val isChecked: Boolean,
    open val clickableText: String? = null,
    open val appLinkOrUrl: String? = null,
    open val isNewSeller: Boolean = false,
    open val isFirstMondayNewSeller: Boolean = false,
    val periority: Int = 0
) {
    data class ShopScore(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
        override val isNewSeller: Boolean,
        override val isFirstMondayNewSeller: Boolean
    ) : RegistrationTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        isNewSeller,
        isFirstMondayNewSeller,
        1
    )

    data class ActiveProduct(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null
    ) : RegistrationTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        periority = 1
    )

    data class Order(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
        override val isNewSeller: Boolean,
        override val isFirstMondayNewSeller: Boolean
    ) : RegistrationTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        isNewSeller,
        isFirstMondayNewSeller,
        2
    )

    data class NetItemValue(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
        override val isNewSeller: Boolean,
        override val isFirstMondayNewSeller: Boolean
    ) : RegistrationTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        isNewSeller,
        isFirstMondayNewSeller,
        3
    )

    data class Kyc(
        override val title: String,
        override val descriptionHtml: String,
        override val resDrawableIcon: Int,
        override val isChecked: Boolean,
        override val clickableText: String? = null,
        override val appLinkOrUrl: String? = null,
        override val isNewSeller: Boolean,
        override val isFirstMondayNewSeller: Boolean
    ) : RegistrationTermUiModel(
        title,
        descriptionHtml,
        resDrawableIcon,
        isChecked,
        clickableText,
        appLinkOrUrl,
        isNewSeller,
        isFirstMondayNewSeller,
        4
    )
}