package com.tokopedia.salam.umrah.checkout.presentation.activity

import android.content.Context
import android.content.Intent
import android.transition.Scene
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.checkout.data.Schemes
import com.tokopedia.salam.umrah.checkout.data.UmrahCheckoutPilgrims
import com.tokopedia.salam.umrah.checkout.di.DaggerUmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutInstallmentFragment
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance

/**
 * @author by firman on 18/11/2019
 */

class UmrahCheckoutInstallmentActivity : BaseSimpleActivity(), HasComponent<UmrahCheckoutComponent> {

    override fun getNewFragment(): Fragment? = UmrahCheckoutInstallmentFragment.getInstance(
            intent.getParcelableArrayListExtra(EXTRA_SCHEMES),
            intent.getIntExtra(EXTRA_DEFAULT_OPTION_SCHEMES,0)
    )

    override fun getComponent(): UmrahCheckoutComponent =
            DaggerUmrahCheckoutComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()
    companion object {
        const val EXTRA_SCHEMES = "EXTRA_SCHEMES"
        const val EXTRA_DEFAULT_OPTION_SCHEMES = "EXTRA_DEFAULT_OPTION_SCHEMES"

        fun createIntent(context: Context, schemes: ArrayList<Schemes>, defaultOptionSchemes: Int): Intent =
                Intent(context, UmrahCheckoutInstallmentActivity::class.java)
                        .putParcelableArrayListExtra(EXTRA_SCHEMES, schemes)
                        .putExtra(EXTRA_DEFAULT_OPTION_SCHEMES, defaultOptionSchemes)
    }
}