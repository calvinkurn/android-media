package com.tokopedia.entertainment.search.adapter.viewholder

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventModel
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.synthetic.main.ent_search_event_list_item.view.*
import kotlinx.android.synthetic.main.ent_search_event_suggestion.view.*

class SearchEventListViewHolder(val view: View,
                                val listener: SearchEventListListener
) : SearchEventViewHolder<SearchEventModel>(view) {

    val eventListAdapter = KegiatanAdapter(listener)

    init {
        with(itemView){
            recycler_view_kegiatan.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = eventListAdapter
            }
        }
    }

    override fun bind(element: SearchEventModel) {
        eventListAdapter.listKegiatan = element.listEvent
        eventListAdapter.resources = element.resources
        eventListAdapter.notifyDataSetChanged()
    }

    data class KegiatanSuggestion(
            val id: String,
            val price: String,
            val nama_kegiatan : String,
            val tanggal_kegiatan : String,
            val lokasi_kegiatan : String,
            val image_url : String,
            val app_url : String,
            val isLiked: Boolean,
            val category: String,
            val sales_price:String
    ) : ImpressHolder()

    class KegiatanAdapter(val listener: SearchEventListListener) : RecyclerView.Adapter<KegiatanHolder>(){

        lateinit var listKegiatan : List<KegiatanSuggestion>
        lateinit var resources: Resources

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanHolder {
            return KegiatanHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_search_event_list_item,parent,false))
        }

        override fun getItemCount(): Int = listKegiatan.size

        override fun onBindViewHolder(holder: KegiatanHolder, position: Int) {
            val element = listKegiatan.get(position)

            Glide.with(holder.view)
                    .load(element.image_url)
                    .centerCrop()
                    .into(holder.view.imgEvent)
            holder.view.txtJudulEvent.text = element.nama_kegiatan

            holder.view.addOnImpressionListener(element, {
                listener.impressionEventSearchSuggestion(listKegiatan.get(position), position)
            })

            holder.view.setOnClickListener {
                listener.clickEventSearchSuggestion(element, listKegiatan, position+1)
                RouteManager.route(holder.view.context, element.app_url)
            }

            if(element.tanggal_kegiatan.isBlank() || element.tanggal_kegiatan.equals("0") || element.category.equals("3")){
                holder.view.txtTanggalSearch.visibility = View.INVISIBLE
                holder.view.txtTanggalSearch.text = ""
                holder.view.txtLokasiSearch.text = element.lokasi_kegiatan
            } else{
                holder.view.txtTanggalSearch.visibility = View.VISIBLE
                holder.view.txtTanggalSearch.text = DateFormatUtils.getFormattedDate(element.tanggal_kegiatan, "dd MMM")
                holder.view.txtLokasiSearch.text = String.format(resources.getString(R.string.ent_search_black_circle, element.lokasi_kegiatan))
            }
        }
    }

    class KegiatanHolder(val view: View) : RecyclerView.ViewHolder(view)



    companion object{
        val LAYOUT = R.layout.ent_search_event_suggestion
    }

    interface SearchEventListListener{
        fun impressionEventSearchSuggestion(listsEvent: SearchEventListViewHolder.KegiatanSuggestion, position: Int)
        fun clickEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion,
                                       listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>,
                                       position : Int)
    }
}