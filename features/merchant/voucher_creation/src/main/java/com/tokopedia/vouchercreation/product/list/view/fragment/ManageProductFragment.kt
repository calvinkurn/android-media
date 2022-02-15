package com.tokopedia.vouchercreation.product.list.view.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.product.create.data.response.ProductId
import javax.inject.Inject

class ManageProductFragment : BaseDaggerFragment() {

    companion object {

        private const val ZERO = 0
        private const val NO_BACKGROUND: Int = 0
        const val BUNDLE_KEY_MAX_PRODUCT_LIMIT = "maxProductLimit"
        const val BUNDLE_SELECTED_PRODUCTS = "selectedProducts"

        @JvmStatic
        fun createInstance(maxProductLimit: Int,
                           selectedProductIds: ArrayList<ProductId>) = AddProductFragment().apply {
            this.arguments = Bundle().apply {
                putInt(BUNDLE_KEY_MAX_PRODUCT_LIMIT, maxProductLimit)
                putParcelableArrayList(BUNDLE_SELECTED_PRODUCTS, selectedProductIds)
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    override fun getScreenName(): String {
        TODO("Not yet implemented")
    }

    override fun initInjector() {
        TODO("Not yet implemented")
    }


}