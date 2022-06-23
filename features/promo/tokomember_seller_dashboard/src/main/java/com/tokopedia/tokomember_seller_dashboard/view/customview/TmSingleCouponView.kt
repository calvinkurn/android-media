package com.tokopedia.tokomember_seller_dashboard.view.customview

import android.content.Context
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
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.tm_dash_single_coupon.view.*

const val QUOTA_DEFAULT = "100"
const val PERCENTAGE_DEFAULT = "5"
const val MAX_CASHBACK = "20.000"
const val MAX_CASHBACK_COUPON = "20000.0"
const val MIN_TRANSACTION = "100.000"
const val CHIP_LABEL_RUPIAH = "Rupiah(Rp)"
const val CHIP_LABEL_PERCENTAGE = "Persentase (%)"
const val CHIP_LABEL_FREE_SHIPPING = "Gratis Ongkir"
const val CHIP_LABEL_CASHBACK = "Cashback"
const val MIN_CASHBACK_CHECK = 10000
const val MAX_CASHBACK_CHECK = 100000000
const val MIN_QUOTA_CHECK = 50
const val MAX_QUOTA_CHECK = 10000
const val MIN_PERCENTAGE_CHECK = 5
const val MAX_PERCENTAGE_CHECK = 100

class TmSingleCouponView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var selectedChipPositionKupon: Int = 0
    private var selectedChipPositionCashback: Int = 0
    private var tmSingleCouponData = TmSingleCouponData()
    private var isShowCashPercentage = false
    private var shopName = ""
    private var shopAvatar = ""
    private var chipPercentageClickListener:ChipPercentageClickListener?= null
    private var maxTransactionListener:MaxTransactionListener?= null

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
        chipGroupKuponType.addChips(arrayListOf(CHIP_LABEL_CASHBACK, CHIP_LABEL_FREE_SHIPPING ))
        ivPreviewCoupon.showHideCashBackValueView(true)
        ivPreviewCoupon.setCouponType(COUPON_CASHBACK_PREVIEW)
        textFieldMaxCashback.editText.setText(MAX_CASHBACK)

        textFieldQuota.editText.setText(QUOTA_DEFAULT)
        textFieldPercentCashback.editText.setText(PERCENTAGE_DEFAULT)
        chipGroupCashbackType.setCallback(object : ChipGroupCallback {
            override fun chipSelected(position: Int) {
                selectedChipPositionCashback = position
                isShowCashPercentage = if (position == CashbackType.PERCENTAGE){
                    textFieldPercentCashback.show()
                    chipPercentageClickListener?.onClickPercentageChip()
                    chipCashBackCouponValidation()
                    true
                } else{
                    textFieldPercentCashback.hide()
                    ivPreviewCoupon.setCouponBenefit("")
                    false
                }
            }
        })
        chipGroupCashbackType.setDefaultSelection(selectedChipPositionCashback)
        chipGroupCashbackType.addChips(arrayListOf(CHIP_LABEL_RUPIAH, CHIP_LABEL_PERCENTAGE))
        maxCashBackFieldValidation()
        minTransactionFieldValidation()
        quotaValidation()
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
        ivPreviewCoupon.setCouponValue(MAX_CASHBACK_COUPON)
        textFieldMaxCashback.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    when {
                        number < MIN_CASHBACK_CHECK -> {
                            textFieldMaxCashback.isInputError = true
                            textFieldMaxCashback.setMessage("Min. Rp10.000")
                        }
                        number>= MAX_CASHBACK_CHECK -> {
                            textFieldMaxCashback.isInputError = true
                            textFieldMaxCashback.setMessage("Maks. Rp99.999.99")
                        }
                        else -> {
                            if (number > CurrencyFormatHelper.convertRupiahToInt(textFieldMinTransk.editText.text.toString())) {
                                textFieldMaxCashback.isInputError = true
                                textFieldMaxCashback.setMessage("Harus kurang dari min. transaksi.")
                            }else {
                                textFieldMaxCashback.isInputError = false
                                textFieldMaxCashback.setMessage("")
                                maxTransactionListener?.onQuotaCashbackChange()
                                ivPreviewCoupon.setCouponValue(number.toString())
                            }
                        }
                    }
                }
            })
        }
    }

    private fun minTransactionFieldValidation(){
        textFieldMinTransk.editText.setText(MIN_TRANSACTION)
        textFieldMinTransk.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    when {
                        number < MIN_CASHBACK_CHECK -> {
                            textFieldMinTransk.isInputError = true
                            textFieldMinTransk.setMessage("Min. Rp10.000")
                        }
                        number>= MAX_CASHBACK_CHECK -> {
                            textFieldMinTransk.isInputError = true
                            textFieldMinTransk.setMessage("Maks. Rp99.999.99")
                        }
                        else -> {
                            textFieldMinTransk.isInputError = false
                            textFieldMinTransk.setMessage("")
                        }
                    }
                }
            })
        }
    }

    private fun chipCashBackCouponValidation(){
        textFieldPercentCashback.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText) {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    when {
                        number < MIN_PERCENTAGE_CHECK -> {
                            textFieldPercentCashback.isInputError = true
                            textFieldPercentCashback.setMessage("Min. 5%")
                        }
                        number >= MAX_PERCENTAGE_CHECK -> {
                            textFieldPercentCashback.isInputError = true
                            textFieldPercentCashback.setMessage("Maks. 100%")
                        }
                        else -> {
                            textFieldPercentCashback.isInputError = false
                            textFieldPercentCashback.setMessage("")
                            ivPreviewCoupon.setCouponBenefit(number.toString())
                        }
                    }
                }
            })
        }
    }

    private fun quotaValidation(){
        textFieldQuota.let {
            it.editText.addTextChangedListener(object : NumberTextWatcher(it.editText){
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    when {
                        number < MIN_QUOTA_CHECK -> {
                            textFieldQuota.isInputError = true
                            textFieldQuota.setMessage("Min. 50 kuota")
                        }
                        number>= MAX_QUOTA_CHECK -> {
                            textFieldQuota.isInputError = true
                            textFieldQuota.setMessage("Maks. 10.000 kuota")
                        }
                        else -> {
                            textFieldQuota.isInputError = false
                            textFieldQuota.setMessage("")
                            maxTransactionListener?.onQuotaCashbackChange()
                        }
                    }
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

    fun setChipPercentageClickListener(chipPercentageClickListener:ChipPercentageClickListener){
        this.chipPercentageClickListener = chipPercentageClickListener
    }

    fun setMaxTransactionListener(maxTransactionListener:MaxTransactionListener){
        this.maxTransactionListener = maxTransactionListener
    }

    interface ChipPercentageClickListener{
        fun onClickPercentageChip()
    }

    interface MaxTransactionListener{
        fun onQuotaCashbackChange()
    }
}