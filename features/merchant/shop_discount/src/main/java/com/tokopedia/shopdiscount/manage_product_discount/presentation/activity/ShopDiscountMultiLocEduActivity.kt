package com.tokopedia.shopdiscount.manage_product_discount.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.manage_product_discount.presentation.fragment.ShopDiscountMultiLocEduFragment

class ShopDiscountMultiLocEduActivity : BaseSimpleActivity() {

    companion object {
        const val MODE_PARAM = "MODE_PARAM"
    }

    private var mode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        getIntentData()
        super.onCreate(savedInstanceState)
        window?.decorView?.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_manage_product_discount_multi_loc
    }

    override fun getNewFragment(): Fragment = ShopDiscountMultiLocEduFragment.createInstance(mode)

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    private fun getIntentData() {
        mode = intent.extras?.getString(ShopDiscountManageProductActivity.MODE_PARAM).orEmpty()
    }

}