package com.tokopedia.product.addedit.description.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel
import com.tokopedia.product.addedit.description.presentation.model.ProductVariantInputModel

class AddEditProductDescriptionActivity : BaseSimpleActivity() {
    private var categoryId: String = ""
    private var isEditMode: Boolean = false
    private var descriptionInputModel: DescriptionInputModel = DescriptionInputModel()
    private var variantInputModel: ProductVariantInputModel = ProductVariantInputModel()

    override fun getNewFragment(): Fragment {
        intent?.apply {
            categoryId = getStringExtra(PARAM_CATEGORY_ID)
            descriptionInputModel = getParcelableExtra(PARAM_DESCRIPTION_INPUT_MODEL) ?: DescriptionInputModel()
            variantInputModel = getParcelableExtra(PARAM_VARIANT_INPUT_MODEL) ?: ProductVariantInputModel()
            isEditMode = getBooleanExtra(PARAM_IS_EDIT_MODE, false)
        }

        return if (isEditMode) {
            AddEditProductDescriptionFragment.createInstance(
                    categoryId,
                    descriptionInputModel,
                    variantInputModel,
                    isEditMode)
        } else {
            AddEditProductDescriptionFragment.createInstance(categoryId)
        }
    }

    companion object {
        const val REQUEST_CODE_DESCRIPTION = 0x03
        const val PARAM_DESCRIPTION_INPUT_MODEL = "param_description_input_model"
        const val PARAM_VARIANT_INPUT_MODEL = "param_variant_input_model"
        const val PARAM_CATEGORY_ID = "param_category_id"
        const val PARAM_IS_EDIT_MODE = "is_edit_mode"
        fun createInstance(context: Context?, categoryId: String): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_CATEGORY_ID, categoryId)
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
        super.onBackPressed()
        val f = fragment
        if (f!= null && f is AddEditProductDescriptionFragment) {
            f.onBackPressed()
        }
    }
}