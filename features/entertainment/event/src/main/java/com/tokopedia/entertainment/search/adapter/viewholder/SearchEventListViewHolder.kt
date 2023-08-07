package com.tokopedia.entertainment.search.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchEventListItemBinding
import com.tokopedia.entertainment.databinding.EntSearchEventSuggestionBinding
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchEventModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class SearchEventListViewHolder(val view: View,
                                val listener: SearchEventListListener
) : SearchEventViewHolder<SearchEventModel>(view) {

    private val eventListAdapter = KegiatanAdapter(listener)
    private val binding: EntSearchEventSuggestionBinding? by viewBinding()

    init {
        binding?.recyclerViewKegiatan?.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = eventListAdapter
        }
    }

    override fun bind(element: SearchEventModel) {
        eventListAdapter.listKegiatan = element.listEvent
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KegiatanHolder {
            val binding = EntSearchEventListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return KegiatanHolder(binding, listener, listKegiatan)
        }

        override fun getItemCount(): Int = listKegiatan.size

        override fun onBindViewHolder(holder: KegiatanHolder, position: Int) {
            holder.bind(listKegiatan[position])
        }
    }

    class KegiatanHolder(val binding: EntSearchEventListItemBinding, val listener: SearchEventListListener, var listKegiatan : List<KegiatanSuggestion>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: KegiatanSuggestion) {
            with(binding) {
                imgEvent.loadImage(element.image_url)
                txtJudulEvent.text = element.nama_kegiatan

                root.addOnImpressionListener(element) {
                    listener.impressionEventSearchSuggestion(listKegiatan.get(position), position)
                }

                root.setOnClickListener {
                    listener.clickEventSearchSuggestion(element, listKegiatan, position+1)
                }

                if(element.tanggal_kegiatan.isBlank() || element.tanggal_kegiatan.equals(ZERO_STRING) || element.category.equals(
                        THREE_STRING)){
                    txtTanggalSearch.visibility = View.INVISIBLE
                    txtTanggalSearch.text = ""
                    txtLokasiSearch.text = element.lokasi_kegiatan
                } else{
                    txtTanggalSearch.visibility = View.VISIBLE
                    txtTanggalSearch.text = DateFormatUtils.getFormattedDate(element.tanggal_kegiatan, DATE_PATTERN)
                    txtLokasiSearch.text = String.format(root.context.resources.getString(R.string.ent_search_black_circle, element.lokasi_kegiatan))
                }
            }
        }
    }



    companion object{
        val LAYOUT = R.layout.ent_search_event_suggestion
        const val DATE_PATTERN = "dd MMM"
        const val ZERO_STRING = "0"
        const val THREE_STRING = "3"
    }

    interface SearchEventListListener{
        fun impressionEventSearchSuggestion(listsEvent: SearchEventListViewHolder.KegiatanSuggestion, position: Int)
        fun clickEventSearchSuggestion(event: SearchEventListViewHolder.KegiatanSuggestion,
                                       listsEvent: List<SearchEventListViewHolder.KegiatanSuggestion>,
                                       position : Int)
    }
}
