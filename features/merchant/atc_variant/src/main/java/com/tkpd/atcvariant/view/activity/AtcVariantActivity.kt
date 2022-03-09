package com.tkpd.atcvariant.view.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tkpd.atcvariant.R
import com.tkpd.atcvariant.view.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atcvariant.view.viewmodel.AtcVariantSharedViewModel
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.AtcVariantHelper.ATC_VARIANT_CACHE_ID
import com.tokopedia.product.detail.common.AtcVariantHelper.PDP_PARCEL_KEY_RESULT
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import timber.log.Timber

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantActivity : BaseSimpleActivity() {
    companion object {
        const val TOKO_NOW_EXTRA = "isTokoNow"
        const val PAGE_SOURCE_EXTRA = "pageSource"
        const val CD_LIST_EXTRA = "cdListName"
        private const val KEY_BS_VARIANT = "atc variant bs"
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
            uri.pathSegments.getOrNull(1) ?: ""
        } else {
            ""
        }

        val shopId = if (uri != null) {
            uri.lastPathSegment ?: ""
        } else {
            ""
        }

        val paramsData = ProductVariantBottomSheetParams()

        if (bundle != null) {
            paramsData.isTokoNow = bundle.getString(TOKO_NOW_EXTRA, "false").toBoolean()
            paramsData.pageSource = bundle.getString(PAGE_SOURCE_EXTRA, "")
            paramsData.trackerCdListName = bundle.getString(CD_LIST_EXTRA, "")
            paramsData.productId = productId
            paramsData.shopId = shopId
            paramsData.cacheId = bundle.getString(ATC_VARIANT_CACHE_ID, "") ?: ""
            paramsData.dismissAfterTransaction = intent
                    .getBooleanExtra(AtcVariantHelper.KEY_DISMISS_AFTER_ATC, false)
            paramsData.saveAfterClose = intent
                    .getBooleanExtra(AtcVariantHelper.KEY_SAVE_AFTER_CLOSE, true)
        }

        super.onCreate(savedInstanceState)
        adjustOrientation()
        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }

        observeData()

        sharedViewModel.setAtcBottomSheetParams(paramsData)
        AtcVariantBottomSheet().show(supportFragmentManager, KEY_BS_VARIANT)
    }

    private fun observeData() {
        sharedViewModel.activityResult.observe(this, {
            val cacheManager = SaveInstanceCacheManager(this, true)
            val resultIntent = Intent().apply {
                putExtra(ATC_VARIANT_CACHE_ID, cacheManager.id)
            }
            cacheManager.put(PDP_PARCEL_KEY_RESULT, it)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        })
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}