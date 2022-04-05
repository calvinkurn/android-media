package com.tokopedia.centralizedpromo.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.centralizedpromo.view.model.FirstVoucherUiModel
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CentralizedPromoFirstPromoItemLayoutBinding

class FirstVoucherAdapter(private val itemList: List<FirstVoucherUiModel>) :
    RecyclerView.Adapter<FirstVoucherAdapter.FirstVoucherViewHolder>() {

    class FirstVoucherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding by lazy { CentralizedPromoFirstPromoItemLayoutBinding.bind(view) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FirstVoucherViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.centralized_promo_first_promo_item_layout, parent, false)
        return FirstVoucherViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: FirstVoucherViewHolder, position: Int) {
        itemList[position].let { item ->
            holder.binding.run {
                ContextCompat.getDrawable(root.context, item.iconDrawableRes)?.let {
                    firstPromoItemIcon.setImageDrawable(it)
                }
                firstPromoItemTitle.text = root.context.getString(item.titleRes).toBlankOrString()
                firstPromoItemDescription.text =
                    root.context.getString(item.descriptionRes).toBlankOrString()
            }
        }

    }
}