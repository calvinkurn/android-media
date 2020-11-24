package com.tokopedia.homenav.view.activity

import android.content.res.TypedArray
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.tokopedia.applink.internal.ApplinkConsInternalNavigation
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.fragment.MainNavFragmentArgs
import com.tokopedia.searchbar.navigation_component.NavToolbar
import kotlinx.android.synthetic.main.activity_main_nav.*


class HomeNavActivity: AppCompatActivity() {

    private var pageSource: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.slide_top, R.anim.nav_fade_out)
        setContentView(R.layout.activity_main_nav)
        pageSource = intent.getStringExtra(ApplinkConsInternalNavigation.PARAM_PAGE_SOURCE)?:""
        findViewById<NavToolbar>(R.id.toolbar)?.let {
            it.setToolbarTitle(getString(R.string.title_main_nav))
            it.setupToolbarWithStatusBar(this, NavToolbar.Companion.StatusBar.STATUS_BAR_LIGHT, true)
            it.setShowShadowEnabled(true)
        }
        setupNavigation()
        setupView()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_fade_in, R.anim.slide_bottom)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment_container)
        toolbar.setOnBackButtonClickListener {
            navController.navigateUp()
        }
        navController.setGraph(R.navigation.nav_graph,
                MainNavFragmentArgs(StringMainNavArgsSourceKey = pageSource).toBundle())
    }

    private fun setupView() {
        try {
            val styledAttributes: TypedArray = getTheme().obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            val mActionBarSize = styledAttributes.getDimension(0, 0f).toInt()
            styledAttributes.recycle()

            val layoutParams = fragment_container.view?.layoutParams as FrameLayout.LayoutParams
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    resources.getDimensionPixelOffset(R.dimen.dp_16) + mActionBarSize,
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin
            )
        } catch (e: Exception) {
            val layoutParams = fragment_container.view?.layoutParams as FrameLayout.LayoutParams
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    resources.getDimensionPixelOffset(R.dimen.dp_200),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.fragment_container).navigateUp()
                || super.onSupportNavigateUp();
    }
}