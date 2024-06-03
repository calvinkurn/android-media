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
import com.tokopedia.analytics.btm.BtmApi
import com.tokopedia.analytics.btm.Tokopedia
import com.tokopedia.analytics.byteio.AppLogInterface
import com.tokopedia.analytics.byteio.IAdsLog
import com.tokopedia.analytics.byteio.IAppLogActivity
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.pdp.AppLogPdp
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.extraGetString
import com.tokopedia.kotlin.extensions.intentGetBoolean
import com.tokopedia.kotlin.extensions.intentGetParcelable
import com.tokopedia.kotlin.extensions.intentGetString
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.AtcVariantHelper.ATC_VARIANT_CACHE_ID
import com.tokopedia.product.detail.common.AtcVariantHelper.PDP_PARCEL_KEY_RESULT
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantBottomSheetParams
import com.tokopedia.product.detail.common.showImmediately
import timber.log.Timber

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantActivity : BaseSimpleActivity(), AppLogInterface, IAdsLog {
    companion object {
        const val TOKO_NOW_EXTRA = "isTokoNow"
        const val PAGE_SOURCE_EXTRA = "pageSource"
        const val CD_LIST_EXTRA = "cdListName"
        private const val KEY_BS_VARIANT = "atc variant bs"
    }

    private val sharedViewModel by lazy {
        ViewModelProvider(this)[AtcVariantSharedViewModel::class.java]
    }

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.atc_variant_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        BtmApi.registerBtmPageOnCreate(this, Tokopedia.Sku)
        val paramsData = getParamsFromBundle()
        super.onCreate(savedInstanceState)

        adjustOrientation()

        observeData()

        sharedViewModel.setAtcBottomSheetParams(paramsData)

        showImmediately(supportFragmentManager, KEY_BS_VARIANT) {
            AtcVariantBottomSheet()
        }
    }

    private fun getParamsFromBundle(): ProductVariantBottomSheetParams {
        val paramsData = ProductVariantBottomSheetParams().apply {
            val uri = intent.data
            // get data from path of segments
            productId = uri?.pathSegments?.getOrNull(1).orEmpty()
            shopId = uri?.lastPathSegment.orEmpty()

            // get data from appLink query / intent extras
            isTokoNow = extraGetString(key = TOKO_NOW_EXTRA, defaultValue = "false").toBoolean()
            pageSource = extraGetString(key = PAGE_SOURCE_EXTRA)
            trackerCdListName = extraGetString(key = CD_LIST_EXTRA)

            // get data from intent
            cacheId = intentGetString(key = ATC_VARIANT_CACHE_ID)
            dismissAfterTransaction = intentGetBoolean(key = AtcVariantHelper.KEY_DISMISS_AFTER_ATC)
            saveAfterClose = intentGetBoolean(
                key = AtcVariantHelper.KEY_SAVE_AFTER_CLOSE,
                defaultValue = true
            )
            extParams = intentGetString(key = AtcVariantHelper.KEY_EXT_PARAMS)
            showQtyEditor = intentGetBoolean(
                key = AtcVariantHelper.KEY_SHOW_QTY_EDITOR,
                defaultValue = false
            )
            changeVariantOnCart = intentGetParcelable(
                key = AtcVariantHelper.KEY_CHANGE_VARIANT
            ) ?: ProductVariantBottomSheetParams.ChangeVariant()

            dismissWhenTransactionError = intentGetBoolean(
                key = AtcVariantHelper.KEY_DISMISS_WHEN_TRANSACTION_ERROR
            )
        }
        return paramsData
    }

    private fun observeData() {
        sharedViewModel.activityResult.observe(this) {
            val cacheManager = SaveInstanceCacheManager(applicationContext, true)
            val resultIntent = Intent().apply {
                putExtra(ATC_VARIANT_CACHE_ID, cacheManager.id)
            }
            cacheManager.put(PDP_PARCEL_KEY_RESULT, it)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun adjustOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        try {
            window.setDimAmount(0f)
        } catch (th: Throwable) {
            Timber.e(th)
        }
    }

    override fun getPageName(): String {
        return PageName.SKU
    }

    override fun getAdsPageName(): String {
        return PageName.SKU
    }
}
