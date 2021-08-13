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
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.EXTRA_IS_EDIT_MODE
import com.tokopedia.product.addedit.tracking.ProductCategoryTracking
import com.tokopedia.user.session.UserSession
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
        setupUi()
    }

    override fun getComponent(): AddEditProductCategoryComponent {
        return DaggerAddEditProductCategoryComponent.builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
                .addEditProductCategoryModule(AddEditProductCategoryModule())
                .build()
    }

    override fun onBackPressed() {
        if (intent.getBooleanExtra(EXTRA_IS_EDIT_MODE, false)) {
            ProductCategoryTracking.clickBackOtherCategory(UserSession(this).shopId)
        }
        finish()
    }

    private fun setupUi() {
        window?.decorView?.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        huCategory.title = getString(R.string.label_title_category_picker)
        setSupportActionBar(huCategory)
    }

}