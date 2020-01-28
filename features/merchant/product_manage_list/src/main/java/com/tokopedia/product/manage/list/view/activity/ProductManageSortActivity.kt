package com.tokopedia.product.manage.list.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.manage.item.common.di.component.ProductComponent
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.EXTRA_SORT_SELECTED
import com.tokopedia.product.manage.list.constant.option.SortProductOption
import com.tokopedia.product.manage.list.view.fragment.ProductManageSortFragment
import com.tokopedia.seller.ProductEditItemComponentInstance

class ProductManageSortActivity : BaseSimpleActivity(), HasComponent<ProductComponent> {

    companion object {
        fun createIntent(context: Context, @SortProductOption sortProductOption: String) = Intent(context, ProductManageSortActivity::class.java).apply {
            putExtra(EXTRA_SORT_SELECTED, sortProductOption)
        }
    }

    override fun getComponent(): ProductComponent {
        return ProductEditItemComponentInstance.getComponent(application)
    }

    override fun getNewFragment(): Fragment? = ProductManageSortFragment.createInstance(intent.getStringExtra(EXTRA_SORT_SELECTED))
    override fun isShowCloseButton(): Boolean = true
}