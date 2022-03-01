package com.tokopedia.vouchercreation.shop.detail.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.SharingUtil
import com.tokopedia.vouchercreation.shop.detail.model.SubInfoItemUiModel
import kotlinx.android.synthetic.main.item_mvc_sub_info.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class SubInfoAdapter : RecyclerView.Adapter<SubInfoAdapter.SubInfoViewHolder>() {

    companion object {
        private const val PROMO_CODE_LABEL = "promo_code"
    }

    private val items = mutableListOf<SubInfoItemUiModel>()

    private var onPromoCodeClick: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubInfoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SubInfoViewHolder(inflater.inflate(R.layout.item_mvc_sub_info, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SubInfoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    fun setSubInfoItems(items: List<SubInfoItemUiModel>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnPromoCodeClicked(action: () -> Unit) {
        onPromoCodeClick = action
    }

    inner class SubInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(model: SubInfoItemUiModel) = with(itemView) {
            if (model.isWarning) {
                tvMvcInfoKey.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Red_R500))
                tvMvcInfoValue.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Red_R500))
            }
            tvMvcInfoKey.text = context?.getString(model.infoKey).toBlankOrString()
            tvMvcInfoValue.text = model.infoValue.parseAsHtml()
            imgMvcVoucherCopy.isVisible = model.canCopy
            imgMvcVoucherCopy.setOnClickListener {
                onPromoCodeClick()
                context?.run {
                    SharingUtil.copyTextToClipboard(this, PROMO_CODE_LABEL, model.infoValue)
                }
                Toaster.make(this,
                        context.getString(R.string.mvc_voucher_code_copied),
                        Snackbar.LENGTH_LONG,
                        Toaster.TYPE_NORMAL)
            }
        }

    }
}