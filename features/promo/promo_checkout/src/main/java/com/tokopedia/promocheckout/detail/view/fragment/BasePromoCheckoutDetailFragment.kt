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
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.util.mapToVariantPromoStackingCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.model.PromoStackingData
import com.tokopedia.promocheckout.common.view.uimodel.DataUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.detail.view.presenter.CheckPromoCodeDetailException
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
        if (isUse) {
            buttonCancel.visibility = View.VISIBLE
            buttonUse.visibility = View.GONE
        } else {
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
            progressBarLoading.visibility = View.GONE
        } else {
            mainView.visibility = View.GONE
            progressBarLoading.visibility = View.VISIBLE
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel) {
        promoCheckoutDetailModel.let {
            ImageHandler.LoadImage(imageBannerPromo, it.imageUrlMobile)
            view?.titlePeriod?.text = promoCheckoutDetailModel.usage?.text
            view?.titleMinTrans?.text = promoCheckoutDetailModel.minimumUsageLabel
            if (TextUtils.isEmpty(promoCheckoutDetailModel.minimumUsage)) {
                view?.textMinTrans?.visibility = View.GONE
                if(TextUtils.isEmpty(promoCheckoutDetailModel.minimumUsageLabel)){
                    view?.titleMinTrans?.visibility = View.GONE
                    view?.imageMinTrans?.visibility = View.GONE
                }else{
                    view?.titleMinTrans?.visibility = View.VISIBLE
                    view?.imageMinTrans?.visibility = View.VISIBLE
                }
            } else {
                view?.titleMinTrans?.visibility = View.VISIBLE
                view?.imageMinTrans?.visibility = View.VISIBLE
                view?.textMinTrans?.visibility = View.VISIBLE
                view?.textMinTrans?.text = promoCheckoutDetailModel.minimumUsage
            }
            textTitlePromo.text = it.title
            if ((it.usage?.activeCountDown ?: 0 > 0 &&
                            it.usage?.activeCountDown ?: 0 < TimerCheckoutWidget.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setTimerUsage(it.usage?.activeCountDown?.toLong() ?: 0)
            } else if ((it.usage?.expiredCountDown ?: 0 > 0 &&
                            it.usage?.expiredCountDown ?: 0 < TimerCheckoutWidget.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY)) {
                setTimerUsage(it.usage?.expiredCountDown?.toLong() ?: 0)
            } else {
                textPeriod.text = it.usage?.usageStr
            }
            webviewTnc.settings.javaScriptEnabled = true
            webviewTnc.loadData(getFormattedHtml(it.tnc), "text/html", "UTF-8")
        }
    }

    private fun setTimerUsage(countDown: Long) {
        view?.timerUsage?.cancel()
        view?.timerUsage?.visibility = View.VISIBLE
        view?.titlePeriod?.visibility = View.GONE
        view?.textPeriod?.visibility = View.GONE
        view?.timerUsage?.expiredTimer = countDown
        view?.timerUsage?.start()
    }

    override fun onErrorValidatePromo(e: Throwable) {
        var message = ErrorHandler.getErrorMessage(activity, e)
        if (e is CheckPromoCodeException) {
            message = e.message
        }
        NetworkErrorHelper.createSnackbarRedWithAction(activity, message, { onClickUse() }).showRetrySnackbar()
    }

    /*override fun onSuccessValidatePromo(dataVoucher: DataVoucher) {
        val intent = Intent()
        val typePromo = if (dataVoucher.isCoupon == PromoData.VALUE_COUPON) PromoData.TYPE_COUPON else PromoData.TYPE_VOUCHER
        val promoData = PromoData(typePromo, dataVoucher.code ?: "",
                dataVoucher.message?.text ?: "", dataVoucher.titleDescription ?: "",
                dataVoucher.cashbackAmount, dataVoucher.message?.state?.mapToStatePromoCheckout()
                ?: TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }*/

    override fun onSuccessValidatePromoStacking(data: DataUiModel) {
        val intent = Intent()
        val variant = "global"
        val typePromo = if (data.isCoupon == PromoStackingData.VALUE_COUPON) PromoStackingData.TYPE_COUPON else PromoStackingData.TYPE_VOUCHER
        val promoStackingData = PromoStackingData(typePromo, data.codes[0],
                data.message.text, data.titleDescription,
                data.cashbackWalletAmount, data.message.state.mapToStatePromoStackingCheckout(),
                variant.mapToVariantPromoStackingCheckout())
        intent.putExtra(EXTRA_PROMO_DATA, promoStackingData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onErrorCancelPromo(e: Throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(activity, ErrorHandler.getErrorMessage(activity, e))
    }

    /*override fun onSuccessCancelPromo() {
        isUse = false
        validateButton()
        val intent = Intent()
        val promoData = PromoData(PromoData.TYPE_COUPON,state =TickerCheckoutView.State.EMPTY)
        intent.putExtra(EXTRA_PROMO_DATA, promoData)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }*/

    override fun onSuccessCancelPromoStacking() {
        isUse = false
        validateButton()
        val intent = Intent()
        val promoStackingData = PromoStackingData(PromoData.TYPE_COUPON,state =TickerPromoStackingCheckoutView.State.EMPTY)
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
            NetworkErrorHelper.showRedCloseSnackbar(activity,e.message)
            return
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
