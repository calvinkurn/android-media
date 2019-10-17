package com.tokopedia.createpost.view.fragment

import android.os.Bundle
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.createpost.TOKEN
import com.tokopedia.createpost.data.pojo.getcontentform.FeedContentForm
import com.tokopedia.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.createpost.view.viewmodel.ProductSuggestionItem
import java.lang.Exception

/**
 * @author by milhamj on 01/03/19.
 */
class AffiliateCreatePostFragment : BaseCreatePostFragment() {

    private var isAddingProduct = false

    private val localCacheHandler : LocalCacheHandler by lazy {
        LocalCacheHandler(context, AF_CREATE_POST_CACHE)
    }

    private val gson : Gson by lazy {
        GsonBuilder().create()
    }

    private val cacheKeyFormatted : String by lazy {
        String.format(AF_ADD_PRODUCT, userSession.userId)
    }

    companion object {
        private const val AF_CREATE_POST_CACHE = "af_create_post_cache"
        private const val AF_ADD_PRODUCT = "af_add_product_%s"
        private const val REQUEST_ATTACH_AFFILIATE_PRODUCT = 12

        fun createInstance(bundle: Bundle): AffiliateCreatePostFragment {
            val fragment = AffiliateCreatePostFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onStop() {
        super.onStop()
        if (!isAddingProduct) {
            localCacheHandler.clearCache()
        }
    }

    override fun fetchContentForm() {
        val token = arguments?.getString(TOKEN)
        if (token != null) presenter.fetchContentFormByToken(token, viewModel.authorType)
        else presenter.fetchContentForm(viewModel.adIdList, viewModel.authorType, viewModel.postId)
    }

    override fun onRelatedAddProductClick() {
        localCacheHandler.putString(cacheKeyFormatted, gson.toJson(viewModel))
        localCacheHandler.applyEditor()
        isAddingProduct = true

        val intent = RouteManager.getIntent(context, ApplinkConst.AFFILIATE_EXPLORE)
        startActivity(intent)
    }

    override fun initVar(savedInstanceState: Bundle?) {
        val cache = localCacheHandler.getString(cacheKeyFormatted, "")
        if (TextUtils.isEmpty(cache)) {
            super.initVar(savedInstanceState)
        } else {
            try {
                viewModel = gson.fromJson(cache, CreatePostViewModel::class.java)
                initProductIds()
                isAddingProduct = false
            } catch (e: Exception) {
                clearCache()
                super.initVar(savedInstanceState)
            }
        }
    }

    override fun updateRelatedProduct() {
        super.updateRelatedProduct()
        if (adapter.itemCount > 0) {
            productAttachmentLayoutManager.scrollToPosition(adapter.itemCount - 1)
        }
    }

    override fun onSuccessGetContentForm(feedContentForm: FeedContentForm, isFromTemplateToken: Boolean) {
        if (isFromTemplateToken) {
            val currentIdList = viewModel.adIdList.toList()
            viewModel.adIdList.clear()
            viewModel.adIdList.addAll(
                    currentIdList.union(feedContentForm.relatedItems.map { it.id })
            )
        }
        super.onSuccessGetContentForm(feedContentForm, isFromTemplateToken)
    }

    override fun fetchProductSuggestion(onSuccess: (List<ProductSuggestionItem>) -> Unit,
                                        onError: (Throwable) -> Unit) {
        presenter.fetchProductSuggestion(ProductSuggestionItem.TYPE_AFFILIATE, onSuccess, onError)
    }

    fun clearCache() {
        localCacheHandler.clearCache()
    }
}