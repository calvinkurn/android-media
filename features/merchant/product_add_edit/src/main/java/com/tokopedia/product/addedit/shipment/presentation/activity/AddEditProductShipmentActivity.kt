package com.tokopedia.product.addedit.shipment.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment

class AddEditProductShipmentActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = AddEditProductShipmentFragment()

    companion object {
        fun createInstance(context: Context?) = Intent(context, AddEditProductShipmentActivity::class.java)
    }

}
