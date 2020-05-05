package com.tokopedia.product.addedit.description.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel

class AddEditProductDescriptionActivity : BaseSimpleActivity() {
    private var isEditMode: Boolean = false
    private var isAddMode: Boolean = false
    private var productInputModel: ProductInputModel = ProductInputModel()

    override fun getNewFragment(): Fragment {
        intent?.apply {
            productInputModel = getParcelableExtra(PARAM_PRODUCT_INPUT_MODEL) ?: ProductInputModel()
            isEditMode = getBooleanExtra(PARAM_IS_EDIT_MODE, false)
            isAddMode = getBooleanExtra(PARAM_IS_ADD_MODE, false)
        }

        return if (isEditMode) {
            AddEditProductDescriptionFragment.createInstance(
                    productInputModel,
                    isEditMode,
                    isAddMode)
        } else {
            AddEditProductDescriptionFragment.createInstance(productInputModel)
        }
    }

    companion object {
        const val PARAM_PRODUCT_INPUT_MODEL = "param_product_input_model"
        const val PARAM_IS_EDIT_MODE = "is_edit_mode"
        const val PARAM_IS_ADD_MODE = "is_add_mode"

        fun createInstance(context: Context?, productInputModel: ProductInputModel): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_PRODUCT_INPUT_MODEL, productInputModel)

        fun createInstanceEditMode(context: Context?,
                                   productInputModel: ProductInputModel,
                                   isAddMode: Boolean): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_PRODUCT_INPUT_MODEL, productInputModel)
                        .putExtra(PARAM_IS_EDIT_MODE, true)
                        .putExtra(PARAM_IS_ADD_MODE, isAddMode)
    }

    override fun onBackPressed() {
        onBackPressedHitTracking()
        sendDataBack()
    }

    private fun sendDataBack() {
        val f = fragment
        if (f != null && f is AddEditProductDescriptionFragment) {
            f.sendDataBack()
        }
    }

    private fun onBackPressedHitTracking() {
        val f = fragment
        if (f!= null && f is AddEditProductDescriptionFragment) {
            f.onBackPressed()
        }
    }
}