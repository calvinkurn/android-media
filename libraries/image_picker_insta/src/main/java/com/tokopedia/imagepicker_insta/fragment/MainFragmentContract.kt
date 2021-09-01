package com.tokopedia.imagepicker_insta.fragment

import com.tokopedia.unifycomponents.Toaster

interface MainFragmentContract {
    fun handleOnCameraIconTap()
    fun showToast(message: String, toasterType: Int = Toaster.TYPE_NORMAL)
}