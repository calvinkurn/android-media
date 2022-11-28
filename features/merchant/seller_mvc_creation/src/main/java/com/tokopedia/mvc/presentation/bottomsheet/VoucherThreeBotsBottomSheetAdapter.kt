package com.tokopedia.mvc.presentation.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemThreeDotsMenuBinding
import com.tokopedia.unifyprinciples.Typography

class VoucherThreeBotsBottomSheetAdapter(val context: FragmentActivity?, private val entryPoint: MVCBottomSheetType) :
    RecyclerView.Adapter<VoucherThreeBotsBottomSheetAdapter.VoucherThreeBotsBottomSheetItemViewHolder>() {

    private val optionsList = mutableListOf<Pair<String,Int>>()

    inner class VoucherThreeBotsBottomSheetItemViewHolder(itemView: SmvcItemThreeDotsMenuBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val title: Typography = itemView.bottomSheetText
        private val icon: IconUnify = itemView.icon
        val divider: View = itemView.divider
        fun bind(item: Pair<String, Int>, position: Int) {
            title.text = item.first
            icon.setImage(item.second)
            itemView.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VoucherThreeBotsBottomSheetItemViewHolder {
        val view = SmvcItemThreeDotsMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoucherThreeBotsBottomSheetItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherThreeBotsBottomSheetItemViewHolder, position: Int) {
        holder.bind(optionsList[position], position)

        if (entryPoint == MVCBottomSheetType.OngoingEntryPoint) {
            if (positionListToShowDivider.contains(position))
                holder.divider.show()
        }
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    fun setUpcomingOptionsList() {
        this.optionsList.clear()
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_kuota) ?: "",IconUnify.COUPON))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_periode) ?: "",IconUnify.CALENDAR))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah) ?: "",IconUnify.EDIT))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",IconUnify.CLIPBOARD))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_broadcast_chat) ?: "",IconUnify.BROADCAST))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_download) ?: "",IconUnify.DOWNLOAD))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_batalkan) ?: "",IconUnify.CLEAR))
    }

    fun setOngoingOptionsList() {
        this.optionsList.clear()
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_kuota) ?: "",IconUnify.COUPON))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",IconUnify.CLIPBOARD))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_broadcast_chat) ?: "",IconUnify.BROADCAST))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_bagikan) ?: "",IconUnify.SHARE_MOBILE))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_download) ?: "",IconUnify.DOWNLOAD))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_hentikan) ?: "",IconUnify.CLEAR))
    }

    fun setCancelledOptionsList() {
        this.optionsList.clear()
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_duplikat) ?: "",IconUnify.COPY))
        optionsList.add(Pair(context?.getString(R.string.voucher_bs_ubah_lihat_detail) ?: "",IconUnify.CLIPBOARD))
    }

    companion object {
        val positionListToShowDivider = mutableListOf<Int>(1,4)
    }
}
