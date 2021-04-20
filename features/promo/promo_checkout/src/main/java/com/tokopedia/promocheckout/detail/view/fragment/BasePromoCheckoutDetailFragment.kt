package com.tokopedia.promocheckout.detail.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.network.constant.ErrorNetMessage
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.analytics.PromoCheckoutAnalytics.Companion.promoCheckoutAnalytics
import com.tokopedia.promocheckout.common.analytics.FROM_CART
import com.tokopedia.promocheckout.common.analytics.TrackingPromoCheckoutUtil
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException
import com.tokopedia.promocheckout.common.util.*
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.detail.view.presenter.CheckPromoCodeDetailException
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailContract
import com.tokopedia.promocheckout.widget.TimerCheckoutWidget
import com.tokopedia.promocheckout.widget.TimerPromoCheckout
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.fragment_checkout_detail_layout.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.view.*
import timber.log.Timber
import javax.inject.Inject

abstract class BasePromoCheckoutDetailFragment : Fragment(), PromoCheckoutDetailContract.View {

    @Inject
    lateinit var trackingPromoCheckoutUtil: TrackingPromoCheckoutUtil

    var isLoadingFinished = false
    var codeCoupon = ""
    var pageTracking: Int = 1
    open var isUse = false

    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageTracking = arguments?.getInt(PAGE_TRACKING, 1) ?: 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_detail_layout, container, false)
    }

    private lateinit var timerUsage: TimerPromoCheckout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        validateViewLoading()
        validateButton()
        buttonUse.setOnClickListener { onClickUse() }
        buttonCancel.setOnClickListener { onClickCancel() }
        timerUsage = TimerPromoCheckout()
    }

    protected fun validateButton() {
        if (showActionButtonUse()) {
            if (isUse) {
                buttonCancel.visibility = View.VISIBLE
                buttonUse.visibility = View.GONE
            } else {
                buttonCancel.visibility = View.GONE
                buttonUse.visibility = View.VISIBLE
            }
            containerButton.visibility = View.VISIBLE
        } else {
            containerButton.visibility = View.GONE
        }
    }

    open protected fun showActionButtonUse(): Boolean {
        return true
    }

    abstract fun onClickUse()

    protected open fun onClickCancel() {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickCancelPromoCoupon(codeCoupon)
        } else {
            trackingPromoCheckoutUtil.checkoutClickCancelPromoCoupon(codeCoupon)
        }
    }

    protected open fun loadData() {
        isLoadingFinished = false
    }

    protected fun validateViewLoading() {
        if (isLoadingFinished) {
            mainView.visibility = View.VISIBLE
            progressBarLoading.visibility = View.GONE
        } else {
            mainView.visibility = View.GONE
            progressBarLoading.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel?) {
        promoCheckoutDetailModel?.let {
            ImageHandler.LoadImage(imageBannerPromo, it.imageUrlMobile)
            view?.titlePeriod?.text = it.usage.text
            view?.titleMinTrans?.text = it.minimumUsageLabel
            if (TextUtils.isEmpty(it.minimumUsage)) {
                view?.textMinTrans?.visibility = View.GONE
                if (TextUtils.isEmpty(it.minimumUsageLabel)) {
                    view?.titleMinTrans?.visibility = View.GONE
                    view?.imageMinTrans?.visibility = View.GONE
                } else {
                    view?.titleMinTrans?.visibility = View.VISIBLE
                    view?.imageMinTrans?.visibility = View.VISIBLE
                }
            } else {
                view?.titleMinTrans?.visibility = View.VISIBLE
                view?.imageMinTrans?.visibility = View.VISIBLE
                view?.textMinTrans?.visibility = View.VISIBLE
                view?.textMinTrans?.text = it.minimumUsage
            }
            textTitlePromo?.text = it.title
            hideTimerView()
            if ((it.usage.activeCountDown > 0 &&
                            it.usage.activeCountDown < TimerPromoCheckout.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setActiveTimerUsage(it.usage.activeCountDown.toLong())
            } else if ((it.usage.expiredCountDown > 0 &&
                            it.usage.expiredCountDown < TimerPromoCheckout.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setExpiryTimerUsage(it.usage.expiredCountDown.toLong())
            }
            view?.textPeriod?.text = it.usage.usageStr
            webviewTnc?.settings?.javaScriptEnabled = true
            webviewTnc?.loadData(getFormattedHtml(it.tnc), "text/html", "UTF-8")
            enableOrDisableViews(it)
        }
    }


    private fun showTimerView() {
        view?.timerUsage?.visibility = View.VISIBLE
        view?.titlePeriod?.visibility = View.GONE
        view?.textPeriod?.visibility = View.GONE

    }

    private fun hideTimerView() {
        view?.timerUsage?.visibility = View.GONE
        view?.titlePeriod?.visibility = View.VISIBLE
        view?.textPeriod?.visibility = View.VISIBLE

    }

    private fun enableOrDisableViews(item: PromoCheckoutDetailModel) {
        if (item.usage?.activeCountDown!! > 0 || item.usage?.expiredCountDown!! <= 0) {
            disableViews()
        } else {
            enableViews()
        }
    }


    private fun disableViews() {
        imageMinTrans?.setImageResource(R.drawable.ic_promo_rp_grey)
        imagePeriod?.setImageResource(R.drawable.ic_promo_time)
        buttonUse?.isEnabled = false
    }

    private fun enableViews() {
        imageMinTrans?.setImageResource(com.tokopedia.promocheckout.common.R.drawable.ic_voucher_promo_green)
        imagePeriod?.setImageResource(R.drawable.ic_period_promo_green)
        buttonUse?.isEnabled = true
    }


    private fun setActiveTimerUsage(countDown: Long) {
        timerUsage?.cancel()
        timerUsage?.expiredTimer = countDown
        buttonUse?.isEnabled = false
        timerUsage?.listener = object : TimerPromoCheckout.Listener {
            override fun onTick(l: Long) {
                buttonUse?.text = timerUsage.formatMilliSecondsToTime(l * 1000)
            }

            override fun onFinishTick() {
                buttonUse?.text = getString(R.string.promo_label_use)
                buttonUse?.isEnabled = true
                enableViews()
            }
        }
        timerUsage?.start()
    }

    private fun setExpiryTimerUsage(countDown: Long) {
        view?.timerUsage?.cancel()
        showTimerView()
        view?.timerUsage?.expiredTimer = countDown
        view?.timerUsage?.listener = object : TimerCheckoutWidget.Listener {
            override fun onTick(l: Long) {}
            override fun onFinishTick() {
                buttonUse?.text = getString(R.string.promo_label_coupon_not_eligible)
                hideTimerView()
                disableViews()
            }
        }
        view?.timerUsage?.start()
    }

    override fun onErrorCheckPromo(e: Throwable) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponFailed()
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponFailed()
        }

        var message = ErrorHandler.getErrorMessage(activity, e)
        if (e is CheckPromoCodeException) {
            message = e.message
        }
        NetworkErrorHelper.createSnackbarRedWithAction(activity, message) { onClickUse() }.showRetrySnackbar()
    }

    override fun onErrorCheckPromoStacking(e: Throwable) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponFailed()
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponFailed()
        }
        var message = ErrorHandler.getErrorMessage(activity, e)
        if (e is CheckPromoCodeException || e is MessageErrorException) {
            message = e.message
        }
        if (message.equals(resources.getString(R.string.promo_phone_verification_message)) || message.equals(R.string.promo_phone_verification_message_v2)) {
            val variant = RemoteConfigInstance.getInstance().abTestPlatform.getString(AB_TEST_PHONE_VERIFICATION_KEY, AB_TESTING_CTA_VARIANT_A)

            if (variant.isNotEmpty() && variant == AB_TESTING_CTA_VARIANT_A) {
                buttonUse.setOnClickListener {
                    openPhoneVerificationBottomSheet()
                }
            }
        } else {
            NetworkErrorHelper.showRedCloseSnackbar(activity, message)
            setDisabledButtonUse()
        }
    }

    private fun openPhoneVerificationBottomSheet() {
        val view = LayoutInflater.from(context).inflate(R.layout.promo_phoneverification_bottomsheet, null, false)
        val closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(context)
        closeableBottomSheetDialog.setCustomContentView(view, "", false)
        val btnVerifikasi = view.findViewById<UnifyButton>(R.id.btn_verifikasi)
        val btnCancel = view.findViewById<AppCompatImageView>(R.id.cancel_verifikasi)
        btnVerifikasi.setOnClickListener {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PHONE)
            startActivityForResult(intent, REQUEST_CODE_VERIFICATION_PHONE)
            promoCheckoutAnalytics.clickVerifikasai()
            closeableBottomSheetDialog.cancel()
        }

        btnCancel.setOnClickListener {
            closeableBottomSheetDialog.cancel()
            promoCheckoutAnalytics.clickCancelVerifikasi()
        }

        closeableBottomSheetDialog.show()
    }

    override fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel) {

    }

    override fun onSuccessCheckPromo(data: DataUiModel) {
        if (pageTracking == FROM_CART) {
            trackingPromoCheckoutUtil.cartClickUsePromoCouponSuccess(data.codes[0])
        } else {
            trackingPromoCheckoutUtil.checkoutClickUsePromoCouponSuccess(data.codes[0])
        }

        val intent = Intent()
        val variant = "global"
        val typePromo = if (data.isCoupon == PromoStackingData.VALUE_COUPON) PromoStackingData.TYPE_COUPON else PromoStackingData.TYPE_VOUCHER
        val promoStackingData = PromoStackingData(
                typePromo = typePromo,
                promoCode = data.codes[0],
                description = data.message.text,
                title = data.titleDescription,
                counterLabel = "",
                amount = data.cashbackWalletAmount,
                state = data.message.state.mapToStatePromoStackingCheckout(),
                variant = variant.mapToVariantPromoStackingCheckout(),
                trackingDetailUiModels = data.trackingDetailUiModel)
        intent.putExtra(EXTRA_PROMO_DATA, promoStackingData)
        intent.putExtra(EXTRA_INPUT_TYPE, INPUT_TYPE_COUPON)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onErrorCancelPromo(e: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onSuccessCancelPromo() {
        isUse = false
        validateButton()
        val intent = Intent()
        val promoStackingData = PromoStackingData(PromoStackingData.TYPE_COUPON, state = TickerPromoStackingCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoStackingData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun showLoading() {
        isLoadingFinished = false
        validateViewLoading()
    }

    override fun hideLoading() {
        succesLoad()
    }

    override fun hideProgressLoading() {
        progressDialog?.hide()
    }

    override fun showProgressLoading() {
        try {
            progressDialog?.show()
        } catch (exception: UnsupportedOperationException) {
            Timber.d(exception)
        }
    }

    private fun getFormattedHtml(content: String?): String {
        return getString(R.string.promo_label_html_tnc_promo, content)
    }

    override fun onErroGetDetail(e: Throwable) {
        var message: String = ErrorHandler.getErrorMessage(activity, e)
        if (e is CheckPromoCodeException) {
            message = e.message ?: ErrorNetMessage.MESSAGE_ERROR_DEFAULT
            setDisabledButtonUse()
        } else if (e is CheckPromoCodeDetailException) {
            setDisabledButtonUse()
            NetworkErrorHelper.showRedCloseSnackbar(activity, e.message)
            return
        }
        NetworkErrorHelper.showEmptyState(activity, view, message, { loadData() })
    }

    private fun setDisabledButtonUse() {
        validateButton()
        buttonUse.isEnabled = false
        buttonUse.text = getString(R.string.promo_label_use)
    }

    fun succesLoad() {
        isLoadingFinished = true
        validateViewLoading()
    }

    override fun onDestroyView() {
        view?.timerUsage?.cancel()
        timerUsage?.cancel()
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_VERIFICATION_PHONE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        onClickUse()
                    }
                    Activity.RESULT_CANCELED -> {
                        super.onActivityResult(requestCode, resultCode, data)
                    }
                }
            }
        }
    }

    companion object {
        val AB_TESTING_CTA_VARIANT_A = "CTA Phone Verify 2"
        val AB_TEST_PHONE_VERIFICATION_KEY = "CTA Phone Verify 2"
        val REQUEST_CODE_VERIFICATION_PHONE = 301
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"
        val EXTRA_IS_USE = "EXTRA_IS_USE"
        val PAGE_TRACKING = "PAGE_TRACKING"
    }
}
