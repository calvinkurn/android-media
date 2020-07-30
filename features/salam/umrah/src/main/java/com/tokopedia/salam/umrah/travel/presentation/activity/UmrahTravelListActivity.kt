package com.tokopedia.salam.umrah.travel.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.travel.di.DaggerUmrahTravelComponent
import com.tokopedia.salam.umrah.travel.di.UmrahTravelComponent
import com.tokopedia.salam.umrah.travel.presentation.fragment.UmrahTravelListFragment

/**
 * @author by Firman on 04/03/20
 */

class UmrahTravelListActivity : BaseSimpleActivity(), HasComponent<UmrahTravelComponent> {

    override fun getComponent(): UmrahTravelComponent = DaggerUmrahTravelComponent.builder()
            .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
            .build()

    override fun getNewFragment(): Fragment? = UmrahTravelListFragment.createInstance()

    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment is TravelListListener) {
            (fragment as TravelListListener).onBackPressed()
        }
    }

    interface TravelListListener {
        fun onBackPressed()
    }
}