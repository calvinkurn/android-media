package com.tokopedia.product.report.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

/**
 * Created by hendry on 08/02/19.
 */
class ReportTypeAdapter(context: Context?, resource: Int, reportType: List<String>) :
        ArrayAdapter<String>(context, resource, reportType) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getView(position, convertView, parent)
        view.setPadding(0, view.paddingTop, view.paddingRight, view.paddingBottom)
        return view
    }
}
