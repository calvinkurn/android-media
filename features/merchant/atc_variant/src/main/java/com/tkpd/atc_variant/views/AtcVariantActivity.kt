package com.tkpd.atc_variant.views

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.tkpd.atc_variant.R
import com.tkpd.atc_variant.views.bottomsheet.AtcVariantBottomSheet
import com.tkpd.atc_variant.views.bottomsheet.AtcVariantBottomSheetListener
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import timber.log.Timber

/**
 * Created by Yehezkiel on 05/05/21
 */
class AtcVariantActivity : BaseSimpleActivity(), AtcVariantBottomSheetListener {
    companion object{
        const val TOKO_NOW_EXTRA = "isTokoNow"
        const val PAGE_SOURCE_EXTRA = "pageSource"
        const val PARENT_ID_EXTRA = "parentId"
    }

    private var isTokoNow = false
    private var pageSource = ""
    private var parentId = ""

    override fun getNewFragment(): Fragment? = null

    override fun getLayoutRes(): Int {
        return R.layout.atc_variant_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        val uri = intent.data
        val bundle = intent.extras
        if (uri != null) {
            val productId = uri.lastPathSegment
            Log.e("pidnya","ini $productId")
        }

        if (bundle != null) {
            isTokoNow = bundle.getString(TOKO_NOW_EXTRA, "false").toBoolean()
            pageSource = bundle.getString(PAGE_SOURCE_EXTRA, "")
            parentId = bundle.getString(PARENT_ID_EXTRA, "")

        }

        super.onCreate(savedInstanceState)
        try{
            window.setDimAmount(0f)
        }catch (th:Throwable){
            Timber.e(th)
        }

        AtcVariantBottomSheet().show(supportFragmentManager,"test", this)
    }

    override fun onBottomSheetDismiss() {
        finish()
    }

}