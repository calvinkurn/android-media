package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state

sealed class LoginHelperAddEditAccountAction {
    data class GoToRoute(val route: String) : LoginHelperAddEditAccountAction()
    object GoToLoginHelperHome : LoginHelperAddEditAccountAction()
    object OnSuccessAddDataToRest : LoginHelperAddEditAccountAction()
    object OnFailureAddDataToRest : LoginHelperAddEditAccountAction()
    object OnSuccessEditUserData : LoginHelperAddEditAccountAction()
    object OnFailureEditUserData : LoginHelperAddEditAccountAction()
}
