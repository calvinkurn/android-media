package com.tokopedia.tokomember_common_widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import kotlinx.android.synthetic.main.tm_coupon_preview.view.*

class TmCouponViewPreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.tm_coupon_preview, this)
    }

    fun setInitialData(shopName: String,shopAvatar:String) {
        ivShopAvatar.loadImage(shopAvatar)
        tvShopName.text = shopName
    }

    fun setCouponType(couponType: String) {
        tvCouponType.text = couponType
    }

    fun showHideCashBackValueView(flag:Boolean){
        when(flag){
            true -> {
                tvCouponBenefit.show()
                tvCouponHingga.text = "Hingga Rp"
            }
            false -> {
                tvCouponBenefit.hide()
                tvCouponHingga.text = "Rp"
            }
        }
    }

    fun setCouponBenefit(couponBenefit: String) {
        tvCouponBenefit.text = couponBenefit
    }

    fun setCouponValue(couponValue: String) {
        tvCouponValue.text = couponValue
    }

}