package com.tokopedia.tokofood.common.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.tokofood.common.di.DaggerTokoFoodComponent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomePageLoadTimeMonitoring
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import com.tokopedia.tokofood.feature.merchant.presentation.fragment.ManageLocationFragment
import javax.inject.Inject

open class BaseTokofoodActivity : BaseMultiFragActivity(), HasViewModel<MultipleFragmentsViewModel> {

    @Inject
    lateinit var viewModel: MultipleFragmentsViewModel

    var pageLoadTimeMonitoring: TokoFoodHomePageLoadTimeMonitoring? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPerformanceMonitoring()
        initInjector()
        viewModel.onRestoreSavedInstanceState()
    }

    override fun getRootFragment(): Fragment {
        return TokoFoodHomeFragment.createInstance()
    }

    override fun mapUriToFragment(uri: Uri): Fragment? {
        return TokofoodRouteManager.mapUriToFragment(this, uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }

    override fun viewModel(): MultipleFragmentsViewModel = viewModel

    /**
     * Finish the activity when click back from error state address merchant
     */
    @SuppressLint("DeprecatedMethod")
    override fun onBackPressed() {
        val existingFragment = this.supportFragmentManager.findFragmentByTag(ManageLocationFragment::class.java.name)
        if (existingFragment != null) {
            this.finish()
        }
        super.onBackPressed()
    }

    private fun initInjector() {
        DaggerTokoFoodComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initPerformanceMonitoring() {
        if (intent.data != null && intent.data.toString() == ApplinkConstInternalTokoFood.HOME){
            pageLoadTimeMonitoring = TokoFoodHomePageLoadTimeMonitoring()
            pageLoadTimeMonitoring?.initPerformanceMonitoring()
        }
    }

}
