package com.tokopedia.salam.umrah.travel.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.common.presentation.activity.UmrahBaseActivity
import com.tokopedia.salam.umrah.pdp.presentation.fragment.UmrahPdpFragment
import com.tokopedia.salam.umrah.travel.di.DaggerUmrahTravelComponent
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent

class UmrahTravelActivity : UmrahBaseActivity(), HasComponent<UmrahTravelComponent>{
    private var slugName: String = ""

    override fun getMenuButton() = R.menu.umrah_base_menu_white
    override fun getShareLink(): String = getString(R.string.umrah_travel_link_share,slugName)


    override fun getNewFragment(): Fragment?= UmrahPdpFragment.getInstance(slugName)

    override fun getComponent(): UmrahTravelComponent =
            DaggerUmrahTravelComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    companion object {
        const val EXTRA_SLUG_NAME = "EXTRA_SLUG_NAME"
    }
}