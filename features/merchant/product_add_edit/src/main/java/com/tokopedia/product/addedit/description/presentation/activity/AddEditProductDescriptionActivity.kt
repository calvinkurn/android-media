package com.tokopedia.product.addedit.description.presentation.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft

class AddEditProductDescriptionActivity : BaseSimpleActivity() {
    private var categoryId: String = ""
    private var isEditMode: Boolean = false
    private var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    private var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()
    private var productDraft: ProductDraft = ProductDraft()

    override fun getNewFragment(): Fragment {
        intent?.apply {
            categoryId = getStringExtra(PARAM_CATEGORY_ID)
            descriptionInputModel = getParcelableExtra(PARAM_DESCRIPTION_INPUT_MODEL) ?: DescriptionInputModel()
            variantInputModel = getParcelableExtra(PARAM_VARIANT_INPUT_MODEL) ?: ProductVariantInputModel()
            productDraft = getParcelableExtra(PARAM_PRODUCT_INPUT_MODEL) ?: ProductDraft()
            isEditMode = getBooleanExtra(PARAM_IS_EDIT_MODE, false)
        }

        return if (isEditMode) {
            AddEditProductDescriptionFragment.createInstance(
                    categoryId,
                    descriptionInputModel,
                    variantInputModel,
                    isEditMode)
        } else {
            AddEditProductDescriptionFragment.createInstance(categoryId, productDraft)
        }
    }

    companion object {
        const val REQUEST_CODE_DESCRIPTION = 0x03
        const val PARAM_DESCRIPTION_INPUT_MODEL = "param_description_input_model"
        const val PARAM_VARIANT_INPUT_MODEL = "param_variant_input_model"
        const val PARAM_PRODUCT_INPUT_MODEL = "param_product_input_model"
        const val PARAM_CATEGORY_ID = "param_category_id"
        const val PARAM_IS_EDIT_MODE = "is_edit_mode"
        fun createInstance(context: Context?, categoryId: String, productDraft: ProductDraft): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_CATEGORY_ID, categoryId)
                        .putExtra(PARAM_PRODUCT_INPUT_MODEL, productDraft)
        fun createInstanceEditMode(context: Context?,
                                   categoryId: String,
                                   descriptionInputModel: DescriptionInputModel,
                                   variantInputModel: ProductVariantInputModel): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_CATEGORY_ID, categoryId)
                        .putExtra(PARAM_DESCRIPTION_INPUT_MODEL, descriptionInputModel)
                        .putExtra(PARAM_VARIANT_INPUT_MODEL, variantInputModel)
                        .putExtra(PARAM_IS_EDIT_MODE, true)
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
        val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST)
        startActivity(intent)
        finish()
    }

    private fun saveProductToDraft() {
        val f = fragment
        if (f != null && f is AddEditProductDescriptionFragment) {
            f.saveProductDraft(false)
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductDescriptionFragment) {
            f.onBackPressed()
        }
    }
}
