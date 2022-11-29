package com.tokopedia.search.result.product.addtocart

import android.content.Context
import android.content.Intent
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant.REQUEST_CODE_CHECKOUT
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.search.di.qualifier.SearchContext
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.utils.FragmentProvider
import com.tokopedia.search.utils.contextprovider.ContextProvider
import com.tokopedia.search.utils.contextprovider.WeakReferenceContextProvider
import javax.inject.Inject

@SearchScope
class AddToCartVariantBottomSheetLauncher @Inject constructor(
    @SearchContext
    context: Context?,
    fragmentProvider: FragmentProvider,
): ContextProvider by WeakReferenceContextProvider(context),
    FragmentProvider by fragmentProvider {

    private var onCheckout: ((ProductVariantResult) -> Unit)? = {}

    fun launch(
        productId: String,
        shopId: String,
        trackerCDListName: String,
        onCheckout: (ProductVariantResult) -> Unit
    ) {
        val context = context ?: return

        AtcVariantHelper.goToAtcVariant(
            context = context,
            productId = productId,
            pageSource = VariantPageSource.SRP_PAGESOURCE,
            shopId = shopId,
            trackerCdListName = trackerCDListName,
            startActivitResult = ::startActivityResult,
        )

        this.onCheckout = onCheckout
    }

    private fun startActivityResult(intent: Intent, requestCode: Int) {
        getFragment().startActivityForResult(intent, requestCode)
    }

    fun onActivityResult(requestCode: Int, ignored: Int, data: Intent?) {
        val context = context ?: return

        AtcVariantHelper.onActivityResultAtcVariant(context, requestCode, data) {
            if (this.requestCode == REQUEST_CODE_CHECKOUT) {
                onCheckout?.invoke(this)
            }

            onCheckout = null
        }
    }
}
