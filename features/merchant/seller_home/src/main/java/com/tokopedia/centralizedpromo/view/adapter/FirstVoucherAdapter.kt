package com.tokopedia.centralizedpromo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.centralizedpromo.view.model.FirstVoucherUiModel
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.sellerhome.R
import kotlinx.android.synthetic.main.centralized_promo_first_voucher_item_layout.view.*

class FirstVoucherAdapter(private val itemList: List<FirstVoucherUiModel>) : RecyclerView.Adapter<FirstVoucherAdapter.FirstVoucherViewHolder>() {

    class FirstVoucherViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstVoucherViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.centralized_promo_first_voucher_item_layout, parent, false)
        return FirstVoucherViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FirstVoucherViewHolder, position: Int) {
        itemList[position].let { item ->
            holder.itemView.run {
                ContextCompat.getDrawable(context, item.iconDrawableRes)?.let {
                    firstVoucherItemIcon.setImageDrawable(it)
                }
                firstVoucherItemTitle.text = context?.getString(item.titleRes).toBlankOrString()
                firstVoucherItemDescription.text = context?.getString(item.descriptionRes).toBlankOrString()
            }
        }

    }
}