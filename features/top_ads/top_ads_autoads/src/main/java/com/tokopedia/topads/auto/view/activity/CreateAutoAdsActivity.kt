package com.tokopedia.topads.auto.view.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.di.module.AutoAdsQueryModule
import com.tokopedia.topads.auto.view.fragment.CreateAutoAdsFragment

class CreateAutoAdsActivity : BaseSimpleActivity(), HasComponent<AutoAdsComponent> {
    override fun getNewFragment(): Fragment {
        return CreateAutoAdsFragment.newInstance()
    }

    override fun getComponent(): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).autoAdsQueryModule(AutoAdsQueryModule(this)).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
    }
}