package com.tokopedia.inbox.universalinbox.view

sealed class UniversalInboxAction {
    // General Actions
    data class NavigateToPage(val applink: String) : UniversalInboxAction()
    data class ShowErrorMessage(val error: Pair<Throwable, String>) : UniversalInboxAction()
    object RefreshPage : UniversalInboxAction()

    // Widget Actions

    // TopAds Actions

    // Recommendation Actions
}
