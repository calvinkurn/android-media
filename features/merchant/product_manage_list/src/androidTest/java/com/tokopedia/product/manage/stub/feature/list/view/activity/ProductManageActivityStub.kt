package com.tokopedia.product.manage.stub.feature.list.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.product.manage.feature.list.di.ProductManageListComponent
import com.tokopedia.product.manage.feature.list.view.activity.ProductManageActivity
import com.tokopedia.product.manage.stub.feature.list.di.ProductManageListStubInstance
import com.tokopedia.product.manage.stub.feature.list.view.fragment.ProductManageSellerFragmentStub

class ProductManageActivityStub: ProductManageActivity() {

    companion object {
        private const val FILTER_ID_KEY = "filter_id"
        private const val SEARCH_KEYWORD_KEY = "search_keyword"

        @JvmStatic
        fun createIntent(context: Context): Intent =
            Intent(context, ProductManageActivityStub::class.java)

        @JvmStatic
        fun createIntent(context: Context,
                         filterId: String,
                         searchKeyword: String): Intent {
            return Intent(context, ProductManageActivityStub::class.java).apply {
                putExtra(FILTER_ID_KEY, filterId)
                putExtra(SEARCH_KEYWORD_KEY, searchKeyword)
            }
        }
    }

    private val filterId by lazy {
        intent?.extras?.getString(FILTER_ID_KEY).orEmpty()
    }

    private val searchKeyword by lazy {
        intent?.extras?.getString(SEARCH_KEYWORD_KEY).orEmpty()
    }

    override fun getComponent(): ProductManageListComponent {
        return ProductManageListStubInstance.getComponent(this)
    }


    override fun getNewFragment(): Fragment {
        return if(filterId.isBlank() && searchKeyword.isBlank()){
            ProductManageSellerFragmentStub()
        }
        else {
            ProductManageSellerFragmentStub.newInstance(arrayListOf(filterId), searchKeyword)
        }
    }

}