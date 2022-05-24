package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.tokomember_common_widget.callbacks.ChipGroupCallback
import com.tokopedia.tokomember_common_widget.util.CashbackType
import com.tokopedia.tokomember_common_widget.util.CouponType
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.TmSingleCouponData
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_IDR
import com.tokopedia.tokomember_seller_dashboard.util.CASHBACK_PERCENTAGE
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_CASHBACK
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_SHIPPING
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_dash_single_coupon.view.*

class TmSingleCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var selectedChipPositionKupon: Int = 0
    private var selectedChipPositionCashback: Int = 0
    private var tmSingleCouponData = TmSingleCouponData()
    private var isShowCashPercentage = false

    companion object {


    }

    init {
        View.inflate(context, R.layout.tm_dash_single_coupon, this)
        initView()
        setClicks()
    }

    private fun setClicks() {
        tmSingleCouponData
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    private fun initView() {
        chipGroupKuponType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionKupon = position
            }
        })
        chipGroupKuponType.setDefaultSelection(selectedChipPositionKupon)
        chipGroupKuponType.addChips(arrayListOf("Cashback", "Gratis Ongkir"))

        chipGroupCashbackType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionCashback = position
                isShowCashPercentage = if (position == CashbackType.PERCENTAGE){
                    textFieldPercentCashback.show()
                    chipCashBackCouponValidation()
                    true
                } else{
                    textFieldPercentCashback.hide()
                    false
                }
            }
        })
        chipGroupCashbackType.setDefaultSelection(selectedChipPositionCashback)
        chipGroupCashbackType.addChips(arrayListOf("Rupiah(Rp)", "Persentase (%)"))
        maxCashBackFieldValidation()
        minTransactionFieldValidation()
    }

    fun getSingleCouponData():TmSingleCouponData{

        when(selectedChipPositionKupon){
            CouponType.CASHBACK -> {
                tmSingleCouponData.typeCoupon = COUPON_CASHBACK
            }
            CouponType.SHIPPING -> {
                tmSingleCouponData.typeCoupon = COUPON_SHIPPING
            }
        }

        when(selectedChipPositionCashback){
            CashbackType.IDR -> {
                tmSingleCouponData.typeCashback = CASHBACK_IDR
            }
            CashbackType.PERCENTAGE -> {
                tmSingleCouponData.typeCashback = CASHBACK_PERCENTAGE
            }
        }

        tmSingleCouponData.maxCashback = textFieldMaxCashback.editText.text.toString().trimIndent()
        tmSingleCouponData.minTransaki = textFieldMinTransk.editText.text.toString().trimIndent()
        tmSingleCouponData.quota = textFieldQuota.editText.text.toString().trimIndent()
        if (isShowCashPercentage && textFieldPercentCashback.editText.text.isNotEmpty()) {
            tmSingleCouponData.cashBackPercentage =
                textFieldMinTransk.editText.text.toString().toIntSafely()
        }
        return tmSingleCouponData
    }

    private fun maxCashBackFieldValidation(){
        textFieldMaxCashback.editText.run {
            this.addTextChangedListener(object : NumberTextWatcher(this@run){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                }
            })
        }
    }

    private fun minTransactionFieldValidation(){
        textFieldMinTransk.editText.run {
            this.addTextChangedListener(object : NumberTextWatcher(this@run){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                }
            })
        }
    }

    private fun chipCashBackCouponValidation(){
/*
        textFieldPercentCashback.editText.run {
            this.addTextChangedListener(object : NumberTextWatcher(this@run){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    if (number > 100 ){
                        textFieldPercentCashback.isInputError = true
                        textFieldPercentCashback.setMessage("Maximum reached")
                    }
                }
            })
        }
*/
    }
}