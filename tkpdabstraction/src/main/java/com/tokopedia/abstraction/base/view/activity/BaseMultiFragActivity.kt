package com.tokopedia.abstraction.base.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.R
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment

/**
 * Activity that enable navigation to multi fragments
 */
abstract class BaseMultiFragActivity : BaseToolbarActivity() {

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

    protected abstract fun getRootFragment(): BaseMultiFragment

    /**
     * should return null if uri is not supported in this activity
     */
    protected abstract fun mapUriToFragment(uri: Uri): BaseMultiFragment?

    fun navigateToNewFragment(fragment: Fragment) {
        val fragmentCount = getFragmentCount()
        val ft = supportFragmentManager.beginTransaction()
        if (fragmentCount > 0) {
            ft.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
        ft.replace(
            R.id.frame_content,
            fragment, fragmentCount.toString()
        )
        if (fragmentCount > 0) {
            ft.addToBackStack(fragmentCount.toString())
        }
        ft.commit()
    }

    private fun getFragmentCount(): Int {
        return supportFragmentManager.fragments.count()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_multifrag
    }

}