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
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailContract
import com.tokopedia.promocheckout.widget.TimerCheckoutWidget
import kotlinx.android.synthetic.main.fragment_checkout_detail_layout.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.view.*

abstract class BasePromoCheckoutDetailFragment : BaseDaggerFragment(), PromoCheckoutDetailContract.View {

    var isLoadingFinished = false
    var codeCoupon = ""
    open var isUse = false

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        validateViewLoading()
        validateButton()
        buttonUse.setOnClickListener { onClickUse() }
        buttonCancel.setOnClickListener { onClickCancel() }
    }

    protected fun validateButton() {
        if(isUse){
            buttonCancel.visibility = View.VISIBLE
            buttonUse.visibility = View.GONE
        }else{
            buttonCancel.visibility = View.GONE
            buttonUse.visibility = View.VISIBLE
        }
    }

    abstract fun onClickUse()
    abstract fun onClickCancel()

    protected open fun loadData() {
        isLoadingFinished = false
    }

    protected fun validateViewLoading() {
        if (isLoadingFinished) {
            mainView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        } else {
            mainView.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel) {
        promoCheckoutDetailModel.let {
            ImageHandler.LoadImage(imageBannerPromo, it.imageUrlMobile)
            textMinTrans.text = it.minimumUsage
            if ((it.usage?.activeCountDown ?: 0 > 0 &&
                            it.usage?.activeCountDown ?: 0 < TimerCheckoutWidget.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setTimerUsage(it.usage?.activeCountDown?.toLong() ?: 0)
            } else if ((it.usage?.expiredCountDown ?: 0 > 0 &&
                            it.usage?.expiredCountDown ?: 0 < TimerCheckoutWidget.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setTimerUsage(it.usage?.expiredCountDown?.toLong() ?: 0)
            }else{
                textPeriod.text = it.usage?.usageStr
            }
            webviewTnc.settings.javaScriptEnabled = true
            webviewTnc.loadData(getFormattedHtml(it.description), "text/html", "UTF-8")
        }
    }

    private fun setTimerUsage(countDown: Long) {
        view?.timerUsage?.cancel()
        view?.timerUsage?.visibility = View.VISIBLE
        view?.containerUsageDate?.visibility = View.GONE
        view?.timerUsage?.expiredTimer = countDown
        view?.timerUsage?.start()
    }

    override fun onErrorValidatePromo(e: Throwable) {
        var message = ErrorHandler.getErrorMessage(activity, e)
        if(e is CheckPromoCodeException){
            message = e.message
        }
        NetworkErrorHelper.createSnackbarRedWithAction(activity, message, { onClickUse() }).showRetrySnackbar()
    }

    override fun onSuccessValidatePromo(dataVoucher: DataVoucher) {
        val intent = Intent()
        val typePromo = if(dataVoucher.isCoupon == PromoData.TYPE_COUPON ) PromoData.TYPE_COUPON else PromoData.TYPE_VOUCHER
        val promoData = PromoData(typePromo, dataVoucher.code?:"",
                dataVoucher.message?.text?:"", dataVoucher.titleDescription?:"",
                dataVoucher.cashbackAmount, dataVoucher.message?.state?.mapToStatePromoCheckout()?: TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onErrorCancelPromo(e: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    override fun onSuccessCancelPromo() {
        isUse = false
        validateButton()
    }

    override fun showLoading() {
        isLoadingFinished = false
        validateViewLoading()
    }

    override fun hideLoading() {
        succesLoad()
    }

    private fun getFormattedHtml(content: String?): String {
        return "<html><head><style>li{ font-size: 10pt; color: 8A000000; }</style></head><body>$content</body></html>"
    }

    override fun onErroGetDetail(e: Throwable) {
        var message : String = ErrorHandler.getErrorMessage(activity, e)
        if(e is CheckPromoCodeException){
            message = e.message?:ErrorNetMessage.MESSAGE_ERROR_DEFAULT
            setDisabledButtonUse()
        }
        NetworkErrorHelper.showEmptyState(activity, view, message, { loadData() })
    }

    private fun setDisabledButtonUse() {
        validateButton()
        buttonUse.isEnabled = false
        buttonUse.text = getString(R.string.promo_label_coupon_not_eligible)
    }

    fun succesLoad() {
        isLoadingFinished = true
        validateViewLoading()
    }


}
