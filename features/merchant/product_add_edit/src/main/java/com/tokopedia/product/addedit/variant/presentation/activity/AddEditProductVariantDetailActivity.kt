package com.tokopedia.product.addedit.variant.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantModule
import com.tokopedia.product.addedit.variant.di.DaggerAddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.fragment.AddEditProductVariantDetailFragment

class AddEditProductVariantDetailActivity: BaseSimpleActivity(), HasComponent<AddEditProductVariantComponent> {

    companion object {
        fun createInstance(context: Context?, cacheManagerId: String?, isEdit: Boolean): Intent =
                Intent(context, AddEditProductVariantDetailActivity::class.java).apply {
                    putExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                    putExtra(AddEditProductConstants.EXTRA_IS_EDIT_MODE, isEdit)
                }
    }

    override fun getLayoutRes() = com.tokopedia.product.addedit.R.layout.activity_add_edit_product_variant_detail

    override fun getParentViewResourceID(): Int = com.tokopedia.product.addedit.R.id.parent_view

    override fun getNewFragment(): Fragment {
        val cacheManagerId = intent?.getStringExtra(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID).orEmpty()
        val isEditMode = intent?.getBooleanExtra(AddEditProductConstants.EXTRA_IS_EDIT_MODE, false) ?: false
        return AddEditProductVariantDetailFragment.createInstance(cacheManagerId)
    }

    override fun getComponent(): AddEditProductVariantComponent {
        return DaggerAddEditProductVariantComponent
                .builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
                .addEditProductVariantModule(AddEditProductVariantModule())
                .build()
    }
}
