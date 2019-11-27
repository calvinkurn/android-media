package com.tokopedia.salam.umrah.pdp.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpItineraryModel
import com.tokopedia.salam.umrah.pdp.presentation.adapter.UmrahPdpItineraryAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_pdp_itinerary.view.*

/**
 * @author by M on 22/10/19
 */
class UmrahPdpItineraryWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr){

    private val umrahPdpItineraryAdapter: UmrahPdpItineraryAdapter by lazy { UmrahPdpItineraryAdapter() }
    var items = mutableListOf<UmrahPdpItineraryModel>()
    private var maxDisplay: Int? = null

    fun setItem(_items: List<UmrahPdpItineraryModel>, maxDisplay: Int? = null){
        items.clear()
        items.addAll(_items)
        this.maxDisplay = maxDisplay
    }

    init {
        View.inflate(context, R.layout.widget_umrah_pdp_itinerary, this)
    }

    fun buildView() {
        if (items.isEmpty()) {
            showLoading()
        } else {
            hideLoading()
            setUpItineraryList()
        }
    }

    private fun setUpItineraryList() {
        val umrahItineraryItems : List<UmrahPdpItineraryAdapter.UmrahPdpItineraryItem>
        if (maxDisplay!=null){
            read_more.visibility = View.VISIBLE
            umrahItineraryItems = items.dropLast(items.size- maxDisplay!!).map { UmrahPdpItineraryAdapter.UmrahPdpItineraryItem(it.day, it.desc) }
        }
        else{
            read_more.visibility = View.GONE
            umrahItineraryItems = items.map { UmrahPdpItineraryAdapter.UmrahPdpItineraryItem(it.day, it.desc) }
        }
        rv_widget_umrah_pdp_itinerary.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_widget_umrah_pdp_itinerary.adapter = umrahPdpItineraryAdapter
        umrahPdpItineraryAdapter.updateItems(umrahItineraryItems)
    }

    private fun showLoading(){
        container_widget_umrah_pdp_itinerary_shimmering.visibility = View.VISIBLE
        container_widget_umrah_pdp_itinerary.visibility = View.GONE
    }
    private fun hideLoading(){
        container_widget_umrah_pdp_itinerary_shimmering.visibility = View.GONE
        container_widget_umrah_pdp_itinerary.visibility = View.VISIBLE
    }
}