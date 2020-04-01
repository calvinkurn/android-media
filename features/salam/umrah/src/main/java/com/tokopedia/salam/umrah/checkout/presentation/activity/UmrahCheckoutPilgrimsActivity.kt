package com.tokopedia.salam.umrah.checkout.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.checkout.di.DaggerUmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutPilgrimsFragment
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance


/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutPilgrimsActivity : BaseSimpleActivity(),HasComponent<UmrahCheckoutComponent> {


    override fun getNewFragment(): Fragment? = UmrahCheckoutPilgrimsFragment.createInstance(
            intent.getParcelableExtra(EXTRA_PILGRIMS)
    )

    override fun getComponent(): UmrahCheckoutComponent =
            DaggerUmrahCheckoutComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    companion object {
        const val EXTRA_PILGRIMS = "EXTRA_PILGRIMS"
        private const val ARG_SAVED_TAG = "saved_fragment_tag"
        fun createIntent(context: Context, dataPilgrims: UmrahCheckoutPilgrims): Intent =
                Intent(context, UmrahCheckoutPilgrimsActivity::class.java)
                        .putExtra(EXTRA_PILGRIMS, dataPilgrims)
    }
}