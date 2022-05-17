package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.tokomember_common_widget.util.MemberType
import com.tokopedia.tokomember_seller_dashboard.R

class TmSingleCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        const val REQUEST_CODE = 121
        const val RESULT_CODE_OK = 1
    }


    @MemberType
    var shopType: Int = MemberType.PREMIUM

    init {
        View.inflate(context, R.layout.tm_dash_single_coupon, this)
        initViews()
        setClicks()
    }

    private fun initViews() {

    }

    private fun setClicks() {

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    fun setData(

    ) {

    }


    fun sendImpressionTrackerForPdp() {

    }
}