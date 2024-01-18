package com.tokopedia.topads.dashboard.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.fragment.LoaderFragment
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.AUTO_ADS_DISABLED
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAutoPsTypeSelectionFragment
import com.tokopedia.topads.dashboard.view.fragment.TopAdsTypeSelectionFragment
import com.tokopedia.topads.dashboard.viewmodel.TopAdsTypeSelectionViewModel
import javax.inject.Inject

class TopAdsTypeSelectionActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    @JvmField
    @Inject
    var viewModelFactory: ViewModelFactory? = null

    private val viewModel: TopAdsTypeSelectionViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        viewModelFactory?.let {
            ViewModelProvider(
                this,
                it
            )[TopAdsTypeSelectionViewModel::class.java]
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    override fun getNewFragment(): Fragment {
        return LoaderFragment.newInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTO_ADS_DISABLED) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun getComponent(): TopAdsDashboardComponent =
        DaggerTopAdsDashboardComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel?.getVariantById()
        setObservers()
    }


    private fun setObservers(){
        viewModel?.shopVariant?.observe(this){ shopVariants ->
            if(shopVariants.isNotEmpty() && shopVariants.filter { it.experiment == TopAdsCommonConstant.AUTOPS_EXPERIMENT && it.variant == TopAdsCommonConstant.AUTOPS_VARIANT }.isNotEmpty()){
                showAutoPsTypeSelection()
            } else {
                showTypeSelection()
            }
        }
    }

    private fun showTypeSelection(){
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, TopAdsTypeSelectionFragment.newInstance(), tagFragment)
            .commit()
    }

    private fun showAutoPsTypeSelection(){
        supportFragmentManager.beginTransaction()
            .replace(parentViewResourceID, TopAdsAutoPsTypeSelectionFragment.newInstance(), tagFragment)
            .commit()
    }
}
