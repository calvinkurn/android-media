package com.tokopedia.flight.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

/**
 * @author by jessica on 2020-02-21
 */

class FlightFilterSortAdapter(var typeFactory: FlightFilterSortAdapterTypeFactory) :
        BaseAdapter<FlightFilterSortAdapterTypeFactory>(typeFactory) {

    override fun onCreateViewItem(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(typeFactory.getViewHolderLayout(viewType), parent, false)
    }

    fun resetFilter() {
        typeFactory.resetFilter()
    }
}