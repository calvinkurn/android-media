package com.tokopedia.product.addedit.shipment.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentModule
import com.tokopedia.product.addedit.shipment.di.DaggerAddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment

class AddEditProductShipmentActivity : BaseSimpleActivity(), HasComponent<AddEditProductShipmentComponent> {

    companion object {
        fun createInstance(context: Context?, cacheManagerId: String?): Intent =
                Intent(context, AddEditProductShipmentActivity::class.java)
                        .putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)

    }

    override fun getNewFragment(): Fragment {
        val cacheManagerId = intent?.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        return AddEditProductShipmentFragment.createInstance(cacheManagerId)
    }

    override fun getComponent(): AddEditProductShipmentComponent {
        return DaggerAddEditProductShipmentComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductShipmentModule(AddEditProductShipmentModule())
                .build()
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        sendDataBack()
    }

    private fun sendDataBack() {
        val f = fragment
        if (f != null && f is AddEditProductShipmentFragment) {
            f.sendDataBack()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductShipmentFragment) {
            f.onBackPressed()
        }
    }
}
