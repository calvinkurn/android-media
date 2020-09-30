package com.tokopedia.oneclickcheckout.order.view.model

data class OccPrompt(
        val type: String = "",
        val title: String = "",
        val description: String = "",
        val imageUrl: String = "",
        val buttons: List<OccPromptButton> = emptyList()
) {
    companion object {
        const val TYPE_DIALOG = "dialog_box"
        const val TYPE_BOTTOM_SHEET = "bottom_sheet"
    }

    fun shouldShowPrompt(): Boolean {
        return type == TYPE_DIALOG || type == TYPE_BOTTOM_SHEET
    }

    fun getPrimaryButton(): OccPromptButton? {
        return buttons.firstOrNull { it.color == OccPromptButton.COLOR_PRIMARY }
    }

    fun getSecondButton(primaryButton: OccPromptButton): OccPromptButton? {
        return buttons.firstOrNull { it != primaryButton }
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
        const val ACTION_RETRY = "retry"

        const val COLOR_PRIMARY = "primary"
    }
}