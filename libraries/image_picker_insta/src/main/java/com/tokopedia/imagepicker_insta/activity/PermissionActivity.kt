package com.tokopedia.imagepicker_insta.activity

import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.imagepicker_insta.util.handleOnRequestPermissionsResult

open class PermissionActivity : BaseActivity() {
    var permissionDeniedCount = 0

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handleOnRequestPermissionsResult(grantResults)
    }
}