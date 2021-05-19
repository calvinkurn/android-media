package com.tkpd.atc_variant.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.views.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atc_variant.views.bottomsheet.AtcVariantBottomSheetListener
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.detail.common.AtcVariantHelper.PDP_CACHE_ID_KEY
import com.tokopedia.product.detail.common.AtcVariantHelper.PDP_PARCEL_KEY_RESPONSE
import com.tokopedia.product.detail.common.AtcVariantHelper.PDP_PARCEL_KEY_RESULT
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import timber.log.Timber

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantActivity : BaseSimpleActivity(), AtcVariantBottomSheetListener {
    companion object {
        const val TOKO_NOW_EXTRA = "isTokoNow"
        const val PAGE_SOURCE_EXTRA = "pageSource"
        const val PARENT_ID_EXTRA = "parentId"
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(this).get(AtcVariantSharedViewModel::class.java)
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.atc_variant_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val uri = intent.data
        val bundle = intent.extras
        val productId = if (uri != null) {
            uri.lastPathSegment ?: ""
        } else {
            ""
        }

        var paramsData = ProductVariantBottomSheetParams()

        if (bundle != null) {
            val cacheId = bundle.getString(PDP_CACHE_ID_KEY)

            val cacheManager = SaveInstanceCacheManager(this, cacheId)
            val data: ProductVariantBottomSheetParams? = cacheManager.get(PDP_PARCEL_KEY_RESPONSE, ProductVariantBottomSheetParams::class.java, null)

            if (data == null) {
                paramsData.isTokoNow = bundle.getString(TOKO_NOW_EXTRA, "false").toBoolean()
                paramsData.pageSource = bundle.getString(PAGE_SOURCE_EXTRA, "")
                paramsData.productParentId = bundle.getString(PARENT_ID_EXTRA, "")
                paramsData.productId = productId
            } else {
                paramsData = data
            }
        }

        super.onCreate(savedInstanceState)

        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }

        observeData()

        sharedViewModel.setAtcBottomSheetParams(paramsData)
        AtcVariantBottomSheet().show(supportFragmentManager, "test", this)
    }

    private fun observeData() {
        sharedViewModel.activityResult.observe(this, {
            val cacheManager = SaveInstanceCacheManager(this)
            val resultIntent = Intent().apply {
                putExtra(PDP_CACHE_ID_KEY, cacheManager.id)
            }
            cacheManager.put(PDP_PARCEL_KEY_RESULT, it)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        })
    }

    override fun onBottomSheetDismiss() {
        finish()
    }

}