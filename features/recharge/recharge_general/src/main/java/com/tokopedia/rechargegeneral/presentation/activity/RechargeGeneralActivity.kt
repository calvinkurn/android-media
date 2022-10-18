package com.tokopedia.rechargegeneral.presentation.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.rechargegeneral.R
import com.tokopedia.rechargegeneral.di.DaggerRechargeGeneralComponent
import com.tokopedia.rechargegeneral.di.RechargeGeneralComponent
import com.tokopedia.rechargegeneral.presentation.fragment.RechargeGeneralFragment

/**
 * applink
 * tokopedia://digital/form?category_id=5&menu_id=120&template=general
 * or
 * @sample airPDAM = RouteManager.route(this, ApplinkConsInternalDigital.PRODUCT_TEMPLATE, "5", "120", "general")
 */

class RechargeGeneralActivity : BaseSimpleActivity(), HasComponent<RechargeGeneralComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID)?.toIntSafely().toZeroIfNull()
        val menuId = bundle?.getString(PARAM_MENU_ID)?.toIntSafely().toZeroIfNull()
        val operatorId = bundle?.getString(PARAM_OPERATOR_ID)?.toIntSafely().toZeroIfNull()
        val productId = bundle?.getString(PARAM_PRODUCT_ID)?.toIntSafely().toZeroIfNull()
        val isAddSBM = bundle?.getString(PARAM_ADD_BILLS)?.toBoolean() ?: false
        val isFromSBM = bundle?.getBoolean(EXTRA_ADD_BILLS_IS_FROM_SBM) ?: false
        val rechargeProductFromSlice = bundle?.getString(RECHARGE_PRODUCT_EXTRA,"") ?: ""
        return RechargeGeneralFragment.newInstance(categoryId, menuId, operatorId, productId, rechargeProductFromSlice, isAddSBM, isFromSBM)
    }

    override fun getComponent(): RechargeGeneralComponent {
        return DaggerRechargeGeneralComponent.builder()
                .commonTopupBillsComponent(CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application))
                .build()
    }

    override fun onBackPressed() {
        (fragment as? RechargeGeneralFragment)?.onBackPressed()
        super.onBackPressed()
    }

    override fun getLayoutRes(): Int = LAYOUT

    override fun getToolbarResourceID(): Int = TOOLBAR_ID

    override fun getParentViewResourceID(): Int = VIEW_PARENT_ID

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val focusedView: View? = currentFocus
            focusedView?.run {
                if (focusedView is EditText) {
                    val outRect = Rect()
                    focusedView.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                        KeyboardHandler.DropKeyboard(this@RechargeGeneralActivity, focusedView)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {

        @LayoutRes val LAYOUT = R.layout.view_recharge_general_toolbar
        @IdRes val TOOLBAR_ID = R.id.recharge_general_header
        @IdRes val VIEW_PARENT_ID = R.id.recharge_general_view_parent

        const val PARAM_CATEGORY_ID = "category_id"
        const val PARAM_MENU_ID = "menu_id"
        const val PARAM_OPERATOR_ID = "operator_id"
        const val PARAM_PRODUCT_ID = "product_id"
        const val PARAM_ADD_BILLS = "is_add_sbm"
        const val PARAM_CLIENT_NUMBER = "client_number"
        const val EXTRA_ADD_BILLS_IS_FROM_SBM = "IS_FROM_SBM"

        const val RECHARGE_PRODUCT_EXTRA = "RECHARGE_PRODUCT_EXTRA"

        fun newInstance(context: Context,
                        categoryId: Int,
                        menuId: Int,
                        operatorId: Int = 0,
                        productId: String = ""): Intent {
            val intent = Intent(context, RechargeGeneralActivity::class.java)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId.toString())
            intent.putExtra(PARAM_MENU_ID, menuId.toString())
            intent.putExtra(PARAM_OPERATOR_ID, operatorId.toString())
            intent.putExtra(PARAM_PRODUCT_ID, productId)
            return intent
        }
    }
}
