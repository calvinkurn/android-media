package com.tokopedia.productcard.options

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_MODEL
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.di.ProductCardOptionsContextModule
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.product_card_options_activity_layout.*
import javax.inject.Inject
import javax.inject.Named


internal class ProductCardOptionsActivity : BaseSimpleActivity() {

    @field:[Inject Named(PRODUCT_CARD_OPTIONS_VIEW_MODEL_FACTORY)]
    lateinit var productCardOptionsViewModelFactory: ViewModelProvider.Factory
    private var bottomSheetProductCardOptions = BottomSheetUnify()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configBottomsheet()
        bottomSheetProductCardOptions.show(supportFragmentManager,tagFragment)
    }

    override fun getLayoutRes(): Int {
        return R.layout.product_card_options_activity_layout
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

    override fun inflateFragment() { }

    override fun finish() {
        fragment?.let {
            removeFragment(it)
            super.finish()
            overridePendingTransition(0, 0)
        }
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

    private fun configBottomsheet() {
        val childView = View.inflate(this.baseContext, R.layout.product_card_options_activity_layout, null)
        bottomSheetProductCardOptions.apply {
            setChild(childView)
            showKnob = true
            showHeader = false
            showCloseIcon = false
            isHideable = true
            setOnDismissListener {
                finish()
            }
            setShowListener {
                childFragmentManager
                        .beginTransaction()
                        .replace(R.id.parentView, newFragment, tagFragment)
                        .commit()

                bottomSheetProductCardOptions.bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) {
                        println("hahaha x $p1")
                    }

                    override fun onStateChanged(p0: View, p1: Int) {
                        println("hahaha $p1")
                    }
                })
            }
        }

    }
}
