package com.tokopedia.purchase_platform.features.express_checkout.view.profile

import com.tokopedia.purchase_platform.features.express_checkout.view.profile.uimodel.ProfileUiModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

interface CheckoutProfileActionListener {

    fun onItemSelected(profileUiModel: ProfileUiModel)
}