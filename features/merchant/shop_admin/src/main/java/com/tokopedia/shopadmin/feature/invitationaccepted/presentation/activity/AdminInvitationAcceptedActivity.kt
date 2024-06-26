package com.tokopedia.shopadmin.feature.invitationaccepted.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.shopadmin.common.utils.ShopAdminComponentInstance
import com.tokopedia.shopadmin.feature.invitationaccepted.di.component.AdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.di.component.DaggerAdminInvitationAcceptedComponent
import com.tokopedia.shopadmin.feature.invitationaccepted.presentation.fragment.AdminInvitationAcceptedFragment

class AdminInvitationAcceptedActivity: BaseSimpleActivity(), HasComponent<AdminInvitationAcceptedComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    override fun getNewFragment(): Fragment {
        val bundle = intent.data?.let {
            Bundle().apply {
                val params = UriUtil.uriQueryParamsToMap(it)
                val shopNameParam = params[ShopAdminDeepLinkMapper.SHOP_NAME].orEmpty()
                putString(ShopAdminDeepLinkMapper.SHOP_NAME, shopNameParam)
            }
        }
        return AdminInvitationAcceptedFragment.newInstance(bundle)
    }

    override fun getComponent(): AdminInvitationAcceptedComponent {
        return DaggerAdminInvitationAcceptedComponent
            .builder()
            .shopAdminComponent(ShopAdminComponentInstance.getShopAdminComponent(application))
            .build()
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }

}