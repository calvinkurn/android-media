package com.tokopedia.moneyin.viewcontrollers.activity

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Intent
import android.graphics.Typeface
import androidx.fragment.app.Fragment
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPayment
import com.tokopedia.common.payment.PaymentConstant
import com.tokopedia.common.payment.model.PaymentPassData
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.moneyin.R
import com.tokopedia.moneyin.MoneyInGTMConstants
import com.tokopedia.moneyin.model.MoneyInCourierResponse.ResponseData.RatesV4
import com.tokopedia.common_tradein.model.MoneyInKeroGetAddressResponse.ResponseData.KeroGetAddress
import com.tokopedia.moneyin.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption.ScheduleDate
import com.tokopedia.moneyin.viewmodel.*
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.moneyin.viewcontrollers.bottomsheet.MoneyInCourierBottomSheet
import com.tokopedia.moneyin.viewcontrollers.bottomsheet.MoneyInScheduledTimeBottomSheet
import com.tokopedia.moneyin.viewmodel.MoneyInCheckoutViewModel
import com.tokopedia.moneyin.viewmodel.liveState.CourierPriceError
import com.tokopedia.moneyin.viewmodel.liveState.MutationCheckoutError
import com.tokopedia.moneyin.viewmodel.liveState.ScheduleTimeError
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Success


class MoneyInCheckoutActivity : BaseMoneyInActivity<MoneyInCheckoutViewModel>(), MoneyInScheduledTimeBottomSheet.ActionListener, MoneyInCourierBottomSheet.ActionListener {

    private lateinit var moneyInCheckoutViewModel: MoneyInCheckoutViewModel
    private lateinit var scheduleTime: ScheduleDate.ScheduleTime
    private var orderValue: String = ""
    private var hardwareId: String = ""
    private var spId: Int = -1
    private var addrId: Int = -1
    private lateinit var destination: String
    private var isCourierSet: Boolean = false
    private var isTimeSet: Boolean = false
    private var moneyInStringCancelled: String = ""
    private var moneyInStringCancelledOrFailed: String = ""

    companion object {
        const val MONEY_IN_DEFAULT_ADDRESS = "MONEY_IN_DEFAULT_ADDRESS"
        const val MONEY_IN_NEW_ADDRESS = "MONEY_IN_NEW_ADDRESS"
        const val MONEY_IN_REQUEST_CHECKOUT = 8952
        const val MONEY_IN_ORDER_VALUE = "MONEY_IN_PRICE"
        const val MONEY_IN_HARDWARE_ID = "HARDWARE_ID"
        const val STATUS_SUCCESS = 2
    }

    override fun initInject() {
        component.inject(this)
    }

    override fun initView() {
        moneyInStringCancelled = getString(R.string.money_in_alert_payment_canceled)
        moneyInStringCancelledOrFailed = getString(R.string.money_in_alert_payment_canceled_or_failed)
        sendGeneralEvent(MoneyInGTMConstants.ACTION_VIEW_MONEYIN,
                MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                MoneyInGTMConstants.ACTION_VIEW_CHECKOUT_PAGE,
                "")
        if (intent.hasExtra(MONEY_IN_ORDER_VALUE)) {
            orderValue = intent.getStringExtra(MONEY_IN_ORDER_VALUE)
        }
        if (intent.hasExtra(MONEY_IN_HARDWARE_ID)) {
            hardwareId = intent.getStringExtra(MONEY_IN_HARDWARE_ID)
        }
        if (intent.hasExtra(MONEY_IN_DEFAULT_ADDRESS)) {
            setAddressView(intent.getParcelableExtra<KeroGetAddress.Data>(MONEY_IN_DEFAULT_ADDRESS))
        }
        if (intent.hasExtra(MONEY_IN_NEW_ADDRESS)) {
            val saveAddressViewModel: SaveAddressDataModel = intent.getParcelableExtra(MONEY_IN_NEW_ADDRESS)
            saveAddressViewModel.apply {
                val keroGetAddress = KeroGetAddress.Data(id, title, address1, address2, cityId,
                        "", "", districtId, selectedDistrict, true,
                        true, true, latitude, longitude, phone, postalCode,
                        provinceId, "", receiverName, 1)
                setAddressView(keroGetAddress)
            }

        }
        moneyInCheckoutViewModel.getPickupScheduleOption()
        setObservers()
        val terms = getString(R.string.checkout_terms_and_conditions_text)
        val spannableString = SpannableString(terms)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showtnc()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(this@MoneyInCheckoutActivity, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ds.isUnderlineText = false
                ds.typeface = Typeface.DEFAULT_BOLD
            }
        }
        val mTvTnc = findViewById<Typography>(R.id.terms_text) as Typography
        spannableString.setSpan(clickableSpan, 16, 36, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        mTvTnc.text = spannableString
        mTvTnc.isClickable = true
        mTvTnc.movementMethod = LinkMovementMethod.getInstance()
        val tvChangeRecipientAddress = findViewById<Typography>(R.id.tv_change_recipient_address) as Typography
        tvChangeRecipientAddress.setOnClickListener {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalMarketplace.CHECKOUT_ADDRESS_SELECTION)
            intent.putExtra(CheckoutConstant.EXTRA_TYPE_REQUEST, CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN)
            startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
        }
    }

    private fun showtnc() {
        showTnC(MONEYIN_TNC_URL)
    }

    private fun setObservers() {
        moneyInCheckoutViewModel.getPickupScheduleOptionLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    if (!it.data.scheduleDate.isNullOrEmpty())
                        setScheduleBottomSheet(it.data.scheduleDate)
                }
            }
        })
        moneyInCheckoutViewModel.getCourierRatesLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    resetRateAndTime()
                    if (it.data.error?.message.isNullOrEmpty())
                        setCourierRatesBottomSheet(it.data)
                    else {
                        val courierBtn = findViewById<UnifyButton>(R.id.courier_btn)
                        showMessageWithAction(it.data.error?.message, getString(com.tokopedia.abstraction.R.string.title_ok)) {}
                        courierBtn.setOnClickListener { v ->
                            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                                    MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                                    MoneyInGTMConstants.ACTION_CLICK_PILIH_KURIR,
                                    "")
                            showMessageWithAction(it.data.error?.message, getString(com.tokopedia.abstraction.R.string.title_ok)) {}
                        }
                    }
                }
            }
        })
        moneyInCheckoutViewModel.getCheckoutDataLiveData().observe(this, Observer {
            when (it) {
                is Success -> {
                    sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                            MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION_DROP,
                            MoneyInGTMConstants.ACTION_CLICK_PILIH_PEMBAYARAN,
                            MoneyInGTMConstants.SUCCESS)
                    val paymentPassData = PaymentPassData()
                    paymentPassData.redirectUrl = it.data.redirectUrl
                    paymentPassData.transactionId = it.data.parameter.transactionId
                    paymentPassData.paymentId = ""
                    paymentPassData.callbackSuccessUrl = it.data.callbackUrl
                    paymentPassData.callbackFailedUrl = ""
                    paymentPassData.queryString = it.data.queryString
                    val intent = RouteManager.getIntent(this, ApplinkConstInternalPayment.PAYMENT_CHECKOUT)
                    intent.putExtra(PaymentConstant.EXTRA_PARAMETER_TOP_PAY_DATA, paymentPassData)
                    startActivityForResult(intent, PaymentConstant.REQUEST_CODE)
                }
            }
        })
        moneyInCheckoutViewModel.getErrorLiveData().observe(this, Observer {
            when (it) {
                is ScheduleTimeError -> {
                    showMessageWithAction(it.errMsg, getString(com.tokopedia.abstraction.R.string.retry_label)) {
                        moneyInCheckoutViewModel.getPickupScheduleOption()
                    }
                }
                is CourierPriceError -> {
                    showMessageWithAction(it.errMsg, getString(com.tokopedia.abstraction.R.string.retry_label)) {
                        moneyInCheckoutViewModel.getCourierRates(destination)
                    }
                }
                is MutationCheckoutError -> {
                    sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                            MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                            MoneyInGTMConstants.ACTION_CLICK_PILIH_PEMBAYARAN,
                            MoneyInGTMConstants.FAILURE)
                    showMessageWithAction(it.errMsg, getString(com.tokopedia.abstraction.R.string.retry_label)) {
                        moneyInCheckoutViewModel.makeCheckoutMutation(hardwareId, addrId, spId, scheduleTime.minTimeUnix, scheduleTime.maxTimeUnix)
                    }
                }
            }
        })
    }

    private fun setCourierRatesBottomSheet(data: RatesV4.Data) {
        val courierBtn = findViewById<UnifyButton>(R.id.courier_btn)
        spId = data.services[0].products[0].shipper.shipperProduct.id
        val moneyInCourierBottomSheet = MoneyInCourierBottomSheet.newInstance(
                data.services[0].products[0].features.moneyIn,
                data.services[0].products[0].shipper.shipperProduct.description)
        courierBtn.setOnClickListener {
            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                    MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                    when {
                        isCourierSet -> MoneyInGTMConstants.ACTION_CLICK_UBAH_KURIR
                        else -> MoneyInGTMConstants.ACTION_CLICK_PILIH_KURIR
                    },
                    "")
            moneyInCourierBottomSheet.show(supportFragmentManager, "")
        }
        moneyInCourierBottomSheet.setActionListener(this)
        val totalPaymentValue = findViewById<Typography>(R.id.tv_total_payment_value) as Typography
        totalPaymentValue.text = data.services[0].products[0].price.text
    }

    private fun resetRateAndTime() {
        val courierLabel = findViewById<Typography>(R.id.courier_label) as Typography
        val courierPrice = findViewById<Typography>(R.id.courier_price) as Typography
        val courierButton = findViewById<UnifyButton>(R.id.courier_btn) as UnifyButton
        val retrieverTimeLabel = findViewById<Typography>(R.id.retriever_time_label) as Typography
        val retrieverTime = findViewById<Typography>(R.id.retriever_time) as Typography
        val retrieverTimeButton = findViewById<UnifyButton>(R.id.retrival_time_btn) as UnifyButton
        courierLabel.text = getString(R.string.choose_courier)
        courierPrice.hide()
        courierButton.text = getString(R.string.choose)
        MethodChecker.setBackground(courierButton, MethodChecker.getDrawable(this, R.drawable.bg_tradein_button_green))
        courierButton.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        retrieverTimeLabel.text = getString(R.string.retrieval_time)
        retrieverTime.hide()
        retrieverTimeButton.text = getString(R.string.choose)
        MethodChecker.setBackground(retrieverTimeButton, MethodChecker.getDrawable(this, R.drawable.bg_tradein_button_green))
        retrieverTimeButton.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        isCourierSet = false
        isTimeSet = false
    }

    override fun onCourierButtonClick(shipperName: String?, price: String?) {
        val courierLabel = findViewById<Typography>(R.id.courier_label) as Typography
        val courierPrice = findViewById<Typography>(R.id.courier_price) as Typography
        val courierButton = findViewById<UnifyButton>(R.id.courier_btn) as UnifyButton
        courierLabel.text = shipperName
        courierPrice.show()
        courierPrice.text = price
        MethodChecker.setBackground(courierButton, MethodChecker.getDrawable(this, R.drawable.rect_white_rounded_stroke_gray_trade_in))
        courierButton.text = getString(R.string.change_courier)
        courierButton.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        isCourierSet = true
    }

    private fun setScheduleBottomSheet(scheduleDate: ArrayList<ScheduleDate>) {
        val retrievalBtn = findViewById<UnifyButton>(R.id.retrival_time_btn)
        val moneyInScheduledTimeBottomSheet = MoneyInScheduledTimeBottomSheet.newInstance(scheduleDate)
        retrievalBtn.setOnClickListener {
            sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                    MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                    when {
                        isTimeSet -> MoneyInGTMConstants.ACTION_CLICK_UBAH_WAKTU
                        else -> MoneyInGTMConstants.ACTION_CLICK_PILIH_WAKTU_PANGAMBILAN
                    },
                    "")
            moneyInScheduledTimeBottomSheet.show(supportFragmentManager, "")
        }
        moneyInScheduledTimeBottomSheet.setActionListener(this)
    }

    override fun onScheduleButtonClick(scheduleTime: ScheduleDate.ScheduleTime, dateFmt: String) {
        this.scheduleTime = scheduleTime
        val retrieverTimeLabel = findViewById<Typography>(R.id.retriever_time_label) as Typography
        val retrieverTime = findViewById<Typography>(R.id.retriever_time) as Typography
        val retrieverTimeButton = findViewById<UnifyButton>(R.id.retrival_time_btn) as UnifyButton
        retrieverTimeLabel.text = dateFmt
        retrieverTime.show()
        retrieverTime.text = scheduleTime.timeFmt
        MethodChecker.setBackground(retrieverTimeButton, MethodChecker.getDrawable(this, R.drawable.rect_white_rounded_stroke_gray_trade_in))
        retrieverTimeButton.text = getString(R.string.change_time)
        retrieverTimeButton.setTextColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        isTimeSet = true
        sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                MoneyInGTMConstants.ACTION_CLICK_PILIH,
                "$dateFmt - ${scheduleTime.timeFmt}")
    }

    private fun setAddressView(recipientAddress: KeroGetAddress.Data) {
        val tvAddressStatus = findViewById<Typography>(R.id.tv_address_status) as Typography
        val tvAddressName = findViewById<Typography>(R.id.tv_address_name) as Typography
        val tvRecipientName = findViewById<Typography>(R.id.tv_recipient_name) as Typography
        val tvRecipientAddress = findViewById<Typography>(R.id.tv_recipient_address) as Typography
        val tvRecipientPhone = findViewById<Typography>(R.id.tv_recipient_phone) as Typography
        val priceAmount = findViewById<Typography>(R.id.price_amount) as Typography

        if (recipientAddress.status == STATUS_SUCCESS) {
            tvAddressStatus.visibility = View.VISIBLE
        } else {
            tvAddressStatus.visibility = View.GONE
        }
        tvAddressName.text = MethodChecker.fromHtml(recipientAddress.addrName)
        tvRecipientName.text = MethodChecker.fromHtml(recipientAddress.receiverName)
        tvRecipientPhone.text = recipientAddress.phone
        tvRecipientAddress.text = MethodChecker.fromHtml(getFullAddress(recipientAddress))
        priceAmount.text = orderValue

        destination = "${(recipientAddress.district)}|${(recipientAddress.postalCode)}|${(recipientAddress.latitude)},${(recipientAddress.longitude)}"
        addrId = recipientAddress.addrId
        moneyInCheckoutViewModel.getCourierRates(destination)

        val btBuy = findViewById<UnifyButton>(R.id.bt_buy)
        btBuy.setOnClickListener {
            if (isTimeSet && isCourierSet) {
                moneyInCheckoutViewModel.makeCheckoutMutation(hardwareId, addrId, spId, scheduleTime.minTimeUnix, scheduleTime.maxTimeUnix)
            } else if (!isCourierSet) {
                sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                        MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                        MoneyInGTMConstants.ACTION_CLICK_PILIH_PEMBAYARAN,
                        MoneyInGTMConstants.SUCCESS)
                showMessage(getString(R.string.select_shipping))
            } else if (!isTimeSet) {
                sendGeneralEvent(MoneyInGTMConstants.ACTION_CLICK_MONEYIN,
                        MoneyInGTMConstants.CATEGORY_MONEYIN_COURIER_SELECTION,
                        MoneyInGTMConstants.ACTION_CLICK_PILIH_PEMBAYARAN,
                        MoneyInGTMConstants.SUCCESS)
                showMessage(getString(R.string.select_fetch_time))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS) run {
            onResultFromRequestCodeAddressOptions(resultCode, data)
        } else if (requestCode == PaymentConstant.REQUEST_CODE) run {
            onResultFromPayment(resultCode)
        }
    }

    private fun onResultFromRequestCodeAddressOptions(resultCode: Int, data: Intent?) {
        if (resultCode == CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS) {
            when (resultCode) {
                CheckoutConstant.RESULT_CODE_ACTION_SELECT_ADDRESS -> {
                    if (data != null) {
                        val addressModel = data.getParcelableExtra<RecipientAddressModel>(
                                CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA
                        )
                        val recipientAddress = KeroGetAddress.Data(
                                addressModel.id.toInt(),
                                addressModel.addressName,
                                addressModel.addressName,
                                addressModel.addressName,
                                addressModel.cityId.toInt(),
                                addressModel.cityName ?: "",
                                addressModel.countryName ?: "",
                                addressModel.destinationDistrictId.toInt(),
                                addressModel.destinationDistrictName,
                                addressModel.isSelected,
                                addressModel.isSelected,
                                addressModel.isSelected,
                                addressModel.latitude,
                                addressModel.longitude,
                                addressModel.recipientPhoneNumber,
                                addressModel.postalCode,
                                addressModel.provinceId.toInt(),
                                addressModel.provinceName,
                                addressModel.recipientName,
                                addressModel.addressStatus
                        )
                        setAddressView(recipientAddress)
                    }
                }

                else -> finish()
            }
        }
    }


    private fun onResultFromPayment(resultCode: Int) {
        when (resultCode) {
            PaymentConstant.PAYMENT_SUCCESS, Activity.RESULT_OK -> {
                setResult(Activity.RESULT_OK, null)
                finish()
            }
            PaymentConstant.PAYMENT_FAILED -> {
                showMessage(moneyInStringCancelledOrFailed)
            }
            PaymentConstant.PAYMENT_CANCELLED -> {
                showMessage(moneyInStringCancelled)
            }
            else -> {

            }
        }
    }

    private fun getFullAddress(recipientAddress: KeroGetAddress.Data): String {
        return (recipientAddress.address1 + ", "
                + recipientAddress.districtName + ", "
                + recipientAddress.cityName + ", "
                + recipientAddress.provinceName)
    }

    override fun getViewModelType(): Class<MoneyInCheckoutViewModel> {
        return MoneyInCheckoutViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        moneyInCheckoutViewModel = viewModel as MoneyInCheckoutViewModel
    }

    override fun getMenuRes(): Int {
        return -1
    }

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_money_in_checkout
    }

    override fun getRootViewId(): Int {
        return R.id.root_view
    }
}