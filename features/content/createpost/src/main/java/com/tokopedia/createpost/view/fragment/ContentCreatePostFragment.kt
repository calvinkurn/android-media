package com.tokopedia.createpost.view.fragment

import android.content.Intent
import android.os.Bundle
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity
import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem

/**
 * @author by milhamj on 01/03/19.
 */
class ContentCreatePostFragment : BaseCreatePostFragment() {
    private var isAddingProduct = false

    companion object {
        private const val REQUEST_ATTACH_PRODUCT = 10

        fun createInstance(bundle: Bundle): ContentCreatePostFragment {
            val fragment = ContentCreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ATTACH_PRODUCT -> if (resultCode == AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK) {
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
        val intent = AttachProductActivity.createInstance(context,
                userSession.shopId,
                "",
                true,
                "",
                viewModel.maxProduct - viewModel.productIdList.size,
                ArrayList(viewModel.productIdList)
        )
        startActivityForResult(intent, REQUEST_ATTACH_PRODUCT)
    }

    private fun getAttachProductResult(data: Intent?) {
        isAddingProduct = true
        val products = data?.getParcelableArrayListExtra<ResultProduct>(
                AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
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