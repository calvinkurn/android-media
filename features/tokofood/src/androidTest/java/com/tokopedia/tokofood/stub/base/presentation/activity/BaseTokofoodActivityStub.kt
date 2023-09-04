package com.tokopedia.tokofood.stub.base.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.stub.purchase.presentation.fragment.TokoFoodPurchaseFragmentStub
import com.tokopedia.tokofood.stub.purchase.promo.presentation.fragment.TokoFoodPromoFragmentStub

class BaseTokofoodActivityStub: BaseTokofoodActivity() {

    override fun getRootFragment(): Fragment {
        return when (intent?.getStringExtra(PAGE_KEY)) {
            PURCHASE_PAGE -> TokoFoodPurchaseFragmentStub.createInstance()
            else -> TokoFoodPromoFragmentStub.createInstance()
        }
    }

    companion object {

        private const val PAGE_KEY = "page_key"

        const val PURCHASE_PAGE = "purchase_page"
        const val PROMO_PAGE = "promo_page"

        @JvmStatic
        fun createIntent(
            context: Context,
            page: String
        ): Intent {
            return Intent(context, BaseTokofoodActivityStub::class.java).apply {
                putExtra(PAGE_KEY, page)
            }
        }
    }

}
