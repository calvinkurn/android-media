package com.tokopedia.topads.dashboard.view.adapter.beranda

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class RingkasanTopAdsDashboardRvAdapter :
    RecyclerView.Adapter<RingkasanTopAdsDashboardRvAdapter.RingkasanViewHolder>() {

    private val list = mutableListOf<Any>()
    var infoClicked: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RingkasanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        val holder = RingkasanViewHolder(view)
        holder.ivInformation.setOnClickListener { infoClicked?.invoke() }
        return holder
    }

    override fun onBindViewHolder(holder: RingkasanViewHolder, position: Int) {

    }

    override fun getItemCount() = list.size

    inner class RingkasanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<Typography>(R.id.txtTitle)
        val ivInformation = view.findViewById<ImageUnify>(R.id.ivInformation)
        val txtValue = view.findViewById<Typography>(R.id.txtValue)
        val txtPercentageChange = view.findViewById<Typography>(R.id.txtPercentageChange)
        val txtFromLastWeek = view.findViewById<Typography>(R.id.txtFromLastWeek)
    }

    companion object {
        private val layout = R.layout.item_rv_ringkasan
        fun createInstance() = RingkasanTopAdsDashboardRvAdapter()
    }
}