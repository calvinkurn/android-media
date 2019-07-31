package com.tokopedia.affiliate.feature.createpost.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.affiliate.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import kotlinx.android.synthetic.main.item_default_captions.view.*

class DefaultCaptionsAdapter(private val onCaptionsSelected: (String)->Unit):
        RecyclerView.Adapter<DefaultCaptionsAdapter.DefaultCaptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultCaptionsViewHolder {
        return DefaultCaptionsViewHolder(parent.inflateLayout(R.layout.item_default_captions))
    }

    override fun getItemCount(): Int = captions.size

    override fun onBindViewHolder(holder: DefaultCaptionsViewHolder, position: Int) {
        holder.bind(captions[position])
    }

    private val captions = mutableListOf<String>()

    fun updateCaptions(_captions: List<String>){
        captions.clear()
        captions.addAll(_captions)
        notifyDataSetChanged()
    }

    inner class DefaultCaptionsViewHolder(view: View): RecyclerView.ViewHolder(view){

        init {
            itemView.setOnClickListener { onCaptionsSelected(captions[adapterPosition]) }
        }

        fun bind(captions: String){
            itemView.caption_title.text = captions
        }
    }
}