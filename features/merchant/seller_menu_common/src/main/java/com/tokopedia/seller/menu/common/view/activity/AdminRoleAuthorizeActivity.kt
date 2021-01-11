package com.tokopedia.seller.menu.common.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.fragment.AdminRoleAuthorizeFragment

class AdminRoleAuthorizeActivity: BaseSimpleActivity() {

    companion object {
        internal const val KEY_ADMIN_FEATURE = "admin_feature"

        @JvmStatic
        fun createIntent(context: Context, @AdminFeature adminFeature: String): Intent =
                Intent(context, AdminRoleAuthorizeActivity::class.java)
                        .putExtra(KEY_ADMIN_FEATURE, adminFeature)

    }

    // Manually get feature from manual intent or applink (tokopedia-android-internal://sellerapp/admin-authorize/{feature}/)
    private val adminFeature: String by lazy {
        intent?.getStringExtra(KEY_ADMIN_FEATURE) ?: intent?.data?.lastPathSegment.orEmpty()
    }

    override fun getNewFragment(): Fragment = AdminRoleAuthorizeFragment.createInstance(adminFeature)

}