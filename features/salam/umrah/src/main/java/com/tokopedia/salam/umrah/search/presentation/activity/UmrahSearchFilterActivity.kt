package com.tokopedia.salam.umrah.search.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.salam.umrah.common.di.UmrahComponentInstance
import com.tokopedia.salam.umrah.search.di.DaggerUmrahSearchComponent
import com.tokopedia.salam.umrah.search.di.UmrahSearchComponent
import com.tokopedia.salam.umrah.search.presentation.fragment.UmrahSearchFilterFragment
/**
 * @author by M on 24/10/19
 */
class UmrahSearchFilterActivity : BaseSimpleActivity(), HasComponent<UmrahSearchComponent> {
    override fun getComponent(): UmrahSearchComponent = DaggerUmrahSearchComponent.builder()
            .umrahComponent(UmrahComponentInstance.getUmrahComponent(application))
            .build()

    override fun getNewFragment(): Fragment? = UmrahSearchFilterFragment().getInstance()

    companion object {
        fun createIntent(context: Context): Intent =
                Intent(context, UmrahSearchFilterActivity::class.java)
    }
}