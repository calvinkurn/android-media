package com.tokopedia.tokomember_common_widget

import com.tokopedia.imageassets.ImageUrl

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import kotlinx.android.synthetic.main.tm_coupon_preview.view.*
import kotlin.math.floor
import com.tokopedia.kotlin.extensions.view.toIntOrZero

const val TM_COUPON_PREVIEW = ImageUrl.TM_COUPON_PREVIEW

class TmCouponViewPreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.tm_coupon_preview, this)
        //imagePreview.loadImage(TM_COUPON_PREVIEW)
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

    @SuppressLint("SetTextI18n")
    fun setCouponBenefit(couponBenefit: String) {
        tvCouponBenefit.text = "$couponBenefit%"
    }

    fun setCouponValue(couponValue: String) {
        tvCouponValue.text = floorCouponValue(couponValue)
    }


    private fun floorCouponValue(couponValue: String) : String {
        //1,000 to 999,999 - Rb
        // 1000000 to 999999999 - Jt
        var result = ""
        if(!couponValue.contains(".")){
            return (couponValue.toIntOrZero()/1000).toString()
        }
        else {
            result = floor((couponValue.toDouble().toInt() / 1000.0)).toString()
        }
            return result.replaceRange(result.indexOf("."), result.length, "")
        }

}
