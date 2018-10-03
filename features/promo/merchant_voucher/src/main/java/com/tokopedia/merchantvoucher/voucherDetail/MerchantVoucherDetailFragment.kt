package com.tokopedia.merchantvoucher.voucherDetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
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

    @Inject
    lateinit var presenter: MerchantVoucherDetailPresenter

    companion object {
        const val EXTRA_VOUCHER_ID = "voucher_id"

        @JvmStatic
        fun createInstance(voucherId: Int): Fragment {
            return MerchantVoucherDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_VOUCHER_ID, voucherId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        voucherId = arguments!!.getInt(MerchantVoucherDetailFragment.EXTRA_VOUCHER_ID)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_merchant_voucher_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVoucherDetail()
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
        //TODO show merchant voucher detail
        hideLoading()
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
        showLoading()
        //TODO presenter load voucher detail
        presenter.getVoucherDetail(voucherId)
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
