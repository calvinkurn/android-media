package com.tokopedia.purchase_platform.features.express_checkout.view.profile

import com.tokopedia.purchase_platform.features.express_checkout.view.profile.uimodel.ProfileUiModel

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

interface CheckoutProfileFragmentListener {

    fun onContinueWithoutProfile()

    fun onProfileChanged(selectedProfileUiModel: ProfileUiModel)

    fun onChangeTemplateBottomshictButtonCloseClicked()

}