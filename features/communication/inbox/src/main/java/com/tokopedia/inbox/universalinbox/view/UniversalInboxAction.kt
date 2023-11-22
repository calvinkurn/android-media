package com.tokopedia.inbox.universalinbox.view

import android.content.Intent

sealed interface UniversalInboxAction {

    // General Actions
    object RefreshPage : UniversalInboxAction
    object RefreshCounter : UniversalInboxAction

    // Navigation Actions
    data class NavigateWithIntent(val intent: Intent) : UniversalInboxAction
    data class NavigateToPage(val applink: String) : UniversalInboxAction

    // Widget Actions
    object RefreshDriverWidget : UniversalInboxAction

    // Recommendation Actions
    object RefreshRecommendation : UniversalInboxAction
    object LoadNextPage : UniversalInboxAction
    object ResetUserScrollState : UniversalInboxAction
    object AutoScrollRecommendation : UniversalInboxAction
}
