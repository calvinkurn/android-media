package com.tokopedia.deals.home.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.deals.common.ui.activity.DealsBaseActivity
import com.tokopedia.deals.home.di.DaggerDealsHomeComponent
import com.tokopedia.deals.home.di.DealsHomeComponent
import com.tokopedia.deals.home.ui.fragment.DealsHomeFragment

/**
 * @author by jessica on 16/06/20
 */

class DealsHomeActivity : DealsBaseActivity(), HasComponent<DealsHomeComponent> {

    override fun getNewFragment(): Fragment = DealsHomeFragment.getInstance()

    override fun isSearchAble(): Boolean = false

    override fun getComponent(): DealsHomeComponent {
        return DaggerDealsHomeComponent.builder()
                .dealsComponent(getDealsComponent())
                .build()
    }

    override fun isHomePage(): Boolean = true

    companion object {
        fun getCallingIntent(context: Context): Intent = Intent(context, DealsHomeActivity::class.java)
    }
}