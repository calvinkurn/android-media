package com.tokopedia.picker.fake.ui.fragment

import com.tokopedia.picker.ui.fragment.permission.PermissionFragment

class TestPermissionFragment : PermissionFragment() {

    override fun initInjector() {

    }

    companion object {
        fun create(): TestPermissionFragment {
            return TestPermissionFragment()
        }
    }

}