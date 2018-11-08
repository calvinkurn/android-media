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
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.common.domain.model.DataVoucher
import com.tokopedia.promocheckout.common.util.EXTRA_PROMO_DATA
import com.tokopedia.promocheckout.common.util.mapToStatePromoCheckout
import com.tokopedia.promocheckout.common.view.model.PromoData
import com.tokopedia.promocheckout.common.view.widget.TickerCheckoutView
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import com.tokopedia.promocheckout.detail.view.presenter.PromoCheckoutDetailContract
import kotlinx.android.synthetic.main.fragment_checkout_detail_layout.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.*

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
    override fun onSuccessGetDetailPromo(promoCheckoutDetailModel: PromoCheckoutDetailModel?) {
        promoCheckoutDetailModel?.let {
            ImageHandler.LoadImage(imageBannerPromo, it.imageUrlMobile)
            textPeriod.text = it.minimumUsage
            textMinTrans.text = it.minimumUsage
            if (!TextUtils.isEmpty(it.usage?.text)) {
                textCashbackPotention.visibility = View.VISIBLE
                textCashbackPotention.text = it.usage?.text
            } else {
                textCashbackPotention.visibility = View.GONE
            }
            webviewTnc.settings.javaScriptEnabled = true
            webviewTnc.loadData(getFormattedHtml(it.description), "text/html", "UTF-8")
        }
    }

    override fun onErrorValidatePromo(e: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, ErrorHandler.getErrorMessage(activity, e), { onClickUse() })
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
        NetworkErrorHelper.showEmptyState(activity, view, ErrorHandler.getErrorMessage(activity, e), { loadData() })
    }

    fun succesLoad() {
        isLoadingFinished = true
        validateViewLoading()
    }


}
