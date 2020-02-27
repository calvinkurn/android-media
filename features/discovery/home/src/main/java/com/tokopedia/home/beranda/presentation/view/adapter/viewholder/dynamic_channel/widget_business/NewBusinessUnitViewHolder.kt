package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.BusinessUnitItemAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LocalLoad

class NewBusinessUnitViewHolder (view: View, private val listener: BusinessUnitListener): RecyclerView.ViewHolder(view) {
    private val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
    private val loadingView = view.findViewById<View>(R.id.loading_layout)
    private val errorView = view.findViewById<LocalLoad>(R.id.error_bu_unit_widget)
    private var adapter: BusinessUnitItemAdapter? = null

    init {
        errorView.title?.text = itemView.context.getString(R.string.widget_gagal_ditampilkan)
        errorView.refreshBtn?.setOnClickListener {
            loadingView.show()
            errorView.hide()
            listener.getBusinessUnit(adapterPosition)
        }
    }

    fun onBind(model: BusinessUnitDataModel?, positionWidget: Int){
        loadingView.hide()
        recyclerView.hide()
        errorView.hide()
        if(recyclerView.adapter == null) {
            adapter = BusinessUnitItemAdapter(model?.tabPosition ?: -1, model?.tabName ?: "")
            recyclerView.adapter = adapter
        }
        adapter?.setPositionWidgetOnHome(positionWidget)
        if(model?.list != null){
            recyclerView.show()
            loadingView.hide()
            if(model.list.isEmpty()){
                errorView.show()
            }
            adapter?.submitList(model.list)
        } else {
            loadingView.show()
            listener.getBusinessUnit(adapterPosition)
        }
    }

    fun onBind(model: BusinessUnitDataModel?, payload: List<Any>, positionWidget: Int){
        if(model?.list != null){
            adapter?.setPositionWidgetOnHome(positionWidget)
            adapter?.submitList(model.list)
        }
    }

    interface BusinessUnitListener{
        fun getBusinessUnit(position: Int)
    }
}
