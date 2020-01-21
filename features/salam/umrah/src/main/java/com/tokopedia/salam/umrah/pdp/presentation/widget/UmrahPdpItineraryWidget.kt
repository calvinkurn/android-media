package com.tokopedia.salam.umrah.pdp.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.pdp.data.UmrahPdpItineraryModel
import com.tokopedia.salam.umrah.pdp.presentation.adapter.UmrahPdpItineraryAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_umrah_pdp_itinerary.view.*

/**
 * @author by M on 22/10/19
 */
class UmrahPdpItineraryWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    private val umrahPdpItineraryAdapter: UmrahPdpItineraryAdapter by lazy { UmrahPdpItineraryAdapter() }
    private var items = mutableListOf<UmrahPdpItineraryModel>()
    private var maxDisplay: Int = 0

    fun setItem(_items: List<UmrahPdpItineraryModel>, maxDisplay: Int = 0) {
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
        val umrahItineraryItems: List<UmrahPdpItineraryModel>
        if (maxDisplay != 0) {
            read_more.visibility = View.VISIBLE
            umrahItineraryItems = items.dropLast(items.size - maxDisplay)
        } else {
            read_more.visibility = View.GONE
            umrahItineraryItems = items
        }
        rv_widget_umrah_pdp_itinerary.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = umrahPdpItineraryAdapter
        }
        umrahPdpItineraryAdapter.updateItems(umrahItineraryItems)
    }

    private fun showLoading() {
        container_widget_umrah_pdp_itinerary_shimmering.visibility = View.VISIBLE
        container_widget_umrah_pdp_itinerary.visibility = View.GONE
    }

    private fun hideLoading() {
        container_widget_umrah_pdp_itinerary_shimmering.visibility = View.GONE
        container_widget_umrah_pdp_itinerary.visibility = View.VISIBLE
    }
}