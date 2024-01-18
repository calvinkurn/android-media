package com.tokopedia.topads.auto.view.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.di.module.AutoAdsQueryModule
import com.tokopedia.topads.auto.view.fragment.CreateAutoAdsFragment
import com.tokopedia.topads.auto.view.fragment.CreateAutoPsAdsFragment
import com.tokopedia.topads.auto.view.fragment.EditAutoAdsBudgetFragment
import com.tokopedia.topads.auto.view.viewmodel.AutoPsViewModel
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.fragment.LoaderFragment
import javax.inject.Inject

/**
 * Author errysuprayogi on 09,May,2019
 */
class EditBudgetAutoAdsActivity : BaseSimpleActivity(), HasComponent<AutoAdsComponent> {

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: AutoPsViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            )[AutoPsViewModel::class.java]
        }
    }

    override fun getNewFragment(): Fragment? {
        return LoaderFragment.newInstance()
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getComponent(): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).autoAdsQueryModule(AutoAdsQueryModule(this)).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 0f
        }
        viewModel?.getVariantById()
        setObservers()
    }

    private fun setObservers(){
        viewModel?.shopVariant?.observe(this){ shopVariants ->
            if(shopVariants.isNotEmpty() && shopVariants.filter { it.experiment == TopAdsCommonConstant.AUTOPS_EXPERIMENT && it.variant == TopAdsCommonConstant.AUTOPS_VARIANT }.isNotEmpty()){
                showAutoPs()
            } else {
                showAutoAd()
            }
        }
    }

    private fun showAutoAd(){
        updateTitle(getString(R.string.autoads_pengaturan_iklan))
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, EditAutoAdsBudgetFragment.newInstance(), tagFragment)
            .commit()
    }

    private fun showAutoPs(){
        updateTitle(getString(R.string.auto_ads_ps_creation))
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, CreateAutoPsAdsFragment.newInstance(), tagFragment)
            .commit()
    }
}
