package com.tokopedia.promocheckout.detail.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.constant.ErrorNetMessage
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.CheckPromoCodeException
import com.tokopedia.promocheckout.common.util.*
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.ClashingInfoDetailUiModel
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.detail.model.detailmodel.CouponDetailsResponse
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail
import com.tokopedia.promocheckout.detail.view.presenter.CheckPromoCodeDetailException
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailContract
import com.tokopedia.promocheckout.widget.TimerCheckoutWidget
import com.tokopedia.promocheckout.widget.TimerPromoCheckout
import kotlinx.android.synthetic.main.fragment_checkout_detail_layout.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.view.*

abstract class BasePromoCheckoutDetailFragment : BottomSheetDialogFragment(), PromoCheckoutDetailContract.View {

    var isLoadingFinished = false
    var codeCoupon = ""
    var slug="TESTCOUPON"
    var catalog_id=158
    open var isUse = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_detail_layout, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL,R.style.TransparentBottomSheetDialogTheme)
    }
    private lateinit var timerUsage: TimerPromoCheckout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        validateButton()
        buttonUse.setOnClickListener { onClickUse() }
        timerUsage = TimerPromoCheckout()
    }

    protected fun validateButton() {
        if (isUse) {
            buttonUse.visibility = View.GONE
        } else {
            buttonUse.visibility = View.VISIBLE
        }
    }

    abstract fun onClickUse()
    abstract fun onClickCancel()

    protected open fun loadData() {
        isLoadingFinished = false
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel) {
        promoCheckoutDetailModel.let {
            ImageHandler.LoadImage(imageBannerPromo, it.imageUrlMobile)
           // view?.titlePeriod?.text = promoCheckoutDetailModel.usage?.text
            view?.titleMinTrans?.text = promoCheckoutDetailModel.minimumUsageLabel
            if (TextUtils.isEmpty(promoCheckoutDetailModel.minimumUsage)) {
                view?.textMinTrans?.visibility = View.GONE
                if (TextUtils.isEmpty(promoCheckoutDetailModel.minimumUsageLabel)) {
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
                view?.textMinTrans?.text = promoCheckoutDetailModel.minimumUsage
            }
            textTitlePromo?.text = it.title
          /*  hideTimerView()
            if ((it.usage?.activeCountDown ?: 0 > 0 &&
                            it.usage?.activeCountDown ?: 0 < TimerPromoCheckout.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setActiveTimerUsage(it.usage?.activeCountDown?.toLong() ?: 0)
            } else if ((it.usage?.expiredCountDown ?: 0 > 0 &&
                            it.usage?.expiredCountDown ?: 0 < TimerPromoCheckout.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setExpiryTimerUsage(it.usage?.expiredCountDown?.toLong() ?: 0)
            }
            view?.textPeriod?.text = it.usage?.usageStr*/
            webviewTnc?.settings?.javaScriptEnabled = true
            webviewTnc?.loadData(getFormattedHtml(it.tnc), "text/html", "UTF-8")
           // enableOrDisableViews(it)
        }
    }


    private fun showTimerView(){
        view?.timerUsage?.visibility = View.VISIBLE
        view?.titlePeriod?.visibility = View.GONE
        view?.textPeriod?.visibility = View.GONE

    }

    private fun hideTimerView(){
        view?.timerUsage?.visibility = View.GONE
        view?.titlePeriod?.visibility = View.VISIBLE
        view?.textPeriod?.visibility = View.VISIBLE

    }

    private fun enableOrDisableViews(item: HachikoCatalogDetail) {
    /*    if (item.usage?.activeCountDown!! > 0 || item.usage?.expiredCountDown!! <= 0) {
            disableViews()
        } else {
            enableViews()
        }*/
    }


    private fun disableViews() {
        imageMinTrans?.setImageResource(R.drawable.ic_tp_rp_grey)
        imagePeriod?.setImageResource(R.drawable.ic_tp_time)
        buttonUse?.isEnabled = false
    }

    private fun enableViews() {
        imageMinTrans?.setImageResource(R.drawable.ic_voucher_promo_green)
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

    override fun onErrorValidatePromo(e: Throwable) {
        var message = ErrorHandler.getErrorMessage(activity, e)
        if (e is CheckPromoCodeException) {
            message = e.message
        }
        NetworkErrorHelper.createSnackbarRedWithAction(activity, message, { onClickUse() }).showRetrySnackbar()
    }

    override fun onErrorValidatePromoStacking(e: Throwable) {
        var message = ErrorHandler.getErrorMessage(activity, e)
        if (e is CheckPromoCodeException) {
            message = e.message
        }
        NetworkErrorHelper.showRedCloseSnackbar(activity, message)
        setDisabledButtonUse()
    }

    override fun onClashCheckPromo(clasingInfoDetailUiModel: ClashingInfoDetailUiModel) {

    }

    override fun onSuccessValidatePromoStacking(data: DataUiModel) {
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

    override fun onSuccessCancelPromoStacking() {
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
    }

    override fun hideLoading() {
        succesLoad()
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
    }

    override fun onDestroyView() {
        view?.timerUsage?.cancel()
        timerUsage?.cancel()
        super.onDestroyView()
    }
}
