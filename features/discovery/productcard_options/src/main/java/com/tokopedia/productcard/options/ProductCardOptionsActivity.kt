package com.tokopedia.productcard.options

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import kotlinx.android.synthetic.main.product_card_options_activity_layout.*


class ProductCardOptionsActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animateTransparentBackground()
    }

    private fun animateTransparentBackground() {
        val animation = loadAnimation(R.anim.product_card_options_fade_in)
        imageBackgroundTransparent?.startAnimation(animation)
    }

    private fun loadAnimation(@AnimRes animationResource: Int): Animation {
        return AnimationUtils.loadAnimation(this, animationResource)
    }

    // setupStatusBar overriden as empty to disable status bar
    override fun setupStatusBar() { }

    override fun getNewFragment(): Fragment {
        return ProductCardOptionsFragment()
    }

    override fun inflateFragment() {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.product_card_options_slide_up, R.anim.product_card_options_slide_down)
                .replace(R.id.parentView, newFragment, tagFragment)
                .commit()
    }

    override fun getLayoutRes(): Int {
        return R.layout.product_card_options_activity_layout
    }

    override fun onBackPressed() {
        fragment?.let { animateCloseBottomSheet(it) }
    }

    private fun animateCloseBottomSheet(fragment: Fragment) {
        val animation = loadAnimation(R.anim.product_card_options_slide_down)
        animation.setAnimationListener(createBottomSheetAnimationListener(fragment))

        fragment.view?.startAnimation(animation)
    }

    private fun createBottomSheetAnimationListener(fragment: Fragment): Animation.AnimationListener {
        return object: Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) { }

            override fun onAnimationStart(animation: Animation?) { }

            override fun onAnimationEnd(animation: Animation?) { onBottomSheetAnimationEnd(fragment) }
        }
    }

    private fun onBottomSheetAnimationEnd(fragment: Fragment) {
        removeFragment(fragment)
        finish()
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
    }

    override fun finish() {
        fragment?.let { cancelBottomSheetAnimation(it) }

        overridePendingTransition(0, 0)

        super.finish()
    }

    private fun cancelBottomSheetAnimation(fragment: Fragment) {
        fragment.view?.clearAnimation()
        fragment.view?.animate()?.cancel()
    }
}
