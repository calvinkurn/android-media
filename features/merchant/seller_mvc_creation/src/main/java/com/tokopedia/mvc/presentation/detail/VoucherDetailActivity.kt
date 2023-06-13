package com.tokopedia.mvc.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.mvc.R
import com.tokopedia.mvc.common.util.SharedPreferencesUtil
import com.tokopedia.mvc.util.constant.BundleConstant

class VoucherDetailActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, voucherId: Long) {
            SharedPreferencesUtil().setEditCouponSourcePage(context, this::class.java.toString())
            val intent = Intent(context, VoucherDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putLong(BundleConstant.BUNDLE_VOUCHER_ID, voucherId)
            intent.putExtras(bundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }

        private const val VOUCHER_ID_SEGMENT = 2
    }

    private val voucherId by lazy {
        val appLinkData = RouteManager.getIntent(this, intent?.data?.toString().orEmpty()).data
        if (appLinkData?.pathSegments.orEmpty().isNotEmpty()) {
            appLinkData?.pathSegments?.getOrNull(VOUCHER_ID_SEGMENT)?.toLong().orZero()
        } else {
            intent?.extras?.getLong(BundleConstant.BUNDLE_VOUCHER_ID).orZero()
        }
    }

    override fun getLayoutRes() = R.layout.smvc_activity_voucher_detail
    override fun getNewFragment() = VoucherDetailFragment.newInstance(voucherId)
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.smvc_activity_voucher_detail)
    }
}
