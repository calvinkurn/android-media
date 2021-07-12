package com.tokopedia.moneyin.viewcontrollers.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCategory
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.common_tradein.model.AddressResult
import com.tokopedia.common_tradein.model.DeviceDataResponse
import com.tokopedia.common_tradein.model.TradeInParams
import com.tokopedia.moneyin.MoneyInGTMConstants
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.viewcontrollers.activity.MoneyInCheckoutActivity
import com.tokopedia.moneyin.viewmodel.FinalPriceViewModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil.convertPriceValueToIdrFormat
import java.util.*

class FinalPriceActivity : BaseMoneyInActivity<FinalPriceViewModel>(), Observer<DeviceDataResponse?> {
    private lateinit var viewModel: FinalPriceViewModel
    private var orderValue = ""
    private var deviceId = ""
    private var checkoutString = R.string.buy_now
    private var hargeTncString = R.string.harga_tnc

    /**
     * price_valid_until
     */
    private var mTvValidTill: Typography? = null
    private var mTvModelName: Typography? = null
    private var mTvSellingPrice: Typography? = null
    private var mTvDeviceReview: Typography? = null
    private var mTvFinalAmount: Typography? = null
    private var mTvTnc: Typography? = null

    /**
     * buy_now
     */
    private var mTvButtonPayOrKtp: Typography? = null
    private var tvTitle: Typography? = null
    private var category = MoneyInGTMConstants.CATEGORY_TRADEIN_HARGA_FINAL
    override fun initInject() {
        component.inject(this)
    }

    override fun initView() {
        component.inject(this)
        setTradeInParams()
        mTvValidTill = findViewById(R.id.tv_valid_till)
        mTvModelName = findViewById(R.id.tv_model_name)
        mTvSellingPrice = findViewById(R.id.tv_selling_price)
        mTvDeviceReview = findViewById(R.id.tv_device_review)
        mTvFinalAmount = findViewById(R.id.tv_final_amount)
        mTvButtonPayOrKtp = findViewById(R.id.tv_button_pay_or_ktp)
        mTvTnc = findViewById(R.id.tv_tnc)
        tvTitle = findViewById(R.id.tv_title)
    }

    private fun setTradeInParams() {
        if (intent.hasExtra(TradeInParams::class.java.simpleName)) {
            viewModel.tradeInParams = intent.getParcelableExtra(TradeInParams::class.java.simpleName)
        }
        if (intent.hasExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE)) {
            viewModel.tradeInType = intent.getIntExtra(ApplinkConstInternalCategory.PARAM_TRADEIN_TYPE, 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkoutString = R.string.moneyin_sell_now
        hargeTncString = R.string.moneyin_harga_tnc
        category = MoneyInGTMConstants.CATEGORY_MONEYIN_HARGA_FINAL
        viewModel.deviceDiagData.observe(this, this)
        viewModel.addressLiveData.observe(this, { result: AddressResult? ->
            if (result != null) {
                if (result.defaultAddress != null) {
                    //start money in checkout with address object
                    val goToCheckout = Intent(this, MoneyInCheckoutActivity::class.java)
                    goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_DEFAULT_ADDRESS, result.defaultAddress)
                    goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_ORDER_VALUE, orderValue)
                    goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_HARDWARE_ID, deviceId)
                    navigateToActivityRequest(goToCheckout, MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT)
                } else {
                    val intent = RouteManager.getIntent(
                            this, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                    intent.putExtra(KERO_TOKEN, result.token)
                    startActivityForResult(intent, PINPOINT_ACTIVITY_REQUEST_CODE)
                }
            }
        })
    }

    override fun getViewModelType(): Class<FinalPriceViewModel> {
        return FinalPriceViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        this.viewModel = viewModel as FinalPriceViewModel
        lifecycle.addObserver(this.viewModel)
    }

    override val menuRes: Int
        get() = -1

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.layout_activity_tradein_final
    }

    override fun onChanged(deviceDataResponse: DeviceDataResponse?) {
        renderDetails(deviceDataResponse)
    }

    private fun renderDetails(deviceDataResponse: DeviceDataResponse?) {
        deviceDataResponse?.let { it ->
            val tradeInData = viewModel.tradeInParams
            tvTitle?.text = String.format(getString(R.string.moneyin_price_elligible), getString(R.string.money_in))
            val attr = it.deviceAttr
            if (attr != null) {
                mTvModelName?.text = attr.model
                deviceId = attr.imei[0]
            }
            orderValue = convertPriceValueToIdrFormat(it.oldPrice, true)
            mTvSellingPrice?.text = convertPriceValueToIdrFormat(it.oldPrice, true)
            mTvValidTill?.text = String.format(getString(R.string.price_valid_until), it.expiryTimeFmt)
            val deviceReview = it.deviceReview
            val stringBuilder = StringBuilder()
            for (review in deviceReview) {
                stringBuilder.append("•").append(review).append("\n")
            }
            mTvDeviceReview?.text = stringBuilder.toString()
            mTvFinalAmount?.text = convertPriceValueToIdrFormat(it.remainingPrice, true)
            if (tradeInData != null) {
                setbuttonCheckout()
            }
            hideProgressBar()
            sendGeneralEvent(MoneyInGTMConstants.ACTION_VIEW_MONEYIN,
                    category,
                    MoneyInGTMConstants.ACTION_VIEW_HARGA_FINAL, String.format("diagnostic id - %s", deviceId))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT -> {
                    setResult(RESULT_OK, null)
                    finish()
                }
                PINPOINT_ACTIVITY_REQUEST_CODE -> {
                    val parcelableExtra = data?.getParcelableExtra<Parcelable>(EXTRA_ADDRESS_NEW)
                    if (data?.hasExtra(EXTRA_ADDRESS_NEW) == true && parcelableExtra != null) {
                        val goToCheckout = Intent(this, MoneyInCheckoutActivity::class.java)
                        goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_NEW_ADDRESS, parcelableExtra)
                        goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_ORDER_VALUE, orderValue)
                        goToCheckout.putExtra(MoneyInCheckoutActivity.MONEY_IN_HARDWARE_ID, deviceId)
                        navigateToActivityRequest(goToCheckout, MoneyInCheckoutActivity.MONEY_IN_REQUEST_CHECKOUT)
                    }
                }
            }
        }
    }

    private fun goToCheckout() {
        viewModel.getAddress()
    }

    private fun setbuttonCheckout() {
        val notElligible = getString(hargeTncString)
        val spannableString = SpannableString(notElligible)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showTnC()
                sendGeneralEvent("clickTradeIn",
                        "harga final trade in",
                        "click syarat dan ketentuan",
                        "")
            }
        }
        val greenColor = resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
        val foregroundColorSpan = ForegroundColorSpan(greenColor)
        spannableString.setSpan(clickableSpan, 43, 61, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(foregroundColorSpan, 43, 61, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        mTvTnc?.text = spannableString
        mTvTnc?.isClickable = true
        mTvTnc?.movementMethod = LinkMovementMethod.getInstance()
        mTvButtonPayOrKtp?.setBackgroundResource(R.drawable.bg_tradein_button_orange)
        mTvButtonPayOrKtp?.text = getString(checkoutString)
        mTvButtonPayOrKtp?.setOnClickListener { v: View? ->
            goToCheckout()
            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                    category,
                    "click " + getString(checkoutString).toLowerCase(Locale.getDefault()) + " button",
                    "")
        }
    }

    companion object {
        private const val PINPOINT_ACTIVITY_REQUEST_CODE = 1302
        const val PARAM_TRADEIN_PHONE_TYPE = "PARAM_TRADEIN_PHONE_TYPE"
        private const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        private const val KERO_TOKEN = "token"
        fun getHargaFinalIntent(context: Context?): Intent {
            return Intent(context, FinalPriceActivity::class.java)
        }
    }
}