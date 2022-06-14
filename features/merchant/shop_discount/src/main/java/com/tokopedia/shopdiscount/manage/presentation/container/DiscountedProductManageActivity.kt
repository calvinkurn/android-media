package com.tokopedia.shopdiscount.manage.presentation.container

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.di.component.DaggerShopDiscountComponent

class DiscountedProductManageActivity : BaseSimpleActivity() {

    companion object {
        private const val BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID = "previous_discount_status_id"
        private const val BUNDLE_KEY_TOASTER_WORDING = "toaster_wording"

        @JvmStatic
        fun start(
            context: Context, previouslySelectedDiscountStatusId: Int,
            toasterWording: String
        ) {
            val starter = Intent(context, DiscountedProductManageActivity::class.java)
                .putExtra(
                    BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID,
                    previouslySelectedDiscountStatusId
                )
                .putExtra(BUNDLE_KEY_TOASTER_WORDING, toasterWording)
            starter.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(starter)
        }
    }

    private val previouslySelectedDiscountStatusId by lazy {
        intent?.extras?.getInt(
            BUNDLE_KEY_PREVIOUS_DISCOUNT_STATUS_ID
        ).orZero()
    }

    private val toasterWording by lazy {
        intent?.extras?.getString(
            BUNDLE_KEY_TOASTER_WORDING
        ).orEmpty()
    }

    override fun getLayoutRes() = R.layout.activity_product_manage
    override fun getNewFragment() = DiscountedProductManageFragment.newInstance(previouslySelectedDiscountStatusId, toasterWording)
    override fun getParentViewResourceID() = R.id.container

    private fun setupDependencyInjection() {
        DaggerShopDiscountComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_product_manage)
    }
}