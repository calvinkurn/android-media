package com.tokopedia.product.addedit.shipment.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentModule
import com.tokopedia.product.addedit.shipment.di.DaggerAddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment

class AddEditProductShipmentActivity : BaseSimpleActivity(), HasComponent<AddEditProductShipmentComponent> {

    override fun getNewFragment(): Fragment = AddEditProductShipmentFragment.createInstance()

    override fun getComponent(): AddEditProductShipmentComponent {
        return DaggerAddEditProductShipmentComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductShipmentModule(AddEditProductShipmentModule())
                .build()
    }

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductShipmentActivity::class.java)
    }

}
