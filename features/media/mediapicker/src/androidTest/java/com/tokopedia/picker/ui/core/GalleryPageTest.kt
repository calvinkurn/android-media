package com.tokopedia.picker.ui.core

import android.net.Uri
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.picker.ui.PickerTest
import org.junit.Rule

open class GalleryPageTest : PickerTest() {

    @get:Rule
    var permissionRule = GrantPermissionRule.grant(
        "android.permission.INTERNET",
        "android.permission.CAMERA",
        "android.permission.READ_EXTERNAL_STORAGE",
    )

    override fun buildUri(builder: Uri.Builder) {
        builder.appendQueryParameter(
            ApplinkConst.MediaPicker.PARAM_PAGE,
            ApplinkConst.MediaPicker.VALUE_PAGE_GALLERY
        )
    }

}