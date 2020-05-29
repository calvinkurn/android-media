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
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventViewModel
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.synthetic.main.ent_search_event_list_item.view.*
import kotlinx.android.synthetic.main.ent_search_event_suggestion.view.*
import timber.log.Timber

class SearchEventListViewHolder(val view: View) : SearchEventViewHolder<SearchEventViewModel>(view) {

    val eventListAdapter = KegiatanAdapter()

    init {
        with(itemView){
            recycler_view_kegiatan.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = eventListAdapter
            }
        }
    }

    override fun bind(element: SearchEventViewModel) {
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
            val category: String
    ) : ImpressHolder()

    class KegiatanAdapter : RecyclerView.Adapter<KegiatanHolder>(){

        lateinit var listKegiatan : List<KegiatanSuggestion>
        lateinit var resources: Resources

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanHolder {
            return KegiatanHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_search_event_list_item,parent,false))
        }

        override fun getItemCount(): Int = listKegiatan.size

        override fun onBindViewHolder(holder: KegiatanHolder, position: Int) {
            val element = listKegiatan.get(position)

            holder.view.imgEvent.loadImageRounded(element.image_url)
            holder.view.txtJudulEvent.text = element.nama_kegiatan

            holder.view.addOnImpressionListener(element, {
                EventSearchPageTracking.getInstance().impressionEventSearchSuggestion(listKegiatan)
            })

            holder.view.setOnClickListener {
                RouteManager.route(holder.view.context, element.app_url)
                //Tracking
                EventSearchPageTracking.getInstance().onClickedEventSearchSuggestion(element, listKegiatan, position+1)
            }

            if(element.tanggal_kegiatan.isBlank() || element.tanggal_kegiatan.equals("0") || element.category.equals("3")){
                holder.view.txtTanggalSearch.visibility = View.INVISIBLE
                holder.view.txtTanggalSearch.text = ""
                holder.view.txtLokasiSearch.text = element.lokasi_kegiatan
            } else{
                holder.view.txtTanggalSearch.visibility = View.VISIBLE
                holder.view.txtTanggalSearch.text = DateFormatUtils.getFormattedDate(element.tanggal_kegiatan, "dd MMM")
                holder.view.txtLokasiSearch.text = String.format(resources.getString(R.string.black_circle, element.lokasi_kegiatan))
            }
        }
    }

    class KegiatanHolder(val view: View) : RecyclerView.ViewHolder(view)



    companion object{
        val LAYOUT = R.layout.ent_search_event_suggestion
    }
}