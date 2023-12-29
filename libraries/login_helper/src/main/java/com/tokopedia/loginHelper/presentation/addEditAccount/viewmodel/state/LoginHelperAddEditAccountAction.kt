package com.tokopedia.loginHelper.presentation.addEditAccount.viewmodel.state

sealed class LoginHelperAddEditAccountAction {
    object TapBackAction : LoginHelperAddEditAccountAction()
    object GoToLoginHelperHome : LoginHelperAddEditAccountAction()

    object OnSuccessAddDataToRest : LoginHelperAddEditAccountAction()

    object OnFailureAddDataToRest : LoginHelperAddEditAccountAction()
    object OnSuccessEditUserData: LoginHelperAddEditAccountAction()
    object OnFailureEditUserData: LoginHelperAddEditAccountAction()
}
