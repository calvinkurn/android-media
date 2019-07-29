package com.tokopedia.purchase_platform.express_checkout.view.profile

import com.tokopedia.purchase_platform.express_checkout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

interface CheckoutProfileActionListener {

    fun onItemSelected(profileViewModel: ProfileViewModel)
}