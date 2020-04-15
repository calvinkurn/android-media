package com.tokopedia.product.addedit.shipment.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentModule
import com.tokopedia.product.addedit.shipment.di.DaggerAddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel

class AddEditProductShipmentActivity : BaseSimpleActivity(), HasComponent<AddEditProductShipmentComponent> {

    override fun getNewFragment(): Fragment {
        val shipmentInputModel:ShipmentInputModel? = intent.getParcelableExtra(PARAM_SHIPMENT_INPUT_MODEL)
        val productInputModel: ProductInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT_MODEL) ?: ProductInputModel()
        val isEditing: Boolean = intent.getBooleanExtra(PARAM_IS_EDITING, false)
        val isDuplicating: Boolean = intent.getBooleanExtra(PARAM_IS_DUPLICATING, false)
        return AddEditProductShipmentFragment.createInstance(productInputModel, shipmentInputModel, isEditing, isDuplicating)
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
        private const val PARAM_IS_EDITING = "param_is_editing"
        private const val PARAM_IS_DUPLICATING = "param_is_duplicating"
        fun createInstance(context: Context?) = Intent(context, AddEditProductShipmentActivity::class.java)
        fun createInstance(context: Context?, shipmentInputModel: ShipmentInputModel, isEditing: Boolean, isDuplicating: Boolean): Intent =
                Intent(context, AddEditProductShipmentActivity::class.java)
                        .putExtra(PARAM_SHIPMENT_INPUT_MODEL, shipmentInputModel)
                        .putExtra(PARAM_IS_EDITING, isEditing)
                        .putExtra(PARAM_IS_DUPLICATING, isDuplicating)

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
