package com.tokopedia.buyerorder.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
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

    private var onCopiedListener: ((String) -> Unit)? = null

    fun setOnCopiedListener(onCopiedListener: (String) -> Unit){
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
                tvLabelPoweredBy.showWithCondition(!data.poweredBy.isNullOrEmpty())
                ivQrCode.loadImage(data.qrCodeUrl)
                tvIsRedeem.showWithCondition(data.statusLabel.isNotEmpty())
                tvCopyCode.showWithCondition(data.statusLabel.isEmpty())

                tvCopyCode.setOnClickListener {
                    onCopiedListener?.invoke(data.voucherCode)
                    tvCopyCode.text = itemView.context.getString(R.string.deals_label_copied)
                    tvCopyCode.setTextColor(ResourcesCompat.getColor(itemView.resources, com.tokopedia.unifyprinciples.R.color.Unify_NN0_68, null))
                }
            }
        }
    }
}
