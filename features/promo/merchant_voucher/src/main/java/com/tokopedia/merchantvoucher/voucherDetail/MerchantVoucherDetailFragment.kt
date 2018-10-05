package com.tokopedia.merchantvoucher.voucherDetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.constant.MerchantVoucherStatusTypeDef
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.model.*
import com.tokopedia.merchantvoucher.voucherDetail.presenter.MerchantVoucherDetailPresenter
import com.tokopedia.merchantvoucher.voucherDetail.presenter.MerchantVoucherDetailView
import com.tokopedia.shop.common.di.ShopCommonModule
import kotlinx.android.synthetic.main.fragment_merchant_voucher_detail.*
import kotlinx.android.synthetic.main.partial_merchant_voucher_detail_loading.*
import javax.inject.Inject

/**
 * Created by hendry on 21/09/18.
 */

class MerchantVoucherDetailFragment: BaseDaggerFragment(),
        MerchantVoucherDetailView {

    var voucherId: Int = 0
    var merchantVoucherViewModel: MerchantVoucherViewModel? = null

    @Inject
    lateinit var presenter: MerchantVoucherDetailPresenter

    companion object {
        const val EXTRA_VOUCHER_ID = "voucher_id"
        const val EXTRA_VOUCHER = "voucher"

        @JvmStatic
        fun createInstance(voucherId: Int, merchantVoucherViewModel: MerchantVoucherViewModel?): Fragment {
            return MerchantVoucherDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_VOUCHER_ID, voucherId)
                    putParcelable(EXTRA_VOUCHER, merchantVoucherViewModel)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherId = arguments!!.getInt(MerchantVoucherDetailFragment.EXTRA_VOUCHER_ID)
        merchantVoucherViewModel = arguments!!.getParcelable(MerchantVoucherDetailFragment.EXTRA_VOUCHER)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_merchant_voucher_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVoucherDetail()
        btnUseVoucher.setOnClickListener {
            //TODO use voucher
        }
    }

    override fun initInjector() {
        activity?.run {
            DaggerMerchantVoucherComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .shopCommonModule(ShopCommonModule())
                    .build()
                    .inject(this@MerchantVoucherDetailFragment)
            presenter.attachView(this@MerchantVoucherDetailFragment)
        }
    }

    override fun onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel: MerchantVoucherViewModel) {
        hideLoading()
        if (merchantVoucherViewModel.bannerUrl.isNullOrEmpty()) {
            ivVoucherBanner.visibility = View.GONE
        } else {
            ImageHandler.loadImageAndCache(ivVoucherBanner, merchantVoucherViewModel.bannerUrl)
            ivVoucherBanner.visibility = View.VISIBLE
        }
        tvVoucherTitle.text = merchantVoucherViewModel.voucherName
        if (merchantVoucherViewModel.minimumSpend <= 0) {
            tvMinTransaction.visibility = View.GONE
            tvMinTransactionLabel.text = merchantVoucherViewModel.getMinSpendLongString(context!!)
        } else {
            tvMinTransaction.visibility = View.VISIBLE
            tvMinTransactionLabel.text = getString(R.string.min_transaction_colon)
            tvMinTransaction.text = merchantVoucherViewModel.getMinSpendAmountString(context!!)
        }
        tvValidThru.text = merchantVoucherViewModel.getValidThruString(context!!)

        if (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_AVAILABLE) {
            vgVoucherStatus.visibility = View.GONE
            btnContainer.visibility = View.VISIBLE
        } else {
            vgVoucherStatus.visibility = View.VISIBLE
            if (merchantVoucherViewModel.status == MerchantVoucherStatusTypeDef.TYPE_IN_USE) {
                tvSeeCart.visibility = View.VISIBLE
            } else {
                tvSeeCart.visibility = View.GONE
            }
            tvVoucherStatus.text = merchantVoucherViewModel.getStatusString(context!!)
            btnContainer.visibility = View.GONE
        }
        if (merchantVoucherViewModel.tnc.isNullOrEmpty()){
            tvTncLabel.visibility = View.GONE
            webViewTnc.visibility = View.GONE
        } else {
            tvTncLabel.visibility = View.VISIBLE
            webViewTnc.visibility = View.VISIBLE
            webViewTnc.loadData(processWebViewHtmlStyle(merchantVoucherViewModel.tnc!!), "text/html; charset=utf-8", "UTF-8")
        }
    }

    fun processWebViewHtmlStyle(html_string: String): String {
        var returnString = ""
        returnString = ("<html><head>"
                + "<style type=\"text/css\">body{font-size:14px; padding:0; margin:0} img{display: inline;max-width: 100% !important ;height:auto !important;} ol,ul{padding-left:15px} ul>li, ol>li{padding-left:0; margin-left:0; margin-bottom:3px;}"
                + "</style></head>"
                + "<body>"
                + html_string
                + "</body></html>")
        return returnString
    }

    override fun onErrorGetMerchantVoucherDetail(e: Throwable) {
        hideLoading()
        NetworkErrorHelper.showEmptyState(context, view,
                ErrorHandler.getErrorMessage(context, e)) { loadVoucherDetail() }
    }

    override fun getScreenName(): String {
        return ""
    }

    private fun loadVoucherDetail(){
        if (merchantVoucherViewModel == null) {
            showLoading()
            presenter.getVoucherDetail(voucherId)
        } else {
            onSuccessGetMerchantVoucherDetail(merchantVoucherViewModel!!)
        }
    }

    private fun showLoading(){
        loadingView.visibility = View.VISIBLE
        vgContent.visibility = View.GONE
    }

    private fun hideLoading(){
        loadingView.visibility = View.GONE
        vgContent.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
