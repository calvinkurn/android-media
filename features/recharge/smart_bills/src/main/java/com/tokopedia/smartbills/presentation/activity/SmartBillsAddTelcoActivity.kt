package com.tokopedia.smartbills.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.di.DaggerSmartBillsComponent
import com.tokopedia.smartbills.di.SmartBillsComponent
import com.tokopedia.smartbills.presentation.fragment.SmartBillsAddTelcoFragment
import com.tokopedia.smartbills.util.RechargeSmartBillsMapper.parseQuery

/**
 * applink
 * tokopedia://digital/form?category_id=9&menu_id=3&template=telcopost&is_add_sbm=true
 * or
 * tokopedia://digital/form?category_id=1&menu_id=2&template=telcopre&is_add_sbm=true
 */

class SmartBillsAddTelcoActivity: BaseSimpleActivity(), HasComponent<SmartBillsComponent> {

    private var templateTelco : String = ""
    private var categoryID: String = ""
    private var menuID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(com.tokopedia.unifyprinciples.R.color.Unify_NN0)
        val uri = intent.dataString
        templateTelco = parseQuery(TEMPLATE, uri, intent.extras)
        categoryID = parseQuery(CATEGORY_ID, uri, intent.extras)
        menuID = parseQuery(MENU_ID, uri, intent.extras)
    }

    override fun getComponent(): SmartBillsComponent {
        return DaggerSmartBillsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

    override fun getNewFragment(): Fragment {
        return SmartBillsAddTelcoFragment.newInstance()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_smart_bills_add_telco
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getToolbarResourceID(): Int {
        return R.id.sbm_add_telco_toolbar
    }

    override fun onBackPressed() {
        (fragment as SmartBillsAddTelcoFragment).onBackPressed()
        super.onBackPressed()
    }

    fun getTemplateTelco(): String = templateTelco

    fun getCategoryId(): String = categoryID

    fun getMenuId(): String = menuID

    companion object{
        private const val TEMPLATE = "template"
        private const val CATEGORY_ID = "category_id"
        private const val MENU_ID = "menu_id"
    }
}