package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.content.Intent
import android.os.Bundle
import com.tokopedia.affiliate.R
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

        fun createInstance(bundle: Bundle): ContentCreatePostFragment {
            val fragment = ContentCreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_ATTACH_PRODUCT -> if (resultCode == AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_CODE_OK) {
                val products = data?.getParcelableArrayListExtra<ResultProduct>(
                        AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY)
                        ?: arrayListOf()
                viewModel.productIdList.clear()
                products.forEach {
                    viewModel.productIdList.add(it.productId.toString())
                }
                fetchContentForm()
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
                goToAttachProduct()
            }
            shouldGoAttachProduct = false
        }
    }

    override fun getAddRelatedProductText(): String = getString(R.string.af_change_product_tag)

    override fun onRelatedAddProductClick() {
        goToAttachProduct()
    }

    private fun goToAttachProduct() {
        val intent = AttachProductActivity.createInstance(context,
                userSession.shopId,
                "",
                true,
                "",
                viewModel.maxProduct)
        startActivityForResult(intent, REQUEST_ATTACH_PRODUCT)
    }
}