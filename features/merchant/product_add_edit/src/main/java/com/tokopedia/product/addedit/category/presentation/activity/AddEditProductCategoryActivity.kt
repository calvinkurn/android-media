package com.tokopedia.product.addedit.category.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.category.common.Constant.CATEGORY_ID_INIT_SELECTED
import com.tokopedia.product.addedit.category.common.Constant.INIT_UNSELECTED
import com.tokopedia.product.addedit.category.di.AddEditProductCategoryComponent
import com.tokopedia.product.addedit.category.di.AddEditProductCategoryModule
import com.tokopedia.product.addedit.category.di.DaggerAddEditProductCategoryComponent
import com.tokopedia.product.addedit.category.presentation.fragment.AddEditProductCategoryFragment
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import kotlinx.android.synthetic.main.activity_add_edit_product_category.*

class AddEditProductCategoryActivity : BaseSimpleActivity(), HasComponent<AddEditProductCategoryComponent>{

    private val addEditProductCategoryFragment: AddEditProductCategoryFragment by lazy {
        val uri = intent.data
        val selectedCategoryId = if (uri != null) {
            val segments = uri.pathSegments
            segments[segments.size - 1].toLong()
        } else {
            intent.getLongExtra(CATEGORY_ID_INIT_SELECTED, INIT_UNSELECTED.toLong())
        }
        AddEditProductCategoryFragment.newInstance(selectedCategoryId)
    }

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment = addEditProductCategoryFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product_category)
        huCategory.title = getString(R.string.label_title_category_picker)
        setSupportActionBar(huCategory)
    }

    override fun getComponent(): AddEditProductCategoryComponent {
        return DaggerAddEditProductCategoryComponent.builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
                .addEditProductCategoryModule(AddEditProductCategoryModule())
                .build()
    }

    override fun onBackPressed() {
        if (fragment is AddEditProductCategoryFragment) {
            val f = fragment as AddEditProductCategoryFragment
            //
        }
    }

}