package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomFullBinding
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomFullViewHolder

class DenomFullAdapter: RecyclerView.Adapter<DenomFullViewHolder>(), RechargeDenomFullListener {

    private var listDenom = mutableListOf<DenomData>()

    var selectedProductIndex: Int? = null

    var denomWidgetType: DenomWidgetEnum = DenomWidgetEnum.FULL_TYPE

    var productTitleList: String = ""

    var listener: RechargeDenomFullListener? = null

    override fun getItemCount(): Int = listDenom.size

    override fun onBindViewHolder(holder: DenomFullViewHolder, position: Int) {
        holder.bind(
            listDenom[position],
            denomWidgetType,
            position == selectedProductIndex,
            position
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenomFullViewHolder {
        val binding = ViewRechargeDenomFullBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DenomFullViewHolder(this, binding)
    }

    override fun onDenomFullClicked(denomFull: DenomData,
                                    layoutType: DenomWidgetEnum, position: Int,
                                    productListTitle: String,
                                    isShowBuyWidget: Boolean) {
        var isNeedtoShowBuyWidget = isShowBuyWidget

        if (selectedProductIndex == null){
            selectedProductIndex = position
            notifyItemChanged(position)
        } else if (selectedProductIndex == position) {
            selectedProductIndex = null
            notifyItemChanged(position)
            isNeedtoShowBuyWidget = false
        } else {
            selectedProductIndex?.let { selectedPos ->
                notifyItemChanged(selectedPos)
            }
            selectedProductIndex = position
            notifyItemChanged(position)
        }
        listener?.onDenomFullClicked(denomFull, denomWidgetType, position, productTitleList, isNeedtoShowBuyWidget)
    }

    override fun onDenomFullImpression(
        denomFull: DenomData,
        layoutType: DenomWidgetEnum,
        position: Int
    ) {
        listener?.onDenomFullImpression(denomFull, layoutType, position)
    }

    override fun onChevronDenomClicked(denomFull: DenomData, position: Int, layoutType: DenomWidgetEnum) {
        listener?.onChevronDenomClicked(denomFull, position, layoutType)
    }

    fun setDenomFullList(listDenom: List<DenomData>) {
        this.listDenom = listDenom.toMutableList()
        notifyDataSetChanged()
    }

    fun clearDenomFullData() {
        this.listDenom.clear()
    }
}