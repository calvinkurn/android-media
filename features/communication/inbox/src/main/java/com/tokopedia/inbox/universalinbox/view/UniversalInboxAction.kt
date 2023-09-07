package com.tokopedia.inbox.universalinbox.view

import android.content.Intent

sealed class UniversalInboxAction {

    // General Actions
    data class ShowErrorMessage(val error: Pair<Throwable, String>) : UniversalInboxAction()
    object RefreshPage : UniversalInboxAction()

    // Navigation Actions
    data class NavigateWithIntent(val intent: Intent) : UniversalInboxAction()
    data class NavigateToPage(val applink: String) : UniversalInboxAction()
    object ResetNavigation : UniversalInboxAction()

    // Widget Actions

    // TopAds Actions

    // Recommendation Actions
    object RefreshRecommendation : UniversalInboxAction()
    data class LoadNextPage(val page: Int) : UniversalInboxAction()
}
