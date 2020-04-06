package com.tokopedia.product.addedit.shipment.presentation.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductUploadConstant
import com.tokopedia.product.addedit.detail.presentation.fragment.AddEditProductDetailFragment
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.di.AddEditProductShipmentModule
import com.tokopedia.product.addedit.shipment.di.DaggerAddEditProductShipmentComponent
import com.tokopedia.product.addedit.shipment.presentation.fragment.AddEditProductShipmentFragment
import com.tokopedia.product.addedit.shipment.presentation.model.ShipmentInputModel

class AddEditProductShipmentActivity : BaseSimpleActivity(), HasComponent<AddEditProductShipmentComponent> {

    override fun getNewFragment(): Fragment {
        val shipmentInputModel:ShipmentInputModel? = intent.getParcelableExtra(PARAM_SHIPMENT_INPUT_MODEL)
        val productInputModel: ProductInputModel = intent.getParcelableExtra(AddEditProductUploadConstant.EXTRA_PRODUCT_INPUT)
        shipmentInputModel?.run {
            return AddEditProductShipmentFragment.createInstanceEditMode(shipmentInputModel)
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

    companion object {
        private const val PARAM_SHIPMENT_INPUT_MODEL = "param_shipment_input_model"
        fun createInstance(context: Context?) = Intent(context, AddEditProductShipmentActivity::class.java)
        fun createInstanceEditMode(context: Context?, shipmentInputModel: ShipmentInputModel): Intent =
                Intent(context, AddEditProductShipmentActivity::class.java)
                        .putExtra(PARAM_SHIPMENT_INPUT_MODEL, shipmentInputModel)

    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        val dialogBuilder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
                .setMessage(R.string.message_alert_dialog_before_exit)
                .setNegativeButton(R.string.label_negative_button_on_alert_dialog) { _, _ -> }
                .setPositiveButton(R.string.label_positive_button_on_alert_dialog) { _, _ ->
                    super.onBackPressed()
                }
                .setNeutralButton(R.string.label_neutral_button_on_alert_dialog) { _, _ ->
                    saveProductToDraft()
                    moveToManageProduct()
                }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun moveToManageProduct() {
        val intentHome = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intentHome)
        finish()
    }

    private fun saveProductToDraft() {
        val f = fragment
        if (f != null && f is AddEditProductShipmentFragment) {
            f.insertProductDraft(false)
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductShipmentFragment) {
            f.onBackPressed()
        }
    }
}
