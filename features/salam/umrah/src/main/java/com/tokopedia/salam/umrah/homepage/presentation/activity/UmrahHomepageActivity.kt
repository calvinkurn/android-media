package com.tokopedia.salam.umrah.homepage.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.common.presentation.activity.UmrahBaseActivity
import com.tokopedia.salam.umrah.homepage.di.DaggerUmrahHomepageComponent
import com.tokopedia.salam.umrah.homepage.di.UmrahHomepageComponent
import com.tokopedia.salam.umrah.homepage.presentation.fragment.UmrahHomepageFragment

class UmrahHomepageActivity : UmrahBaseActivity(), HasComponent<UmrahHomepageComponent> {
    override fun getNewFragment(): Fragment? =
            UmrahHomepageFragment.getInstance()

    override fun getComponent(): UmrahHomepageComponent =
            DaggerUmrahHomepageComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun shouldShowMenuWhite() = false

    override fun getMenuButton(): Int = R.menu.umrah_base_menu
    override fun getShareLink(): String = getString(R.string.umrah_link)


}
