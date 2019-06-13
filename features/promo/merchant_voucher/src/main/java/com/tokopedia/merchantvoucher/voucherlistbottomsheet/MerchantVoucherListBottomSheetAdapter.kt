package com.tokopedia.merchantvoucher.voucherlistbottomsheet

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherViewUsed

/**
 * Created by fwidjaja on 10/03/19.
 */
class MerchantVoucherListBottomSheetAdapter(onMerchantVoucherViewListener: MerchantVoucherViewUsed.OnMerchantVoucherViewListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        return MerchantVoucherListBottomSheetViewHolder(view, onMerchantVoucherViewListener)
    }

    override fun getItemCount(): Int {
        return merchantBottomSheetViewModelList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MerchantVoucherListBottomSheetViewHolder).bind(merchantBottomSheetViewModelList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return MerchantVoucherListBottomSheetViewHolder.ITEM_MERCHANT_VOUCHER
    }
}