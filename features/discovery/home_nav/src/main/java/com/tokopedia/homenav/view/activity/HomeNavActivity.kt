package com.tokopedia.homenav.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.tokopedia.homenav.R
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.setStatusBarColor


class HomeNavActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setAnimation()
        overridePendingTransition(R.anim.slide_top, R.anim.nav_fade_out)
        setContentView(R.layout.activity_main_nav)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(Color.WHITE)
        }

        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
            it.navigationIcon = getResDrawable(R.drawable.ic_close_x_black)
        }
        setupNavigation()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nav_fade_in, R.anim.slide_bottom)
    }

    private fun setAnimation() {
        if (Build.VERSION.SDK_INT > 20) {
//            val slide = Slide().apply {
//                slideEdge = Gravity.LEFT
//                duration = 200
//                interpolator = FastOutSlowInInterpolator()
//                addListener(object : Transition.TransitionListener{
//                    override fun onTransitionStart(p0: Transition?) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            setStatusBarColor(Color.TRANSPARENT)
//                        }
//                    }
//
//                    override fun onTransitionEnd(p0: Transition?) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            setStatusBarColor(Color.WHITE)
//                        }
//                    }
//
//                    override fun onTransitionCancel(p0: Transition?) {
//                    }
//
//                    override fun onTransitionPause(p0: Transition?) {
//                    }
//
//                    override fun onTransitionResume(p0: Transition?) {
//                    }
//                })
//            }
//            window.exitTransition = slide
//            window.enterTransition = slide

        }
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.fragment_container)
        val listener = AppBarConfiguration.OnNavigateUpListener {
            navController.navigateUp()
        }

        val appBarConfiguration = AppBarConfiguration.Builder().setFallbackOnNavigateUpListener(listener).build()
        navController.setGraph(R.navigation.nav_graph, Bundle())
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }
}