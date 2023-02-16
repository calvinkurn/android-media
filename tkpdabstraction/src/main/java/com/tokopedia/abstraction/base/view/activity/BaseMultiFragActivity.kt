package com.tokopedia.abstraction.base.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode

/**
 * Activity that enable navigation to multi fragments
 */
abstract class BaseMultiFragActivity : BaseToolbarActivity() {

    /**
     * This function is used in case we use flag SINGLE_TOP to activity
     */
    final override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent?.data
        if (uri == null) {
            navigateToNewFragment(getRootFragment())
        } else {
            goToFragmentBasedUri(uri)
        }
    }

    final override fun setupFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            inflateFragment()
        }
    }

    private fun inflateFragment() {
        val uri = intent?.data
        if (uri == null) {
            goToRootFragment()
        } else {
            goToFragmentBasedUri(uri)
        }
    }

    private fun goToFragmentBasedUri(uri: Uri) {
        var f = mapUriToFragment(uri)
        //fallback if uri is not supported.
        if (f == null) {
            f = getRootFragment()
        }
        navigateToNewFragment(f)
    }

    private fun goToRootFragment() {
        navigateToNewFragment(getRootFragment())
    }

    protected abstract fun getRootFragment(): Fragment

    /**
     * should return null if uri is not supported in this activity
     */
    protected abstract fun mapUriToFragment(uri: Uri): Fragment?

    protected fun getFragmentCount(): Int {
        return supportFragmentManager.fragments.count()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_multifrag
    }

    /**
     * Navigate to a fragment in activity which can mimic the launch mode behaviour of activities.
     * It is either launching new fragment with single task or single top behaviour.
     * If it is single task, it will replace the fragment with same name in the backstack with a new one
     *
     * @param   fragment            fragment that we want to navigate into
     * @param   isFinishCurrent     flag to determine whether we should finish the current fragment before navigating to the new one
     */
    open fun navigateToNewFragment(fragment: Fragment, isFinishCurrent: Boolean = false) {
        val isSingleTask = fragment.getFragmentLaunchMode() == BaseMultiFragmentLaunchMode.SINGLE_TASK
        val fragmentName = fragment.javaClass.name

        if (getIsNavigatingToSameFragment(fragmentName)) return

        val existingFragment = supportFragmentManager.findFragmentByTag(fragmentName)

        when {
            isSingleTask && existingFragment != null -> {
                popExistedFragment(fragmentName)
                // It means that we are trying to add new fragment but currently the same instance of that fragment resides as initial fragment in this activity
                // Later, this should not add the fragment transaction into backstack, because there will be double fragments in the stack
                val isNavigatingToInitialWithNewFragment = supportFragmentManager.backStackEntryCount < NAVIGATE_TO_INITIAL_FRAGMENT_COUNT
                addNewFragment(fragment, true, isNavigatingToInitialWithNewFragment, isFinishCurrent)
            }
            existingFragment == null -> {
                // Add new fragment if the same fragment is not found in the back stack
                addNewFragment(fragment, isFinishCurrent = isFinishCurrent)
            }
            else -> {
                // Move into existing same fragment in the back stack
                moveToExistedFragment(fragmentName)
            }
        }
    }

    /**
     * Replace current fragment and add to back stack
     *
     * @param   destinationFragment                     fragment that we want to navigate into
     * @param   leftToRightAnim                         flag to determine whether we should add slide animation from LTR or RTL
     * @param   isNavigatingToInitialWithNewFragment    flag to determine whether we are navigating to initial fragment with a new instance
     * @param   isFinishCurrent                         flag to determine whether we should finish the current fragment before navigating to the new one
     */
    private fun addNewFragment(destinationFragment: Fragment,
                               leftToRightAnim: Boolean = false,
                               isNavigatingToInitialWithNewFragment: Boolean = false,
                               isFinishCurrent: Boolean = false
    ) {
        val destinationFragmentName = destinationFragment.javaClass.name
        val fragmentCount = getFragmentCount()
        val ft = supportFragmentManager.beginTransaction()
        if (fragmentCount > 0) {
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
        if (isFinishCurrent) {
            if (getShouldPopBackStackImmediately()) {
                supportFragmentManager.popBackStackImmediate()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
        ft.add(
            R.id.frame_content,
            destinationFragment, destinationFragmentName
        )
        val shouldAddBackStack =
            fragmentCount > 0 && !isNavigatingToInitialWithNewFragment
        if (shouldAddBackStack) {
            val fCount = getFragmentCount()
            if (fCount > 0) {
                var i = fCount - 1
                var prevFragment: Fragment?
                var hasChecked = false
                while (i >= 0) {
                    prevFragment = supportFragmentManager.fragments.getOrNull(i)
                    if (prevFragment?.isHidden == false && prevFragment.isAdded) {
                        val shouldPopBackStackImmediately = getShouldPopBackStackImmediately()
                        if (isFinishCurrent && !hasChecked) {
                            hasChecked = true
                            if (!shouldPopBackStackImmediately) {
                                ft.hide(prevFragment)
                            }
                        } else {
                            try {
                                ft.setMaxLifecycle(prevFragment, Lifecycle.State.STARTED)
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                        }
                        if (shouldPopBackStackImmediately) {
                            ft.hide(prevFragment)
                        }
                    }
                    i--
                }
                ft.addToBackStack(destinationFragmentName)
            }
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
            if (backStackCount <= 1) {
                // Means that the fragment is existed but not yet added into the back stack.
                supportFragmentManager.popBackStack()
            } else {
                val destinationFragmentIndex = getDestinationFragmentIndex(backStackCount, destinationFragmentName)
                popBackStackUntilDestination(backStackCount, false, destinationFragmentIndex, destinationFragmentName)
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
            val backStackCount = supportFragmentManager.backStackEntryCount
            if (backStackCount <= 1) {
                // Means that the fragment is existed but not yet added into the back stack.
                supportFragmentManager.popBackStack()
            } else {
                val destinationFragmentIndex = getDestinationFragmentIndex(backStackCount, destinationFragmentName)
                popBackStackUntilDestination(backStackCount, true, destinationFragmentIndex, destinationFragmentName)
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
        for (i in 0 until backStackCount) {
            val existedFragmentName = supportFragmentManager.getBackStackEntryAt(i).name
            if (destinationFragmentName == existedFragmentName) {
                destinationFragmentIndex = i + 1
                break
            }
        }
        return destinationFragmentIndex
    }

    /**
     * Pop the back stack until it reaches the destination fragment
     *
     * @param   backStackCount              count of fragments back stack from the fragment manager
     * @param   shouldPopSimilarFragment    flag of whether we should pop same fragment in stack
     * @param   destinationFragmentIndex    index of destination fragment in the back stack
     * @param   destinationFragmentName     the name of the destination fragment
     */
    private fun popBackStackUntilDestination(backStackCount: Int,
                                             shouldPopSimilarFragment: Boolean,
                                             destinationFragmentIndex: Int,
                                             destinationFragmentName: String) {
        if (destinationFragmentIndex > RecyclerView.NO_POSITION) {
            val extraCount =
                if (shouldPopSimilarFragment) {
                    1
                } else {
                    0
                }
            val popStackCount = backStackCount - destinationFragmentIndex + extraCount
            if (popStackCount > 0) {
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

    private fun getIsNavigatingToSameFragment(destinationFragmentName: String): Boolean {
        val currentFragmentName = supportFragmentManager.fragments.lastOrNull()?.javaClass?.name
        return currentFragmentName == destinationFragmentName
    }

    private fun getShouldPopBackStackImmediately(): Boolean {
        return true
    }

    private fun Fragment.getFragmentLaunchMode(): BaseMultiFragmentLaunchMode {
        return (this as? BaseMultiFragment)?.getLaunchMode()
            ?: BaseMultiFragmentLaunchMode.SINGLE_TOP
    }

    companion object {
        private const val NAVIGATE_TO_INITIAL_FRAGMENT_COUNT = 2
    }

}
