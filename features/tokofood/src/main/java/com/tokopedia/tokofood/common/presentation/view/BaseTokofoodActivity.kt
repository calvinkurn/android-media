package com.tokopedia.tokofood.common.presentation.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokofood.common.di.DaggerTokoFoodComponent
import com.tokopedia.tokofood.common.presentation.listener.HasViewModel
import com.tokopedia.tokofood.common.presentation.viewmodel.MultipleFragmentsViewModel
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomePageLoadTimeMonitoring
import com.tokopedia.tokofood.feature.home.presentation.fragment.TokoFoodHomeFragment
import javax.inject.Inject

class BaseTokofoodActivity : BaseMultiFragActivity(), HasViewModel<MultipleFragmentsViewModel> {

    @Inject
    lateinit var viewModel: MultipleFragmentsViewModel

    var pageLoadTimeMonitoring: TokoFoodHomePageLoadTimeMonitoring? = null

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
        return TokofoodRouteManager.mapUriToFragment(uri)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }

    override fun viewModel(): MultipleFragmentsViewModel = viewModel

    override fun navigateToNewFragment(fragment: Fragment) {
        val fragmentName = fragment.javaClass.name
        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentName)
        if (existingFragment == null) {
            // Add new fragment if the same fragment is not found in the back stack
            addNewFragment(fragment)
        } else {
            // Move into existing same fragment in the back stack
            moveToExistedFragment(fragmentName)
        }
    }

    /**
     * Navigate to a fragment in Tokofood which can mimic the launch mode behaviour of activities.
     * It is either launching new fragment with single task or single top behaviour.
     * If it is single task, it will replace the fragment with same name in the backstack with a new one
     *
     * @param   fragment        fragment that we want to navigate into
     * @param   isSingleTask    flag to determine either the fragment should be launched with single task or single top
     */
    fun navigateToNewFragment(fragment: Fragment, isSingleTask: Boolean = false) {
        val fragmentName = fragment.javaClass.name
        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentName)
        if (isSingleTask && existingFragment != null) {
            popExistedFragment(fragmentName)
            addNewFragment(fragment, true)
        } else {
            navigateToNewFragment(fragment)
        }
    }

    /**
     * Replace current fragment and add to back stack
     *
     * @param   destinationFragment     fragment that we want to navigate into
     * @param   leftToRightAnim         flag to determine whether we should add slide animation from LTR or RTL
     */
    private fun addNewFragment(destinationFragment: Fragment,
                               leftToRightAnim: Boolean = false) {
        val destinationFragmentName = destinationFragment.javaClass.name
        val fragmentCount = getFragmentCount()
        val ft = supportFragmentManager.beginTransaction()
        if (fragmentCount > Int.ZERO) {
            if (leftToRightAnim) {
                ft.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            } else {
                ft.setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
        }
        ft.replace(
            R.id.frame_content,
            destinationFragment, destinationFragmentName
        )
        if (fragmentCount > Int.ZERO) {
            ft.addToBackStack(destinationFragmentName)
        }
        ft.commit()
    }

    /**
     * Move to existed fragment in the backstack if the class name found are same as destination's.
     * This needs to be checked because there should not be multiple fragments with same class in the backstack.
     *
     * For example: Merchant Page -> Purchase Page -> Merchant Page.
     * After the Purchase Page, we should pop the backstack instead of adding new merchant page fragment
     *
     * @param   destinationFragmentName the name of the destination fragment
     */
    private fun moveToExistedFragment(destinationFragmentName: String) {
        try {
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount <= Int.ONE) {
                // Means that the fragment is existed but not yet added into the back stack.
                supportFragmentManager.popBackStack()
            } else {
                val destinationFragmentIndex = getDestinationFragmentIndex(backStackCount, destinationFragmentName)
                popBackStackUntilDestination(backStackCount, destinationFragmentIndex, destinationFragmentName)
            }
        } catch (ex: Exception) {
            popDestinationBackStack(destinationFragmentName)
        }
    }

    /**
     * Pop existed fragment in the backstack.
     * This is used to change the existed fragment to new instance of fragment with the same class name
     *
     * @param   destinationFragmentName the name of the destination fragment
     */
    private fun popExistedFragment(destinationFragmentName: String) {
        try {
            var backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount <= Int.ONE) {
                // Means that the fragment is existed but not yet added into the back stack.
                supportFragmentManager.popBackStack()
            } else {
                backStackCount++
                val destinationFragmentIndex = getDestinationFragmentIndex(backStackCount, destinationFragmentName)
                popBackStackUntilDestination(backStackCount, destinationFragmentIndex, destinationFragmentName)
            }
        } catch (ex: Exception) {
            popDestinationBackStack(destinationFragmentName)
        }
    }

    /**
     * Get the index of the destination fragment in the back stack
     * Returns {@link androidx.recyclerview.widget.RecyclerView.NO_POSITION} if no destination fragment found in the back stack
     *
     * @param   backStackCount          count of fragments back stack from the fragment manager
     * @param   destinationFragmentName the name of the destination fragment
     * @return  destination fragment index
     *
     */
    private fun getDestinationFragmentIndex(backStackCount: Int,
                                            destinationFragmentName: String): Int {
        var destinationFragmentIndex = RecyclerView.NO_POSITION
        for (i in Int.ZERO until backStackCount) {
            supportFragmentManager.getBackStackEntryAt(i).name.let { existedFragmentName ->
                if (destinationFragmentName == existedFragmentName) {
                    destinationFragmentIndex = i
                }
            }
        }
        return destinationFragmentIndex
    }

    /**
     * Pop the back stack until it reaches the destination fragment
     *
     * @param   backStackCount              count of fragments back stack from the fragment manager
     * @param   destinationFragmentIndex    index of destination fragment in the back stack
     * @param   destinationFragmentName     the name of the destination fragment
     */
    private fun popBackStackUntilDestination(backStackCount: Int,
                                             destinationFragmentIndex: Int,
                                             destinationFragmentName: String) {
        if (destinationFragmentIndex > RecyclerView.NO_POSITION) {
            val popStackCount = backStackCount - destinationFragmentIndex - Int.ONE
            if (popStackCount > Int.ZERO) {
                repeat(popStackCount) {
                    supportFragmentManager.popBackStack()
                }
            } else {
                popDestinationBackStack(destinationFragmentName)
            }
        } else {
            popDestinationBackStack(destinationFragmentName)
        }
    }

    private fun popDestinationBackStack(fragmentName: String) {
        supportFragmentManager.popBackStack(fragmentName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    private fun initInjector() {
        DaggerTokoFoodComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun initPerformanceMonitoring() {
        if (intent.data != null && intent.data.toString().equals(ApplinkConstInternalTokoFood.HOME)){
            pageLoadTimeMonitoring = TokoFoodHomePageLoadTimeMonitoring()
            pageLoadTimeMonitoring?.initPerformanceMonitoring()
        }
    }
}