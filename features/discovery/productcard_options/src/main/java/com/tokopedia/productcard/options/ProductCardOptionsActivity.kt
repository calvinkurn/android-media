package com.tokopedia.productcard.options

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.discovery.common.manager.PRODUCT_CARD_OPTIONS_MODEL
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.di.ProductCardOptionsContextModule
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject
import javax.inject.Named


internal class ProductCardOptionsActivity : BaseSimpleActivity() {

    @field:[Inject Named(PRODUCT_CARD_OPTIONS_VIEW_MODEL_FACTORY)]
    lateinit var productCardOptionsViewModelFactory: ViewModelProvider.Factory
    private var bottomSheetProductCardOptions: BottomSheetUnify? = null

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        supportFragmentManager.fragmentFactory = fragmentFactory
        super.onCreate(savedInstanceState)

        configBottomsheet()
        bottomSheetProductCardOptions?.show(supportFragmentManager,tagFragment)
    }

    override fun getLayoutRes(): Int {
        return R.layout.product_card_options_activity_layout
    }

    // setupStatusBar overriden as empty to disable status bar
    override fun setupStatusBar() { }

    override fun setupFragment(savedInstance: Bundle?) {
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
        return ProductCardOptionsFragment.newInstance(
            classLoader,
            supportFragmentManager.fragmentFactory,
        )
    }

    override fun inflateFragment() { }

    override fun finish() {
        fragment?.let {
            removeFragment(it)
        }
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss()
    }

    private fun configBottomsheet() {
        val childView = View.inflate(this.baseContext, R.layout.product_card_options_activity_layout, null)
        bottomSheetProductCardOptions = BottomSheetUnify().apply {
            setChild(childView)
            showKnob = true
            showHeader = false
            showCloseIcon = false
            isHideable = true
            clearContentPadding = true
            setOnDismissListener {
                Handler().postDelayed({
                    finish()
                }, FINISH_ACTIVITY_DELAY)
            }
            setShowListener {
                // check whether activity is finishing or has been destroyed
                if(!isFinishing || !isDestroyed) {
                    childFragmentManager
                            .beginTransaction()
                            .replace(R.id.parentView, newFragment, tagFragment)
                            // in case the commit happened after previous state check, allow state loss
                            .commitAllowingStateLoss()
                }

                bottomSheetProductCardOptions = null
            }
        }
    }

    companion object {
        private const val FINISH_ACTIVITY_DELAY = 200L
    }
}
