package com.tokopedia.home.beranda.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.analytics.v2.BusinessUnitTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.BusinessWidgetTypeFactory
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.SizeLargeBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.SizeMiddleBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.SizeSmallBusinessViewHolder
import com.tokopedia.home.beranda.presentation.view.fragment.BusinessUnitItemView

@SuppressLint("SyntheticAccessor")
class BusinessUnitItemAdapter(private val tabIndex: Int, private val tabName: String) : RecyclerView.Adapter<SizeSmallBusinessViewHolder>(){
    private var list: List<BusinessUnitItemDataModel> = listOf()
    private var positionWidgetOnHome = -1

    private var listener = object: BusinessUnitItemView{
        override fun onReloadButtonClick() {}

        override fun onSuccessGetData(data: HomeWidget) {}

        override fun onErrorGetData(throwable: Throwable) {}

        override fun onImpressed(element: BusinessUnitItemDataModel, position: Int) {
            BusinessUnitTracking.getBusinessUnitView(BusinessUnitTracking.mapToPromotionTracker(element, tabName, tabIndex, positionWidgetOnHome))
        }
    }
    private var adapterTypeFactory = BusinessWidgetTypeFactory(listener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeSmallBusinessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when(viewType) {
            SizeSmallBusinessViewHolder.LAYOUT -> SizeSmallBusinessViewHolder(view, listener)
            SizeMiddleBusinessViewHolder.LAYOUT -> SizeMiddleBusinessViewHolder(view, listener)
            SizeLargeBusinessViewHolder.LAYOUT -> SizeLargeBusinessViewHolder(view, listener)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: SizeSmallBusinessViewHolder, position: Int) {
        getItem(position)?.let {businessUnit ->
            holder.bind(businessUnit)
            if(!holder.itemView.hasOnClickListeners()){
                holder.itemView.setOnClickListener {
                    BusinessUnitTracking.getBusinessUnitClick(BusinessUnitTracking.mapToPromotionTracker(businessUnit, tabName, tabIndex, positionWidgetOnHome))
                }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return type(getItem(position)?.content)
    }

    private fun type(itemTab: HomeWidget.ContentItemTab?): Int {
        return when (itemTab?.templateId) {
            1 -> SizeSmallBusinessViewHolder.LAYOUT
            2 -> SizeMiddleBusinessViewHolder.LAYOUT
            3 -> SizeLargeBusinessViewHolder.LAYOUT
            else -> SizeSmallBusinessViewHolder.LAYOUT
        }
    }

    fun submitList(list: List<BusinessUnitItemDataModel>){
        this.list = list
        notifyDataSetChanged()
    }

    fun setPositionWidgetOnHome(index: Int){
        this.positionWidgetOnHome = index
    }

    private fun getItem(listPosition: Int): BusinessUnitItemDataModel? =
        if (listPosition >= 0 && listPosition < list.size) {
            list[listPosition]
        } else {
            null
        }

}