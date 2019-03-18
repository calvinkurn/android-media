package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.content.Intent
import android.os.Bundle
import com.tokopedia.affiliate.feature.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.attachproduct.resultmodel.ResultProduct
import com.tokopedia.attachproduct.view.activity.AttachProductActivity

/**
 * @author by milhamj on 01/03/19.
 */
class ContentCreatePostFragment : BaseCreatePostFragment() {

    private var shouldGoAttachProduct = true

    companion object {
        private const val REQUEST_ATTACH_PRODUCT = 10
        private const val REQUEST_ATTACH_PRODUCT_FIRST = 11

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
            REQUEST_ATTACH_PRODUCT_FIRST -> {
                if (resultCode == AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK
                        && data != null) {
                    getAttachProductResult(data)
                } else {
                    activity?.finish()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }

    }

    override fun fetchContentForm() {
        presenter.fetchContentForm(viewModel.productIdList, viewModel.authorType)
    }

    override fun onSuccessGetContentForm(feedContentForm: FeedContentForm) {
        super.onSuccessGetContentForm(feedContentForm)
        if (shouldGoAttachProduct) {
            if (viewModel.productIdList.isEmpty() || viewModel.productIdList.first().isBlank()) {
                goToAttachProduct(true)
            }
            shouldGoAttachProduct = false
        }
    }

    override fun onRelatedAddProductClick() {
        goToAttachProduct(false)
    }

    private fun goToAttachProduct(isFirstTime: Boolean) {
        val intent = AttachProductActivity.createInstance(context,
                userSession.shopId,
                "",
                true,
                "",
                viewModel.maxProduct - viewModel.productIdList.size,
                ArrayList(viewModel.productIdList)
        )
        startActivityForResult(
                intent,
                if (isFirstTime) REQUEST_ATTACH_PRODUCT_FIRST else REQUEST_ATTACH_PRODUCT
        )
    }

    private fun getAttachProductResult(data: Intent?) {
        val products = data?.getParcelableArrayListExtra<ResultProduct>(
                AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                ?: arrayListOf()
        products.forEach {
            viewModel.productIdList.add(it.productId.toString())
        }
        fetchContentForm()
    }
}