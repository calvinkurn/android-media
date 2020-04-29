package com.tokopedia.product.addedit.shipment.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentModule
import com.tokopedia.product.addedit.shipment.di.DaggerAddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel

class AddEditProductShipmentActivity : BaseSimpleActivity(), HasComponent<AddEditProductShipmentComponent> {

    companion object {
        private const val PARAM_SHIPMENT_INPUT_MODEL = "param_shipment_input_model"
        private const val PARAM_IS_ADD_MODE = "param_shipment_is_add_mode"
        fun createInstance(context: Context?) = Intent(context, AddEditProductShipmentActivity::class.java)
        fun createInstanceEditMode(context: Context?, shipmentInputModel: ShipmentInputModel, isAddMode: Boolean): Intent =
                Intent(context, AddEditProductShipmentActivity::class.java)
                        .putExtra(PARAM_SHIPMENT_INPUT_MODEL, shipmentInputModel)
                        .putExtra(PARAM_IS_ADD_MODE, isAddMode)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }
    }

    override fun getNewFragment(): Fragment {
        val shipmentInputModel:ShipmentInputModel? = intent.getParcelableExtra(PARAM_SHIPMENT_INPUT_MODEL)
        val productInputModel: ProductInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL) ?: ProductInputModel()
        val isAddMode: Boolean = intent.getBooleanExtra(PARAM_IS_ADD_MODE, false)
        shipmentInputModel?.run {
            return AddEditProductShipmentFragment.createInstanceEditMode(this, isAddMode)
        }
        return AddEditProductShipmentFragment.createInstance(productInputModel)
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
