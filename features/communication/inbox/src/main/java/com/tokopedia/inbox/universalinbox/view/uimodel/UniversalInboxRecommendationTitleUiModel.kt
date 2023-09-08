package com.tokopedia.inbox.universalinbox.view.uimodel

data class UniversalInboxRecommendationTitleUiModel(
    val text: String
) {
    companion object {
        fun areItemsTheSame(
            oldItem: UniversalInboxRecommendationTitleUiModel,
            newItem: UniversalInboxRecommendationTitleUiModel
        ): Boolean {
            return oldItem.text == newItem.text
        }
    }
}
