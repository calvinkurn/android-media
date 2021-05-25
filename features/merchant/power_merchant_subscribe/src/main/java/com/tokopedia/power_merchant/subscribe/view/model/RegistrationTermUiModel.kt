package com.tokopedia.power_merchant.subscribe.view.model

/**
 * Created By @ilhamsuaib on 03/03/21
 */

data class RegistrationTermUiModel(
        val title: String,
        val descriptionHtml: String,
        val resDrawableIcon: Int,
        val clickableText: String? = null,
        val appLinkOrUrl: String? = null
)