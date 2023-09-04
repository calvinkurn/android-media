package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchEventListItemBinding
import com.tokopedia.entertainment.databinding.EntSearchHistorySearchBinding
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.HistoryModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class HistoryBackgroundItemViewHolder(val view: View) : SearchEventViewHolder<HistoryModel>(view){

    private val listAdapter = EventAdapter()
    private val binding: EntSearchHistorySearchBinding? by viewBinding()
    init {
        binding?.recyclerView?.run {
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
            val binding = EntSearchEventListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return EventViewHolder(binding)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
            holder.bind(list[position])
        }
    }

    class EventViewHolder(val binding: EntSearchEventListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: EventModel) {
            with(binding) {
                imgEvent.loadImage(element.image_url)
                txtTanggalSearch.text = element.tanggal_event
                txtJudulEvent.text = element.nama_event
                txtLokasiSearch.text = element.lokasi_event
                root.setOnClickListener {
                    RouteManager.route(root.context, element.app_url)
                }

                if(element.tanggal_event.isBlank()){
                    txtLokasiSearch.setMargin(Int.ZERO, Int.ZERO, Int.ZERO, Int.ZERO)
                }
            }
        }
    }
}
