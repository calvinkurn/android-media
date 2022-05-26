package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.content.Context
import android.graphics.Bitmap
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
import com.tokopedia.tokomember_seller_dashboard.util.*
import com.tokopedia.tokomember_seller_dashboard.util.COUPON_CASHBACK
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
    private var shopName = ""
    private var shopAvatar = ""

    init {
        View.inflate(context, R.layout.tm_dash_single_coupon, this)
        initView()
    }

     fun setShopData(shopName:String, shopAvatar:String) {
         this.shopAvatar = shopAvatar
         this.shopName = shopName
         ivPreviewCoupon.setInitialData(shopName,shopAvatar)
     }

    private fun initView() {
        chipGroupKuponType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionKupon = position
                when(selectedChipPositionKupon){
                    CouponType.CASHBACK -> {
                        ivPreviewCoupon.showHideCashBackValueView(true)
                        ivPreviewCoupon.setCouponType(COUPON_CASHBACK_PREVIEW)
                    }
                    CouponType.SHIPPING -> {
                        ivPreviewCoupon.showHideCashBackValueView(false)
                        ivPreviewCoupon.setCouponType(COUPON_SHIPPING_PREVIEW)
                    }
                }
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
        textFieldMaxCashback.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    ivPreviewCoupon.setCouponValue(number.toString())
                }
            })
        }
    }

    private fun minTransactionFieldValidation(){
        textFieldMinTransk.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                }
            })
        }
    }

    private fun chipCashBackCouponValidation(){
        textFieldPercentCashback.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    ivPreviewCoupon.setCouponBenefit(number.toString())
                }
            })
        }
    }

    fun getCouponView(): View {
        return ivPreviewCoupon
    }

    fun setErrorMaxBenefit(error:String){
        if (error.isNotEmpty()) {
            textFieldMaxCashback.isInputError = true
            textFieldMaxCashback.setMessage(error)
        }
    }

    fun setErrorMinTransaction(error:String){
        if(error.isNotEmpty()) {
            textFieldMinTransk.isInputError = true
            textFieldMinTransk.setMessage(error)
        }
    }

    fun setErrorCashbackPercentage(error:String){
        if (error.isNotEmpty()) {
            textFieldPercentCashback.isInputError = true
            textFieldPercentCashback.setMessage(error)
        }
    }

    fun setErrorQuota(error:String){
        if (error.isNotEmpty()) {
            textFieldQuota.isInputError = true
            textFieldQuota.setMessage(error)
        }
    }
}