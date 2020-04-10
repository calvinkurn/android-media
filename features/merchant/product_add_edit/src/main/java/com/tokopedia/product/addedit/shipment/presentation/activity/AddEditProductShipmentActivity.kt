package com.tokopedia.product.addedit.shipment.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.product.addedit.R
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
        shipmentInputModel?.run {
            return AddEditProductShipmentFragment.createInstanceEditMode(this)
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
        DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
            setTitle(getString(R.string.label_title_on_dialog))
            setDescription(getString(R.string.label_description_on_dialog))
            setPrimaryCTAText(getString(R.string.label_cta_primary_button_on_dialog))
            setSecondaryCTAText(getString(R.string.label_cta_secondary_button_on_dialog))
            setSecondaryCTAClickListener {
                saveProductToDraft()
                moveToManageProduct()
                onCtaYesPressedHitTracking()
            }
            setPrimaryCTAClickListener {
                super.onBackPressed()
                onCtaNoPressedHitTracking()
            }
        }.show()
    }

    private fun moveToManageProduct() {
        val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
        finish()
    }

    private fun saveProductToDraft() {
        val f = fragment
        if (f != null && f is AddEditProductShipmentFragment) {
            f.saveProductDraft(false)
        }
    }

    private fun onCtaYesPressedHitTracking() {
        val f = fragment
        if (f != null && f is AddEditProductShipmentFragment) {
            f.onCtaYesPressed()
        }
    }

    private fun onCtaNoPressedHitTracking() {
        val f = fragment
        if (f != null && f is AddEditProductShipmentFragment) {
            f.onCtaNoPressed()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductShipmentFragment) {
            f.onBackPressed()
        }
    }
}
