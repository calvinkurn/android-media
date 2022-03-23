package com.tokopedia.tokofood.base

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.tokofood.example.FragmentA
import com.tokopedia.tokofood.example.MultipleFragmentsViewModel
import com.tokopedia.tokofood.example.TokofoodRouteManager

class ExampleTokofoodActivity : BaseMultiFragActivity() {

    val viewModel: MultipleFragmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onRestoreSavednstanceState()
    }

    override fun getRootFragment(): BaseMultiFragment {
        return FragmentA()
    }

    override fun mapUriToFragment(uri: Uri): BaseMultiFragment? {
        return TokofoodRouteManager.mapUriToFragment(uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }
}