package com.tokopedia.merchantvoucher.voucherDetail

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.merchantvoucher.common.di.DaggerMerchantVoucherComponent
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.voucherDetail.presenter.MerchantVoucherDetailPresenter
import com.tokopedia.merchantvoucher.voucherDetail.presenter.MerchantVoucherDetailView
import com.tokopedia.shop.common.di.ShopCommonModule
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
        //TODO show empty state error full page
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
        //TODO show loading shimmering
    }

    private fun hideLoading(){
        //TODO hide loading shimmering
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

}
