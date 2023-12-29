package com.tokopedia.entertainment.search.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.EntSearchCityAdapterItemBinding
import com.tokopedia.entertainment.databinding.EntSearchLocationSuggestionBinding
import com.tokopedia.entertainment.search.Link
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationModel
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.utils.view.binding.viewBinding
import java.util.Locale

class SearchLocationListViewHolder(val view: View, val onClicked: (() -> Unit),
                                   val listener: SearchLocationListener
) : SearchEventViewHolder<SearchLocationModel>(view) {

    private val locationListAdapter = LocationAdapter(listener)
    private val binding: EntSearchLocationSuggestionBinding? by viewBinding()
    init {
        binding?.recyclerViewLocation?.run {
             setHasFixedSize(true)
             layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
             adapter = locationListAdapter
        }

        binding?.lihatSemua?.setOnClickListener {
             onClicked.invoke()
        }
    }

    override fun bind(element: SearchLocationModel) {
        locationListAdapter.listLocation = element.listLocation
        locationListAdapter.searchQuery = element.query
        locationListAdapter.notifyDataSetChanged()

        if (element.allLocation) {
            binding?.lihatSemua?.visibility = View.GONE
        }
    }

    companion object {
        val LAYOUT = R.layout.ent_search_location_suggestion
        private const val QUERY_CATEGORY =  "?id_city={id_city}&query_text={query_text}"
    }

    data class LocationSuggestion(
            val id_city: String,
            val city: String,
            val country: String,
            val type: String,
            val imageUrl: String
    ) : ImpressHolder()

    class LocationAdapter(val listener: SearchLocationListener) :
            RecyclerView.Adapter<LocationViewHolder>() {

        lateinit var listLocation: List<LocationSuggestion>
        lateinit var searchQuery: String
        lateinit var spannable: SpannableStringBuilder

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
            val binding = EntSearchCityAdapterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return LocationViewHolder(binding, searchQuery, listLocation, listener)
        }

        override fun getItemCount(): Int = listLocation.size

        override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
            holder.bind(listLocation[position])
        }
    }

    class LocationViewHolder(val binding: EntSearchCityAdapterItemBinding, val searchQuery: String, val listLocation: List<LocationSuggestion>, val listener: SearchLocationListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationSuggestion) {
            with(binding) {
                locName.text = getSpannableText(location.city, searchQuery)
                locType.text = location.type

                root.setOnClickListener {
                    listener.clickLocationEvent(location,
                        listLocation.get(position), position)
                    val intent = RouteManager.getIntent(root.context,
                        Link.EVENT_CATEGORY + QUERY_CATEGORY,
                        location.id_city, location.city)
                    root.context.startActivity(intent)
                }

                root.addOnImpressionListener(location) {
                    listener.impressionLocationEvent(listLocation.get(position), position)
                }
            }
        }
        private fun getSpannableText(cityFullText: String, textToBold: String):
            SpannableStringBuilder {
            val spannable = SpannableStringBuilder(cityFullText)
            try {
                val startIndex = cityFullText.lowercase(Locale.US)
                    .indexOf(textToBold.lowercase(Locale.US)) + textToBold.length
                spannable.setSpan(StyleSpan(Typeface.BOLD), startIndex, cityFullText.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (startIndex - Int.ONE - textToBold.length > -Int.ONE)
                    spannable.setSpan(StyleSpan(Typeface.BOLD), Int.ZERO, startIndex -
                        textToBold.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return spannable
        }
    }

    interface SearchLocationListener{
        fun impressionLocationEvent(listsCity: SearchLocationListViewHolder.LocationSuggestion, position: Int)
        fun clickLocationEvent(location: SearchLocationListViewHolder.LocationSuggestion,
                               listsLocation: SearchLocationListViewHolder.LocationSuggestion,
                               position: Int)
    }

}
