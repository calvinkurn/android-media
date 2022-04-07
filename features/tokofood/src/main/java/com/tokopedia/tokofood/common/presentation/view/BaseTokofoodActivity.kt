package com.tokopedia.tokofood.common.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.home.presentation.fragment.TokoFoodHomeFragment

class BaseTokofoodActivity : BaseMultiFragActivity(), HasViewModel<MultipleFragmentsViewModel> {

    val viewModel: MultipleFragmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onRestoreSavednstanceState()
    }

    override fun getRootFragment(): Fragment {
        return TokoFoodHomeFragment()
    }

    override fun mapUriToFragment(uri: Uri): Fragment? {
        return TokofoodRouteManager.mapUriToFragment(uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }

    override fun viewModel(): MultipleFragmentsViewModel = viewModel
}