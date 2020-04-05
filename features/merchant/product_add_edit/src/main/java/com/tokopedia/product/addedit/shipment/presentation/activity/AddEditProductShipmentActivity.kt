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
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel

class AddEditProductShipmentActivity : BaseSimpleActivity(), HasComponent<AddEditProductShipmentComponent> {

    override fun getNewFragment(): Fragment {
        val shipmentInputModel:ShipmentInputModel? = intent.getParcelableExtra(PARAM_SHIPMENT_INPUT_MODEL)
        shipmentInputModel?.run {
            return AddEditProductShipmentFragment.createInstanceEditMode(shipmentInputModel)
        }
        return AddEditProductShipmentFragment.createInstance()
    }

    override fun getComponent(): AddEditProductShipmentComponent {
        return DaggerAddEditProductShipmentComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .addEditProductShipmentModule(AddEditProductShipmentModule())
                .build()
    }

    companion object {
        private const val PARAM_SHIPMENT_INPUT_MODEL = "param_shipment_input_model"
        fun createInstance(context: Context?) = Intent(context, AddEditProductShipmentActivity::class.java)
        fun createInstanceEditMode(context: Context?, shipmentInputModel: ShipmentInputModel): Intent =
                Intent(context, AddEditProductShipmentActivity::class.java)
                        .putExtra(PARAM_SHIPMENT_INPUT_MODEL, shipmentInputModel)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val f = fragment
        if (f!= null && f is AddEditProductShipmentFragment) {
            f.onBackPressed()
        }
    }
}
