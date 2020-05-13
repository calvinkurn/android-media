package com.tokopedia.changepassword.common.analytics

import com.tokopedia.track.TrackApp

/**
 * @author by nisie on 7/25/18.
 */
class ChangePasswordAnalytics{

    private val tracker = TrackApp.getInstance().gtm

    fun onClickSubmit() {

    }

    fun onSuccessChangePassword() {

    }

    fun onErrorValidate(message: String) {

    }

    companion object {
        const val SCREEN_NAME: String = "Change Password Page"
    }
}