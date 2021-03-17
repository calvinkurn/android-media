package com.tokopedia.sellerorder.confirmshipping.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.sellerorder.SomComponentInstance
import com.tokopedia.sellerorder.common.presenter.activities.BaseSomActivity
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.confirmshipping.di.DaggerSomConfirmShippingComponent
import com.tokopedia.sellerorder.confirmshipping.di.SomConfirmShippingComponent
import com.tokopedia.sellerorder.confirmshipping.presentation.fragment.SomConfirmShippingFragment
import kotlinx.android.synthetic.main.fragment_som_confirm_shipping.*

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingActivity: BaseSomActivity(), HasComponent<SomConfirmShippingComponent> {
    override fun getNewFragment(): Fragment? {
        var bundle = Bundle()
        if (intent.extras != null) {
            bundle = intent.extras ?: Bundle()
        } else {
            bundle.putString(PARAM_ORDER_ID, "")
            bundle.putBoolean(PARAM_CURR_IS_CHANGE_SHIPPING, false)
        }
        return SomConfirmShippingFragment.newInstance(bundle)
    }

    override fun getComponent(): SomConfirmShippingComponent =
        DaggerSomConfirmShippingComponent.builder()
                .somComponent(SomComponentInstance.getSomComponent(application))
                .build()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let { it ->
            val barcode = getBarcode(requestCode, resultCode, it)
            tf_no_resi?.textFieldInput?.setText(barcode)
            super.onActivityResult(requestCode, resultCode, it)
        }
    }

    private fun getBarcode(requestCode: Int, resultCode: Int, data: Intent): String {
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        return if (scanResult?.contents != null) {
            scanResult.contents
        } else ""
    }
}