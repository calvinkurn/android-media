package com.tokopedia.shopdiscount.manage.presentation.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent
import com.tokopedia.shopdiscount.utils.navigation.FragmentRouter
import javax.inject.Inject

class ProductManageActivity : AppCompatActivity() {

    @Inject
    lateinit var router : FragmentRouter

    companion object {
        private const val BUNDLE_KEY_FOCUS_TO_UPCOMING_STATUS_TAB = "focus_to_upcoming_status_tab"

        @JvmStatic
        fun start(context: Context, focusToUpcomingStatusTab : Boolean) {
            val starter = Intent(context, ProductManageActivity::class.java)
                .putExtra(BUNDLE_KEY_FOCUS_TO_UPCOMING_STATUS_TAB, focusToUpcomingStatusTab)
            context.startActivity(starter)
        }
    }

    private val focusToUpcomingStatusTab by lazy {
        intent?.extras?.getBoolean(
            BUNDLE_KEY_FOCUS_TO_UPCOMING_STATUS_TAB
        ).orFalse()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_product_manage)
        displayFragment()
    }

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun displayFragment() {
        val fragment = ProductManageFragment.newInstance(focusToUpcomingStatusTab)
        router.replace(supportFragmentManager, R.id.container, fragment)
    }

}