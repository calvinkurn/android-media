package com.tokopedia.shop_showcase.shop_showcase_add.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ShopShowcaseEditParam
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.fragment.ShopShowcaseAddFragment

/**
 * @author by Rafli Syam on 2020-03-04
 *
 * ApplinkConsInternalMerchant.MERCHANT_SHOP_SHOWCASE_ADD
 */

class ShopShowcaseAddActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context, isActionEdit: Boolean, showcaseId: String, showcaseName: String): Intent {
            val intent = Intent(context, ShopShowcaseAddActivity::class.java)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT, isActionEdit)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID, showcaseId)
            intent.putExtra(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME, showcaseName)
            return intent
        }

        const val DEFAULT_SHOWCASE_ID = "0"

        val ACTIVITY_LAYOUT = R.layout.activity_shop_showcase_product_add
        val PARENT_VIEW_ACTIVITY = R.id.parent_view
    }

    private var isActionEdit: Boolean = false
    private var showcaseId: String? = DEFAULT_SHOWCASE_ID
    private var showcaseName: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBackgroundColor()
    }

    override fun getNewFragment(): Fragment? {
        intent?.extras?.let {
            isActionEdit = it.getBoolean(ShopShowcaseEditParam.EXTRA_IS_ACTION_EDIT)
            showcaseId = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_ID)
            showcaseName = it.getString(ShopShowcaseEditParam.EXTRA_SHOWCASE_NAME)
        }
        return ShopShowcaseAddFragment.createInstance(isActionEdit, showcaseId, showcaseName)
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ACTIVITY
    }

    override fun onBackPressed() {
        if(isActionEdit) {
            if(fragment != null) {
                (fragment as? ShopShowcaseAddFragment)?.onBackPressedConfirm()
            }
        } else {
            finish()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(currentFocus != null) {
            KeyboardHandler.hideSoftKeyboard(this)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setBackgroundColor() {
        window.decorView.setBackgroundColor(
                androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
    }

}