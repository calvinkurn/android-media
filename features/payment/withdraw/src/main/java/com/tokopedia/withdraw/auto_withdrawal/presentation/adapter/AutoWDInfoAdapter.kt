package com.tokopedia.withdraw.auto_withdrawal.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.withdraw.R

class AutoWDInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(
                parent.context)
        val v = inflater.inflate(R.layout.swd_item_awd_info, parent, false)
        return AutoWDInfoHolder(v)
    }

    override fun getItemCount(): Int = 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    internal inner class AutoWDInfoHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var infoTitleText: TextView? = null
        private var infoDescText: TextView? = null
        private var infoIcon: ImageView? = null

        private fun findingViewsId(view: View) {
            infoTitleText = view.findViewById(R.id.info_title)
            infoDescText = view.findViewById(R.id.info_description)
            infoIcon = view.findViewById(R.id.info_type_img)
        }
    }


}