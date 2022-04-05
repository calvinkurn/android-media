package com.tokopedia.centralizedpromo.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.centralizedpromo.view.model.FirstPromoUiModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.databinding.CentralizedPromoFirstPromoItemLayoutBinding
import timber.log.Timber

class FirstVoucherAdapter(private val itemList: List<FirstPromoUiModel>) :
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
                when {
                    item.iconDrawableRes != null -> {
                        ContextCompat.getDrawable(root.context, item.iconDrawableRes)?.let {
                            firstPromoItemIcon.setImageDrawable(it)
                        }
                        firstPromoItemIconUnify.gone()
                    }
                    item.iconUnifyAndColorResPair != null -> {
                        val (iconId, iconColor) = item.iconUnifyAndColorResPair
                        firstPromoItemIconUnify.setupIconColor(root.context, iconId, iconColor)
                        firstPromoItemIcon.gone()
                    }
                }
                if (item.titleRes == null) {
                    firstPromoItemTitle.gone()
                } else {
                    firstPromoItemTitle.text = root.context.getString(item.titleRes).toBlankOrString()
                }
                firstPromoItemDescription.text =
                    root.context.getString(item.descriptionRes).toBlankOrString()
            }
        }

    }

    private fun IconUnify.setupIconColor(context: Context, iconId: Int, iconColorRes: Int) {
        try {
            val color = MethodChecker.getColor(context, iconColorRes)
            setImage(newIconId = iconId, newLightEnable = color)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

}