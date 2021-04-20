package com.tokopedia.productcard.options

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_MODEL
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.di.ProductCardOptionsContextModule
import kotlinx.android.synthetic.main.product_card_options_activity_layout.*
import javax.inject.Inject
import javax.inject.Named


internal class ProductCardOptionsActivity : BaseSimpleActivity() {

    @field:[Inject Named(PRODUCT_CARD_OPTIONS_VIEW_MODEL_FACTORY)]
    lateinit var productCardOptionsViewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        animateTransparentBackground()
        setTransparentBackgroundOnClick()
    }

    override fun getLayoutRes(): Int {
        return R.layout.product_card_options_activity_layout
    }

    private fun animateTransparentBackground() {
        val animation = loadAnimation(R.anim.product_card_options_fade_in)
        imageBackgroundTransparent?.startAnimation(animation)
    }

    private fun loadAnimation(@AnimRes animationResource: Int): Animation {
        return AnimationUtils.loadAnimation(this, animationResource)
    }

    private fun setTransparentBackgroundOnClick() {
        imageBackgroundTransparent?.setOnClickListener {
            finish()
        }
    }

    // setupStatusBar overriden as empty to disable status bar
    override fun setupStatusBar() { }

    override fun setupFragment(savedInstance: Bundle?) {
        injectDependencies()

        setupViewModel()

        super.setupFragment(savedInstance)
    }

    private fun injectDependencies() {
        DaggerProductCardOptionsComponent
                .builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .productCardOptionsContextModule(ProductCardOptionsContextModule(this))
                .productCardOptionsViewModelFactoryModule(createProductCardOptionsViewModelFactoryModule())
                .build()
                .inject(this)
    }

    private fun createProductCardOptionsViewModelFactoryModule(): ProductCardOptionsViewModelFactoryModule {
        val productCardOptionsModel = intent?.extras?.getParcelable<ProductCardOptionsModel>(PRODUCT_CARD_OPTIONS_MODEL)

        return ProductCardOptionsViewModelFactoryModule(productCardOptionsModel)
    }

    private fun setupViewModel() {
        ViewModelProviders.of(this, productCardOptionsViewModelFactory).get(ProductCardOptionsViewModel::class.java)
    }

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

    override fun finish() {
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
        cancelBottomSheetAnimation(fragment)
        overridePendingTransition(0, 0)

        super.finish()
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
    }

    private fun cancelBottomSheetAnimation(fragment: Fragment) {
        fragment.view?.clearAnimation()
        fragment.view?.animate()?.cancel()
    }
}
