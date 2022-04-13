package com.tokopedia.tokofood.common.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.tokofood.common.di.DaggerTokoFoodComponent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.home.presentation.TokoFoodHomeFragment
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseFragment
import javax.inject.Inject

class BaseTokofoodActivity : BaseMultiFragActivity(), HasViewModel<MultipleFragmentsViewModel> {

    @Inject
    lateinit var viewModel: MultipleFragmentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        viewModel.onRestoreSavednstanceState()
    }

    override fun getRootFragment(): Fragment {
        return TokoFoodPurchaseFragment.createInstance()
    }

    override fun mapUriToFragment(uri: Uri): Fragment? {
        return TokofoodRouteManager.mapUriToFragment(uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }

    override fun viewModel(): MultipleFragmentsViewModel = viewModel

    private fun initInjector() {
        DaggerTokoFoodComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}