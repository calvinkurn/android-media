package com.tokopedia.createpost.view.fragment

import android.content.Intent
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst.AttachProduct.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.createpost.common.view.viewmodel.ProductSuggestionItem

/**
 * @author by milhamj on 01/03/19.
 */
class ContentCreatePostFragment : BaseCreatePostFragment() {
    private var isAddingProduct = false

    companion object {
        private const val REQUEST_ATTACH_PRODUCT = 10
        const val TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK = 324
        private const val MAX_ALLOWED_PRODUCT = 5

        fun createInstance(bundle: Bundle): ContentCreatePostFragment {
            val fragment = ContentCreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ATTACH_PRODUCT -> if (resultCode == TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK) {
                getAttachProductResult(data)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun fetchContentForm() {
        presenter.fetchContentForm(viewModel.productIdList, viewModel.authorType, viewModel.postId)
    }

    override fun onRelatedAddProductClick() {
        goToAttachProduct()
    }

    private fun goToAttachProduct() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ATTACH_PRODUCT)
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SHOP_ID_KEY, userSession.shopId)
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_IS_SELLER_KEY, true)
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, "")
        intent.putExtra(TOKOPEDIA_ATTACH_PRODUCT_MAX_CHECKED, MAX_ALLOWED_PRODUCT - viewModel.productIdList.size)
        intent.putStringArrayListExtra(TOKOPEDIA_ATTACH_PRODUCT_HIDDEN, ArrayList(viewModel.productIdList))

        startActivityForResult(intent, REQUEST_ATTACH_PRODUCT)
    }

    private fun getAttachProductResult(data: Intent?) {
        isAddingProduct = true
        val products = data?.getParcelableArrayListExtra<ResultProduct>(
                TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                ?: arrayListOf()
        products.forEach {
            viewModel.productIdList.add(it.productId.toString())
        }
        fetchContentForm()
    }

    override fun updateRelatedProduct(){
        super.updateRelatedProduct()
        if (isAddingProduct){
            productSmoothScroller.targetPosition = adapter.itemCount - 1
            productAttachmentLayoutManager.startSmoothScroll(productSmoothScroller)
            isAddingProduct = false
        }
    }

    override fun fetchProductSuggestion(onSuccess: (List<ProductSuggestionItem>) -> Unit,
                                        onError: (Throwable) -> Unit) {
        presenter.fetchProductSuggestion(ProductSuggestionItem.TYPE_SHOP, onSuccess, onError)
    }
}