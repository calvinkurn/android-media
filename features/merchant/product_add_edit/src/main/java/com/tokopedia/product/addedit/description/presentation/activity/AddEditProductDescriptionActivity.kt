package com.tokopedia.product.addedit.description.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.product.addedit.description.presentation.fragment.AddEditProductDescriptionFragment
import com.tokopedia.product.addedit.description.presentation.model.DescriptionInputModel

class AddEditProductDescriptionActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment {
        val categoryId = intent?.getStringExtra(PARAM_CATEGORY_ID) ?: ""
        return AddEditProductDescriptionFragment.createInstance(categoryId)
    }

    companion object {
        const val REQUEST_CODE_DESCRIPTION = 0x03
        private const val PARAM_DESCRIPTION_INPUT_MODEL = "param_description_input_model"
        private const val PARAM_CATEGORY_ID = "param_category_id"
        fun createInstance(context: Context?, categoryId: String): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_CATEGORY_ID, categoryId)
        fun createInstanceEditMode(context: Context?,
                                   categoryId: String,
                                   descriptionInputModel: DescriptionInputModel): Intent =
                Intent(context, AddEditProductDescriptionActivity::class.java)
                        .putExtra(PARAM_CATEGORY_ID, categoryId)
                        .putExtra(PARAM_DESCRIPTION_INPUT_MODEL, descriptionInputModel)
    }

}
