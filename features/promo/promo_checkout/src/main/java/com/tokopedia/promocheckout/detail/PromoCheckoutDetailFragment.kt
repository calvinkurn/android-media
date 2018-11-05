package com.tokopedia.promocheckout.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.promocheckout.R
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailModule
import com.tokopedia.promocheckout.detail.model.PromoCheckoutDetailModel
import kotlinx.android.synthetic.main.fragment_checkout_detail_layout.*
import kotlinx.android.synthetic.main.include_period_tnc_promo.*
import javax.inject.Inject

class PromoCheckoutDetailFragment : BaseDaggerFragment(), PromoCheckoutDetailContract.View {

    var isLoadingFinished = false
    var codeCoupon = ""

    @Inject
    lateinit var promoCheckoutDetailPresenter: PromoCheckoutDetailPresenter

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        codeCoupon = arguments?.getString(EXTRA_KUPON_CODE, "") ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_checkout_detail_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        validateViewLoading()
        loadData()
        buttonUse.setOnClickListener {  }
    }

    private fun loadData() {
        isLoadingFinished = false
        promoCheckoutDetailPresenter.getDetailPromo(codeCoupon, resources)
    }

    private fun validateViewLoading() {
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
        succesLoad()
        promoCheckoutDetailModel?.let {
            ImageHandler.LoadImage(imageBannerPromo, it.imageUrlMobile)
            textPeriod.text = it.minimumUsage
            textMinTrans.text = it.minimumUsage
            if(!TextUtils.isEmpty(it.usage?.text)) {
                textCashbackPotention.visibility = View.VISIBLE
                textCashbackPotention.text = it.usage?.text
            }else{
                textCashbackPotention.visibility = View.GONE
            }
            webviewTnc.settings.javaScriptEnabled = true
            webviewTnc.loadData(getFormattedHtml(it.description), "text/html", "UTF-8")
        }
    }

    private fun getFormattedHtml(content: String?): String {
        return "<html><head><style>li{ font-size: 10pt; color: 8A000000; }</style></head><body>$content</body></html>"
    }

    override fun onErroGetDetail(e: Throwable) {
        succesLoad()
        NetworkErrorHelper.showEmptyState(activity, view, ErrorHandler.getErrorMessage(activity, e), { loadData() })
    }

    fun succesLoad() {
        isLoadingFinished = true
        validateViewLoading()
    }

    override fun initInjector() {
        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .promoCheckoutDetailModule(PromoCheckoutDetailModule())
                .build()
                .inject(this)
        promoCheckoutDetailPresenter.attachView(this)
    }

    override fun onDestroy() {
        promoCheckoutDetailPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_KUPON_CODE = "EXTRA_KUPON_CODE"

        fun createInstance(codeCoupon: String): PromoCheckoutDetailFragment {
            val promoCheckoutDetailFragment = PromoCheckoutDetailFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_KUPON_CODE, codeCoupon)
            promoCheckoutDetailFragment.arguments = bundle
            return promoCheckoutDetailFragment
        }
    }
}
