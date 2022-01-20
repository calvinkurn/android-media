package com.tokopedia.product.manage.stub.feature.list.view.fragment

import android.os.Bundle
import com.tokopedia.product.manage.feature.list.view.fragment.ProductManageSellerFragment
import java.util.ArrayList

class ProductManageSellerFragmentStub: ProductManageSellerFragment() {

    companion object {
        @JvmStatic
        fun newInstance(
            filterOptions: ArrayList<String>,
            searchKeyWord: String
        ): ProductManageSellerFragment {
            return ProductManageSellerFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(FILTER_OPTIONS, filterOptions)
                    putString(SEARCH_KEYWORD_OPTIONS, searchKeyWord)
                }
            }
        }
    }

}