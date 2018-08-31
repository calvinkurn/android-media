package com.tokopedia.talk.reporttalk.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.talk.R
import com.tokopedia.talk.reporttalk.view.viewmodel.TalkReportOptionViewModel


/**
 * @author by nisie on 8/30/18.
 */
class ReportTalkAdapter : RecyclerView.Adapter<ReportTalkAdapter.ViewHolder>() {

    val listOption: ArrayList<TalkReportOptionViewModel> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent.inflate(R.layout.view_item))

    override fun getItemCount(): Int {

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }


}