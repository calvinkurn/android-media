package com.tokopedia.recharge_component.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recharge_component.databinding.ViewRechargeDenomGridBinding
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.presentation.adapter.viewholder.denom.DenomGridViewHolder

class DenomGridAdapter: RecyclerView.Adapter<DenomGridViewHolder>(), RechargeDenomGridListener {

    private var listDenom = mutableListOf<DenomData>()

    var selectedProductIndex: Int? = null

    var denomWidgetType: DenomWidgetEnum = DenomWidgetEnum.GRID_TYPE

    var productTitleList: String = ""

    var listener: RechargeDenomGridListener? = null

    override fun getItemCount(): Int = listDenom.size

    override fun onBindViewHolder(holder: DenomGridViewHolder, position: Int) {
        holder.bind(listDenom[position], denomWidgetType, position == selectedProductIndex, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DenomGridViewHolder {
        val binding = ViewRechargeDenomGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return DenomGridViewHolder(this, binding)
    }

    override fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int,
                                    productTitle: String, isShowBuyWidget: Boolean) {
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
        listener?.onDenomGridClicked(denomGrid, denomWidgetType, position, productTitleList, isNeedtoShowBuyWidget)
    }

    override fun onDenomGridImpression(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int) {
        listener?.onDenomGridImpression(denomGrid, layoutType, position)
    }

    fun setDenomGridList(listDenom: List<DenomData>) {
        this.listDenom = listDenom.toMutableList()
        notifyDataSetChanged()
    }

    fun clearDenomGridData(){
        this.listDenom.clear()
    }
}