package com.tokopedia.expresscheckout.view.profile

import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 16/01/19.
 */

interface CheckoutProfileFragmentListener {

    fun onContinueWithoutProfile()

    fun onProfileChanged(selectedProfileViewModel: ProfileViewModel)

}