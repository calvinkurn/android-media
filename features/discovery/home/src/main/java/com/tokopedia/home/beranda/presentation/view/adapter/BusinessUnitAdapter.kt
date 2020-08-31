package com.tokopedia.home.beranda.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.NewBusinessUnitViewHolder


@Suppress("unused")
@SuppressLint("SyntheticAccessor")
class BusinessUnitAdapter(private val listener: NewBusinessUnitViewHolder.BusinessUnitListener) : RecyclerView.Adapter<NewBusinessUnitViewHolder>() {
    private var itemList: List<BusinessUnitDataModel> = listOf()
    private var positionWidgetOnHome = -1

    fun setItemList(newItemList: List<BusinessUnitDataModel>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    private fun getItem(listPosition: Int): BusinessUnitDataModel? {
        return if (listPosition >= 0 && listPosition < itemList.size) {
            itemList[listPosition]
        } else {
            null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewBusinessUnitViewHolder {
        return NewBusinessUnitViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_business_unit, parent, false), listener)
    }

    override fun onBindViewHolder(holder: NewBusinessUnitViewHolder, position: Int) {
        holder.onBind(getItem(position), positionWidgetOnHome)
    }

    override fun onBindViewHolder(holder: NewBusinessUnitViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty()){
            holder.onBind(getItem(position), payloads, positionWidgetOnHome)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setPositionWidgetOnHome(index: Int){
        this.positionWidgetOnHome = index
    }

    init {
        setItemList(listOf())
    }
}
