package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business

import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.GravitySnapHelper
import com.tokopedia.home.beranda.presentation.view.adapter.BusinessUnitItemAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.BusinessUnitDataModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LocalLoad

class NewBusinessUnitViewHolder(
    view: View,
    private val listener: BusinessUnitListener,
    private val cardInteraction: Boolean = false,
): RecyclerView.ViewHolder(view) {
    private val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
    private val loadingView = view.findViewById<View>(R.id.loading_layout)
    private val errorView = view.findViewById<LocalLoad>(R.id.error_bu_unit_widget)
    private var adapter: BusinessUnitItemAdapter? = null
    private val startSnapHelper: GravitySnapHelper by lazy { GravitySnapHelper(Gravity.START) }
    private val listenerBusinessUnitItemTrackerListener = object : BusinessUnitItemTrackerListener{
        override fun onClickTracking(tracker: HashMap<String, Any>) {
            listener.sendEnhanceEcommerce(tracker)
        }

        override fun onImpressTracking(tracker: HashMap<String, Any>) {
            listener.putEnhanceEcommerce(tracker)
        }
    }

    init {
        errorView.title?.text = itemView.context.getString(R.string.widget_gagal_ditampilkan)
        errorView.refreshBtn?.setOnClickListener {
            loadingView.show()
            errorView.hide()
            listener.getBusinessUnit(adapterPosition)
        }
        startSnapHelper.attachToRecyclerView(recyclerView)
    }

    fun onBind(model: BusinessUnitDataModel?, positionWidget: Int){
        loadingView.hide()
        recyclerView.hide()
        errorView.hide()
        if(recyclerView.adapter == null) {
            adapter = BusinessUnitItemAdapter(
                tabIndex = model?.tabPosition ?: -1,
                tabId = model?.tabId ?: "",
                tabName = model?.tabName ?: "",
                channelId = model?.channelId ?: "",
                campaignCode = model?.campaignCode ?: "",
                listenerBusinessTrackerTracker = listenerBusinessUnitItemTrackerListener,
                cardInteraction = cardInteraction,
                userId = listener.userId,
            )
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
        fun sendEnhanceEcommerce(tracker: HashMap<String, Any>)
        fun putEnhanceEcommerce(tracker: HashMap<String, Any>)
        val userId: String
    }

    interface BusinessUnitItemTrackerListener{
        fun onClickTracking(tracker: HashMap<String, Any>)
        fun onImpressTracking(tracker: HashMap<String, Any>)
    }
}
