package com.tokopedia.shop_widget.mvc_locked_to_product.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.mvc_locked_to_product.view.fragment.MvcLockedToProductFragment

class MvcLockedToProductPageActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN0))
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_mvc_locked_to_product
    }

    override fun getNewFragment(): Fragment = MvcLockedToProductFragment.createInstance()

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }
}