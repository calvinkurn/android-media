package com.tokopedia.home.beranda.presentation.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.home.analytics.v2.BusinessUnitTracking
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitItemDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business.*

@SuppressLint("SyntheticAccessor")
class BusinessUnitItemAdapter(private val tabIndex: Int, private val tabName: String,
                              private val listenerBusinessTrackerTracker: NewBusinessUnitViewHolder.BusinessUnitItemTrackerListener,
                              private val cardInteraction: Boolean = false
) : RecyclerView.Adapter<SizeSmallBusinessViewHolder>(){
    private var list: List<BusinessUnitItemDataModel> = listOf()
    private var positionWidgetOnHome = -1

    private var listener = object: BusinessUnitItemViewListener{
        //increase tab index by 1 as  PO requested @rico.ocir
        override fun onClicked(position: Int) {
            val element = getItem(position)
            listenerBusinessTrackerTracker.onClickTracking(BusinessUnitTracking.getBusinessUnitClick(BusinessUnitTracking.mapToPromotionTracker(element, positionWidgetOnHome)) as HashMap<String, Any>)
        }

        override fun onImpressed(element: BusinessUnitItemDataModel, position: Int) {
            listenerBusinessTrackerTracker.onImpressTracking(BusinessUnitTracking.getBusinessUnitView(BusinessUnitTracking.mapToPromotionTracker(element, positionWidgetOnHome)) as HashMap<String, Any>)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeSmallBusinessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when(viewType) {
            SizeSmallBusinessViewHolder.LAYOUT -> SizeSmallBusinessViewHolder(view, listener, cardInteraction)
            SizeMiddleBusinessViewHolder.LAYOUT -> SizeMiddleBusinessViewHolder(view, listener, cardInteraction)
            SizeLargeBusinessViewHolder.LAYOUT -> SizeLargeBusinessViewHolder(view, listener, cardInteraction)
            else -> super.createViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: SizeSmallBusinessViewHolder, position: Int) {
        getItem(position).let {businessUnit ->
            holder.bind(businessUnit)
        }
    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return type(getItem(position).content)
    }

    private fun type(itemTab: HomeWidget.ContentItemTab?): Int {
        return when (itemTab?.templateId) {
            TEMPLATE_ID_1 -> SizeSmallBusinessViewHolder.LAYOUT
            TEMPLATE_ID_2 -> SizeMiddleBusinessViewHolder.LAYOUT
            TEMPLATE_ID_3 -> SizeLargeBusinessViewHolder.LAYOUT
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

    private fun getItem(listPosition: Int): BusinessUnitItemDataModel = list[listPosition]

    companion object {
        private const val TEMPLATE_ID_1 = 1
        private const val TEMPLATE_ID_2 = 2
        private const val TEMPLATE_ID_3 = 3
    }
}