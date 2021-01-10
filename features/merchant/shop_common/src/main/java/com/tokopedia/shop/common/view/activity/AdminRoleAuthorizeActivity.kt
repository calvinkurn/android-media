package com.tokopedia.shop.common.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.common.constant.AccessId
import com.tokopedia.shop.common.view.fragment.AdminRoleAuthorizeFragment

class AdminRoleAuthorizeActivity: BaseSimpleActivity() {

    companion object {
        internal const val KEY_ACCESS_ID = "access_id"

        @JvmStatic
        fun createIntent(context: Context, @AccessId accessId: Int): Intent =
                Intent(context, AdminRoleAuthorizeActivity::class.java)
                        .putExtra(KEY_ACCESS_ID, accessId)

    }

    private val accessId: Int by lazy {
        intent?.getIntExtra(KEY_ACCESS_ID, 0) ?: 0
    }

    override fun getNewFragment(): Fragment = AdminRoleAuthorizeFragment.createInstance(accessId)

}