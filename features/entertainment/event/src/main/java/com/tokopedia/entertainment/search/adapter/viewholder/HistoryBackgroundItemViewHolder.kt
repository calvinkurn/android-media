package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.HistoryModel
import com.tokopedia.kotlin.extensions.view.setMargin
import kotlinx.android.synthetic.main.ent_search_event_list_item.view.*
import kotlinx.android.synthetic.main.ent_search_history_search.view.*

class HistoryBackgroundItemViewHolder(val view: View) : SearchEventViewHolder<HistoryModel>(view){

    val listAdapter = EventAdapter()

    init {
        itemView.recycler_view.apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            adapter = listAdapter
        }
    }

    override fun bind(element: HistoryModel) {
        listAdapter.list = element.list
        listAdapter.notifyDataSetChanged()
    }

    companion object{
        val LAYOUT = R.layout.ent_search_history_search
    }


    data class EventModel(val nama_event : String, val tanggal_event : String, val lokasi_event : String, val image_url : String, val app_url : String)

    class EventAdapter : RecyclerView.Adapter<EventViewHolder>(){
        lateinit var list: List<EventModel>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
            return EventViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_search_event_list_item, parent, false))
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            var element: EventModel = list.get(position)

            Glide.with(holder.view)
                    .load(element.image_url)
                    .centerCrop()
                    .into(holder.view.imgEvent)
            holder.view.txtTanggalSearch.text = element.tanggal_event
            holder.view.txtJudulEvent.text = element.nama_event
            holder.view.txtLokasiSearch.text = element.lokasi_event
            holder.view.setOnClickListener {
                RouteManager.route(holder.view.context, element.app_url)
            }

            if(element.tanggal_event.isBlank()){
                holder.view.txtLokasiSearch.setMargin(0,0,0,0)
            }
        }
    }

    class EventViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}