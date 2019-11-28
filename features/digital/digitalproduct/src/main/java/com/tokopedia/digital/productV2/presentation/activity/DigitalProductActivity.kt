package com.tokopedia.digital.productV2.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.digital.productV2.di.DigitalProductComponent
import com.tokopedia.digital.productV2.di.DigitalProductComponentInstance
import com.tokopedia.digital.productV2.presentation.fragment.DigitalProductFragment

class DigitalProductActivity : BaseSimpleActivity(), HasComponent<DigitalProductComponent> {

    private lateinit var digitalProductComponent: DigitalProductComponent

    override fun getNewFragment(): Fragment {
        val bundle = intent.extras
        val menuId = bundle?.getInt(PARAM_MENU_ID) ?: 0
        val categoryId = bundle?.getString(PARAM_CATEGORY_ID) ?: ""
        return DigitalProductFragment.newInstance(menuId, categoryId)
    }

    override fun getComponent(): DigitalProductComponent {
        if (!::digitalProductComponent.isInitialized) {
            digitalProductComponent = DigitalProductComponentInstance.getDigitalProductComponent(application)
        }
        return digitalProductComponent
    }

    companion object {

        val PARAM_MENU_ID = "menu_id"
        val PARAM_CATEGORY_ID = "category_id"

        fun newInstance(context: Context, menuId: Int, categoryId: String): Intent {
            val intent = Intent(context, DigitalProductActivity::class.java)
            intent.putExtra(PARAM_MENU_ID, menuId)
            intent.putExtra(PARAM_CATEGORY_ID, categoryId)
            return intent
        }
    }
}