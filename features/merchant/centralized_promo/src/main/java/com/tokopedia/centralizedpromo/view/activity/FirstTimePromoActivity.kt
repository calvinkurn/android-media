package com.tokopedia.centralizedpromo.view.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.centralizedpromo.CentralizedPromoApplinkConst
import com.tokopedia.centralizedpromo.R
import com.tokopedia.centralizedpromo.view.fragment.FirstTimePromoBottomSheetFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class FirstTimePromoActivity: BaseSimpleActivity() {
    
    private val promoType by lazy {
        intent.data?.getQueryParameter(CentralizedPromoApplinkConst.PROMO_TYPE).orEmpty()
    }

    private val productId by lazy {
        intent.data?.getQueryParameter(CentralizedPromoApplinkConst.PRODUCT_ID)
    }
    
    private val bottomSheet by lazy {
        FirstTimePromoBottomSheetFragment.createInstance(promoType, productId).apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_centralized_promo_transparent_base)
        setOrientation()
    }

    override fun onStart() {
        super.onStart()
        showBottomSheet()
    }

    private fun showBottomSheet() {
        (bottomSheet as? BottomSheetUnify)?.setOnDismissListener {
            finish()
        }
        bottomSheet.show(supportFragmentManager)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setOrientation() {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

}
