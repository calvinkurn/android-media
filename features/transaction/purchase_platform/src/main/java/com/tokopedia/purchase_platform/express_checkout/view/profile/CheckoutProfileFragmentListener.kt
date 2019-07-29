package com.tokopedia.purchase_platform.express_checkout.view.profile

import com.tokopedia.purchase_platform.express_checkout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

interface CheckoutProfileFragmentListener {

    fun onContinueWithoutProfile()

    fun onProfileChanged(selectedProfileViewModel: ProfileViewModel)

    fun onChangeTemplateBottomshictButtonCloseClicked()

}