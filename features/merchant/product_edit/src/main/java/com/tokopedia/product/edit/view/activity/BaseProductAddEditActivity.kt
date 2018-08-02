package com.tokopedia.product.edit.view.activity

import android.content.DialogInterface
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import com.tkpd.library.ui.utilities.TkpdProgressDialog
import com.tkpd.library.utils.CommonUtils
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.core.analytics.AppEventTracking
import com.tokopedia.core.analytics.UnifyTracking
import com.tokopedia.core.app.TkpdCoreRouter
import com.tokopedia.core.router.productdetail.ProductDetailRouter
import com.tokopedia.product.edit.R
import com.tokopedia.product.edit.common.di.component.ProductComponent
import com.tokopedia.product.edit.util.ProductEditModuleRouter

abstract class BaseProductAddEditActivity : BaseSimpleActivity(), HasComponent<ProductComponent>{


    override fun getComponent(): ProductComponent {
        return (getApplication() as ProductEditModuleRouter).getProductComponent()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_product_edit_with_menu
    }
}
