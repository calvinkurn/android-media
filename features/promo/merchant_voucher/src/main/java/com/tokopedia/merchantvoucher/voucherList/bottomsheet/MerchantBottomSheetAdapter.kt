package com.tokopedia.merchantvoucher.voucherList.bottomsheet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherViewUsed

/**
 * Created by fwidjaja on 10/03/19.
 */
class MerchantBottomSheetAdapter(onMerchantVoucherViewListener: MerchantVoucherViewUsed.OnMerchantVoucherViewListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onMerchantVoucherViewListener: MerchantVoucherViewUsed.OnMerchantVoucherViewListener? = null
    private var merchantBottomSheetViewModelList = ArrayList<MerchantVoucherViewModel>()

    init {
        this.onMerchantVoucherViewListener = onMerchantVoucherViewListener
    }

    open fun setViewModelList(merchantBottomSheetViewModelList: ArrayList<MerchantVoucherViewModel>) {
        this.merchantBottomSheetViewModelList = merchantBottomSheetViewModelList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return MerchantBottomSheetViewHolder(view, onMerchantVoucherViewListener)
    }

    override fun getItemCount(): Int {
        return merchantBottomSheetViewModelList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*for (merchantVoucherViewModel in merchantBottomSheetViewModelList) {
            (holder as MerchantBottomSheetViewHolder).bind(merchantVoucherViewModel)
        }*/

        (holder as MerchantBottomSheetViewHolder).bind(merchantBottomSheetViewModelList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return MerchantBottomSheetViewHolder.ITEM_MERCHANT_VOUCHER
    }
}