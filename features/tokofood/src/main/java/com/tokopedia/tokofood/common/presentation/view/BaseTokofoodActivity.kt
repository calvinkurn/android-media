package com.tokopedia.tokofood.common.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.di.DaggerTokoFoodComponent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
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
        return TokoFoodHomeFragment.createInstance()
    }

    override fun mapUriToFragment(uri: Uri): Fragment? {
        return TokofoodRouteManager.mapUriToFragment(uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }

    override fun viewModel(): MultipleFragmentsViewModel = viewModel

    override fun navigateToNewFragment(fragment: Fragment) {
        val fragmentCount = getFragmentCount()
        val ft = supportFragmentManager.beginTransaction()
        if (fragmentCount > Int.ZERO) {
            ft.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        val fragmentName = fragment.javaClass.name
        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentName)
        if (existingFragment == null) {
            ft.replace(
                R.id.frame_content,
                fragment, fragmentName
            )
            if (fragmentCount > Int.ZERO) {
                ft.addToBackStack(fragmentName)
            }
            ft.commit()
        } else {
            supportFragmentManager.popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    private fun initInjector() {
        DaggerTokoFoodComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }
}