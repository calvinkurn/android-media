package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.LayoutScanQrCodeItemBinding
import com.tokopedia.buyerorder.detail.data.RedeemVoucherModel
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage

/**
 * created by @bayazidnasir on 25/3/2022
 */

class RedeemVoucherAdapter( private val items:List<RedeemVoucherModel>): RecyclerView.Adapter<RedeemVoucherAdapter.ViewHolder>() {

    private var onCopiedListener: (() -> Unit)? = null

    fun setOnCopiedListener(onCopiedListener: () -> Unit){
        this.onCopiedListener = onCopiedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutScanQrCodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: LayoutScanQrCodeItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RedeemVoucherModel){
            with(binding){
                tvVoucherCode.text = data.voucherCode
                tvPoweredBy.text = data.poweredBy
                ivQrCode.loadImage(data.qrCodeUrl)
                tvIsRedeem.showWithCondition(data.isRedeem)
                //tvCopyCode.showWithCondition(data.isRedeem)

                tvCopyCode.setOnClickListener {
                    onCopiedListener?.invoke()
                    tvCopyCode.text = itemView.context.getString(R.string.deals_label_copied)
                }
            }
        }
    }
}
