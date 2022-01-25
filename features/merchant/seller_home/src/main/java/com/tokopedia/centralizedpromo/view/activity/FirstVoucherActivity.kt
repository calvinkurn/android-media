package com.tokopedia.centralizedpromo.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.sellerhome.SellerHomeApplinkConst
import com.tokopedia.centralizedpromo.view.fragment.FirstVoucherBottomSheetFragment
import com.tokopedia.sellerhome.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class FirstVoucherActivity: BaseSimpleActivity() {
    
    private val voucherType by lazy {
        intent.data?.getQueryParameter(SellerHomeApplinkConst.VOUCHER_TYPE).orEmpty()
    }

    private val productId by lazy {
        intent.data?.getQueryParameter(SellerHomeApplinkConst.PRODUCT_ID)
    }
    
    private val bottomSheet by lazy {
        FirstVoucherBottomSheetFragment.createInstance(voucherType, productId).apply {
            setCloseClickListener {
                this.dismiss()
            }
        }
    }

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sah_transparent_base)
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

}