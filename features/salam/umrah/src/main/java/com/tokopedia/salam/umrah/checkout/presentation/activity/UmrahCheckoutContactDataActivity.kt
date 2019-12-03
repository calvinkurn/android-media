package com.tokopedia.salam.umrah.checkout.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.travel.presentation.model.TravelContactData
import com.tokopedia.salam.umrah.checkout.data.ContactUser
import com.tokopedia.salam.umrah.checkout.di.DaggerUmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutContactDataFragment
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance

class UmrahCheckoutContactDataActivity : BaseSimpleActivity(), HasComponent<UmrahCheckoutComponent> {

    override fun getNewFragment(): Fragment =
          UmrahCheckoutContactDataFragment.getInstance(
                  intent.getParcelableExtra(EXTRA_INITIAL_CONTACT_DATA)
          )

    override fun getComponent(): UmrahCheckoutComponent =
            DaggerUmrahCheckoutComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    companion object {

        const val EXTRA_INITIAL_CONTACT_DATA = "EXTRA_INITIAL_CONTACT_DATA"

        fun getCallingIntent(context: Context, contactData: ContactUser): Intent =
                Intent(context, UmrahCheckoutContactDataActivity::class.java)
                        .putExtra(EXTRA_INITIAL_CONTACT_DATA, contactData)
    }
}