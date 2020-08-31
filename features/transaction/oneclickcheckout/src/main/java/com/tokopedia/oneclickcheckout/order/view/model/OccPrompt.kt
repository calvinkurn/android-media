package com.tokopedia.oneclickcheckout.order.view.model

data class OccPrompt(
        val from: Int = 0,
        val type: String = "",
        val title: String = "",
        val description: String = "",
        val buttons: List<OccPromptButton> = emptyList()
) {
    companion object {
        const val FROM_CART = 0
        const val FROM_CHECKOUT = 1

        const val TYPE_DIALOG = "dialog_box"
    }

    fun shouldShowPrompt(): Boolean {
        return type == TYPE_DIALOG
    }

    fun getPrimaryButton(): OccPromptButton? {
        return buttons.firstOrNull { it.color == OccPromptButton.COLOR_PRIMARY }
    }

    fun getSecondButton(primaryButton: OccPromptButton): OccPromptButton? {
        return buttons.firstOrNull { it != primaryButton }
    }

    fun hasReloadActionButton(): Boolean {
        return buttons.firstOrNull { it.action == OccPromptButton.ACTION_RELOAD } != null
    }
}

data class OccPromptButton(
        val text: String = "",
        val link: String = "",
        val action: String = "",
        val color: String = ""
) {
    companion object {
        const val ACTION_OPEN = "open"
        const val ACTION_RELOAD = "reload"

        const val COLOR_PRIMARY = "primary"
    }
}