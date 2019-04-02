package com.tokopedia.affiliate.feature.createpost.view.fragment

import android.os.Bundle
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.affiliate.feature.createpost.view.viewmodel.CreatePostViewModel
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager

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
        presenter.fetchContentForm(viewModel.adIdList, viewModel.authorType)
    }

    override fun onRelatedAddProductClick() {
        localCacheHandler.putString(cacheKeyFormatted, gson.toJson(viewModel))
        localCacheHandler.applyEditor()
        isAddingProduct = true

        RouteManager.route(context, ApplinkConst.AFFILIATE_EXPLORE)
    }

    override fun initVar(savedInstanceState: Bundle?) {
        val cache = localCacheHandler.getString(cacheKeyFormatted, "")
        if (TextUtils.isEmpty(cache)) {
            super.initVar(savedInstanceState)
        } else {
            viewModel = gson.fromJson(cache, CreatePostViewModel::class.java)
            initProductIds()
            isAddingProduct = false
        }
    }
}