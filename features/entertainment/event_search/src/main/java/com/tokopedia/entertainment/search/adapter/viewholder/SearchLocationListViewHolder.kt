package com.tokopedia.entertainment.search.adapter.viewholder

import android.content.Intent
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
import com.tokopedia.entertainment.search.Link
import com.tokopedia.entertainment.search.R
import com.tokopedia.entertainment.search.adapter.SearchEventViewHolder
import com.tokopedia.entertainment.search.adapter.viewmodel.SearchLocationViewModel
import com.tokopedia.entertainment.search.analytics.EventSearchPageTracking
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import kotlinx.android.synthetic.main.ent_search_city_adapter_item.view.*
import kotlinx.android.synthetic.main.ent_search_location_suggestion.view.*
import timber.log.Timber
import java.util.*

class SearchLocationListViewHolder(val view: View, val onClicked: (() -> Unit)) : SearchEventViewHolder<SearchLocationViewModel>(view) {

    val locationListAdapter = LocationAdapter()

    init {
        with(itemView) {
            recycler_view_location.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = locationListAdapter
            }

            lihatSemua.setOnClickListener {
                Timber.tag("Lihat").w("Clicked")
                onClicked.invoke()
            }
        }
    }

    override fun bind(element: SearchLocationViewModel) {
        locationListAdapter.listLocation = element.listLocation
        locationListAdapter.searchQuery = element.query
        locationListAdapter.notifyDataSetChanged()

        if (element.allLocation) {
            itemView.lihatSemua.visibility = View.GONE
        }
    }

    companion object {
        val LAYOUT = R.layout.ent_search_location_suggestion
    }

    data class LocationSuggestion(
            val id_city: String,
            val city: String,
            val country: String,
            val type: String,
            val imageUrl: String
    ) : ImpressHolder()

    class LocationAdapter : RecyclerView.Adapter<LocationViewHolder>() {

        lateinit var listLocation: List<LocationSuggestion>
        lateinit var searchQuery: String
        lateinit var spannable: SpannableStringBuilder

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
            return LocationViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.ent_search_city_adapter_item, parent, false))
        }

        override fun getItemCount(): Int = listLocation.size

        override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
            val location: LocationSuggestion = listLocation.get(position)

            holder.view.loc_name.text = getSpannableText(location.city, searchQuery)
            holder.view.loc_type.text = location.type

            holder.view.setOnClickListener {
                EventSearchPageTracking.getInstance().onClickLocationSuggestion(location,
                        listLocation, position+1)
                goToDetail(holder, location.city, location.id_city)
            }

            holder.view.addOnImpressionListener(location, {
                EventSearchPageTracking.getInstance().impressionCitySearchSuggestion(listLocation)
            })
        }

        private fun getSpannableText(city_full_text: String, text_to_bold: String):
                SpannableStringBuilder {
            spannable = SpannableStringBuilder(city_full_text)
            try {
                val startIndex = city_full_text.toLowerCase(Locale.US)
                        .indexOf(text_to_bold.toLowerCase(Locale.US)) + text_to_bold.length
                spannable.setSpan(StyleSpan(Typeface.BOLD), startIndex, city_full_text.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                if (startIndex - 1 - text_to_bold.length > -1)
                    spannable.setSpan(StyleSpan(Typeface.BOLD), 0, startIndex -
                            text_to_bold.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return spannable
        }

        fun goToDetail(holder: LocationViewHolder, query_text: String, id_city: String) {
            val intent = RouteManager.getIntent(holder.view.context,
                    Link.EVENT_CATEGORY + "?id_city={id_city}&query_text={query_text}",
                    id_city, query_text)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            holder.view.context.startActivity(intent)
        }
    }

    class LocationViewHolder(val view: View) : RecyclerView.ViewHolder(view)


}