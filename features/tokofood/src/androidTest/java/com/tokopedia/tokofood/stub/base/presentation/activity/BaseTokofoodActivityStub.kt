package com.tokopedia.tokofood.stub.base.presentation.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodActivity
import com.tokopedia.tokofood.stub.purchase.presentation.fragment.TokoFoodPurchaseFragmentStub

class BaseTokofoodActivityStub: BaseTokofoodActivity() {

    override fun getRootFragment(): Fragment {
        return TokoFoodPurchaseFragmentStub.createInstance()
    }

    companion object {

        private const val PAGE_KEY = "page_key"

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
