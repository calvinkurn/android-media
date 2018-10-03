package com.tokopedia.merchantvoucher.voucherList

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.merchantvoucher.R
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import kotlinx.android.synthetic.main.fragment_merchant_voucher_list_widget.*

/**
    [Title]                                [See All]
    +----------+ +-----+  +----------+ +-----+  +---
    | VOUCHR_A +-+     |  | VOUCHR_B +-+     |  |
    | *20Rb     |  USE |  | *20Rb     |  USE |  |
    |          +-+     |  |          +-+     |  |
    +----------+ +-----+  +----------+ +-----+  +---
 */
class MerchantVoucherListWidgetFragment : MerchantVoucherListFragment() {

    var titleString:String? = null

    companion object {

        @JvmStatic
        fun createInstance(shopId: String): Fragment {
            return MerchantVoucherListWidgetFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SHOP_ID, shopId)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_merchant_voucher_list_widget, container, false)
    }

    override fun callInitialLoadAutomatically() = false

    override fun hasInitialSwipeRefresh() = false

    override fun getRecyclerView(view: View?): RecyclerView {
        val recyclerView: RecyclerView = super.getRecyclerView(view)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setLayoutManager(linearLayoutManager)
        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.visibility = View.GONE
        super.onViewCreated(view, savedInstanceState)
        //TODO see all button setonClick
    }

    override fun getShopInfo() {
        // no op, no need get shop info in widget
    }

    override fun onSuccessGetMerchantVoucherList(merchantVoucherViewModelList: ArrayList<MerchantVoucherViewModel>) {
        super.onSuccessGetMerchantVoucherList(merchantVoucherViewModelList);
        tvTitle.text = if (titleString.isNullOrEmpty()) getString(R.string.merchant_voucher) else titleString
        view?.visibility = View.VISIBLE
        //TODO see all visibility
    }

    override fun onErrorGetMerchantVoucherList(e: Throwable) {
        // TODO show snackbar/popup (since it is widget)
        view?.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //TODO check on widget
//        when (requestCode) {
//            REQUEST_CODE_MERCHANT_DETAIL -> if (resultCode == Activity.RESULT_OK) {
//            }
//            else -> super.onActivityResult(requestCode, resultCode, data)
//        }
    }

}
