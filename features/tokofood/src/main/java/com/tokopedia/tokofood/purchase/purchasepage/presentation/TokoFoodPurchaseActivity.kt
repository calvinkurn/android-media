package com.tokopedia.tokofood.purchase.purchasepage.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.tokofood.purchase.purchasepage.presentation.di.DaggerTokoFoodPurchaseComponent
import com.tokopedia.tokofood.purchase.purchasepage.presentation.di.TokoFoodPurchaseComponent

class TokoFoodPurchaseActivity: BaseSimpleActivity(), HasComponent<TokoFoodPurchaseComponent> {

    lateinit var fragment: TokoFoodPurchaseFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.gone()
    }

    override fun getNewFragment(): Fragment {
        fragment = TokoFoodPurchaseFragment.createInstance()
        return fragment
    }

    override fun getLayoutRes(): Int {
        return com.tokopedia.abstraction.R.layout.activity_base_simple
    }

    override fun onBackPressed() {
        if (::fragment.isInitialized) {
            fragment.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }

    override fun getComponent(): TokoFoodPurchaseComponent {
        return DaggerTokoFoodPurchaseComponent
                .builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }
}