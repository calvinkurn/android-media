package com.tokopedia.media.picker.common.ui.fragment

import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment

class TestPermissionFragment : PermissionFragment() {

    override fun initInjector() {

    }

    companion object {
        fun create(): TestPermissionFragment {
            return TestPermissionFragment()
        }
    }

}