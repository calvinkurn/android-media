package com.tokopedia.shopadmin.feature.authorize.presentation.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shopadmin.common.utils.ShopAdminComponentInstance
import com.tokopedia.shopadmin.feature.authorize.di.component.AdminRoleAuthorizeComponent
import com.tokopedia.shopadmin.feature.authorize.di.component.DaggerAdminRoleAuthorizeComponent
import com.tokopedia.shopadmin.feature.authorize.presentation.fragment.AdminRoleAuthorizeFragment

class AdminRoleAuthorizeActivity: BaseSimpleActivity(), HasComponent<AdminRoleAuthorizeComponent> {

    companion object {
        internal const val KEY_ADMIN_FEATURE = "admin_feature"
    }

    // Manually get feature from manual intent or applink (tokopedia-android-internal://sellerapp/admin-authorize/{feature}/)
    private val adminFeature: String by lazy {
        intent?.getStringExtra(KEY_ADMIN_FEATURE) ?: intent?.data?.lastPathSegment.orEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window?.decorView?.setBackgroundColor(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun getNewFragment(): Fragment = AdminRoleAuthorizeFragment.createInstance(adminFeature)

    override fun getComponent(): AdminRoleAuthorizeComponent {
        return DaggerAdminRoleAuthorizeComponent
            .builder()
            .shopAdminComponent(ShopAdminComponentInstance.getShopAdminComponent(application))
            .build()
    }

}
