package com.tokopedia.checkout.view.feature.emptycart2

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.checkout.R

/**
 * Created by Irfan Khoirul on 2019-05-17.
 */

class EmptyCartFragment {

    private fun setupToolbar(view: View) {
        val appbar = view.findViewById<Toolbar>(R.id.toolbar)
        val statusBarBackground = view.findViewById<View>(R.id.status_bar_bg)
        statusBarBackground.layoutParams.height = DisplayMetricUtils.getStatusBarHeight(getActivity()!!)
        if (isToolbarWithBackButton) {
            toolbar = toolbarRemoveWithBackView()
            statusBarBackground.visibility = View.GONE
        } else {
            toolbar = toolbarRemoveView()
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBarBackground.visibility = View.INVISIBLE
            } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                statusBarBackground.visibility = View.VISIBLE
            } else {
                statusBarBackground.visibility = View.GONE
            }
        }
        appbar.addView(toolbar)
        (getActivity() as AppCompatActivity).setSupportActionBar(appbar)
        setVisibilityRemoveButton(false)
    }

}