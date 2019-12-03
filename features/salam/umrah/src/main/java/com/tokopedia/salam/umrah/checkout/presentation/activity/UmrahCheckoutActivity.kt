package com.tokopedia.salam.umrah.checkout.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.di.DaggerUmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.fragment.UmrahCheckoutFragment
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by firman on 4/11/2019
 */

class UmrahCheckoutActivity : BaseSimpleActivity(), HasComponent<UmrahCheckoutComponent> {

    lateinit var userSession: UserSessionInterface
        @Inject set

    override fun getNewFragment(): Fragment? = UmrahCheckoutFragment.getInstance(
            intent.getStringExtra(EXTRA_SLUG_NAME),
            intent.getStringExtra(EXTRA_VARIANT),
            intent.getIntExtra(EXTRA_PRICE,0),
            intent.getIntExtra(EXTRA_TOTAL_PRICE, 0),
            intent.getIntExtra(EXTRA_TOTAL_PASSENGER,0),
            intent.getStringExtra(EXTRA_DEPART_DATE)
    )

    override fun getComponent(): UmrahCheckoutComponent =
            DaggerUmrahCheckoutComponent.builder()
                    .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
                    .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object{
        const val EXTRA_PRICE = "EXTRA_PRICE"
        const val EXTRA_TOTAL_PRICE = "EXTRA_TOTAL_PRICE"
        const val EXTRA_SLUG_NAME = "EXTRA_SLUG_NAME"
        const val EXTRA_VARIANT = "EXTRA_VARIANT"
        const val EXTRA_TOTAL_PASSENGER = "EXTRA_TOTAL_PASSENGER"
        const val EXTRA_DEPART_DATE = "EXTRA_DEPART_DATE"


        fun createIntent(context:Context, slugName: String, variant:String, price: Int,
                         totalPrice:Int, totalPassenger:Int, departDate: String
        ):Intent = Intent(context,UmrahCheckoutActivity::class.java)
                .putExtra(EXTRA_PRICE,price)
                .putExtra(EXTRA_TOTAL_PRICE,totalPrice)
                .putExtra(EXTRA_SLUG_NAME, slugName)
                .putExtra(EXTRA_VARIANT, variant)
                .putExtra(EXTRA_TOTAL_PASSENGER, totalPassenger)
                .putExtra(EXTRA_DEPART_DATE, departDate)

    }

    interface OnBackListener {
        fun onBackPress()
    }

    override fun onBackPressed() {
        if (fragment is OnBackListener) {
            (fragment as OnBackListener).onBackPress()
        }

        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.umrah_checkout_dialog_title))
        dialog.setDescription(getString(R.string.umrah_checkout_dialog_desc))
        dialog.setSecondaryCTAText(getString(R.string.umrah_checkout_dialog_cancel_desc))
        dialog.setPrimaryCTAText(getString(R.string.umrah_checkout_dialog_ok_desc))
        dialog.show()
        dialog.setPrimaryCTAClickListener {
            dialog.dismiss()
        }

        dialog.setSecondaryCTAClickListener {
                dialog.dismiss()
            super.onBackPressed()

        }

    }
}