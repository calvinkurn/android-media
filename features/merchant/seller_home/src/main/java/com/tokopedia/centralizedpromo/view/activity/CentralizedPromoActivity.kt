package com.tokopedia.centralizedpromo.view.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.centralizedpromo.view.customview.FirstVoucherBottomSheetView
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.kotlin.extensions.view.setStatusBarColor
import com.tokopedia.unifycomponents.BottomSheetUnify

class CentralizedPromoActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, CentralizedPromoActivity::class.java)

        private const val FIRST_VOUCHER_BOTTOMSHEET_TAG = "first_voucher_bottomsheet"
    }

    private val bottomSheet by lazy {
        BottomSheetUnify().apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    private val bottomSheetView by lazy {
        FirstVoucherBottomSheetView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarColor(android.R.color.white)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun getNewFragment(): Fragment = CentralizedPromoFragment.createInstance()

    private fun handleIntent(intent: Intent?) {
        intent?.data?.toString()?.let { uri ->
            if (uri.startsWith(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO_FIRST_VOUCHER)) {
                showBottomSheet(bottomSheetView)
            }
        }
    }

    private fun showBottomSheet(view: View) {
        bottomSheet.run {
            setChild(view)
            show(supportFragmentManager, FIRST_VOUCHER_BOTTOMSHEET_TAG)
        }
    }
}