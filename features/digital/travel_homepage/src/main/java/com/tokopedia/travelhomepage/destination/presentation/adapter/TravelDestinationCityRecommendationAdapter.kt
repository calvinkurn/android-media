package com.tokopedia.travelhomepage.destination.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.ActionListener
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionViewModel
import kotlinx.android.synthetic.main.item_travel_destination_recommendation.view.*

/**
 * @author by jessica on 2019-08-09
 */

class TravelDestinationCityRecommendationAdapter(private var list: List<TravelDestinationSectionViewModel.Item>,
                                                 private val actionListener: ActionListener) :
        RecyclerView.Adapter<TravelDestinationCityRecommendationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, itemViewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_travel_destination_recommendation, parent, false),
                actionListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun updateList(newList: List<TravelDestinationSectionViewModel.Item>) {
        this.list = newList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: TravelDestinationSectionViewModel.Item, position: Int) {
            with(itemView) {
                iv_travel_destination_recommendation_item_category.loadImage(item.imageUrl)

                if (item.subtitle.isNotEmpty()) tv_travel_destination_recommendation_item_subtitle.text = item.subtitle
                else tv_travel_destination_recommendation_item_subtitle.visibility = View.GONE

                tv_travel_destination_recommendation_item_title.text = item.title
                tv_travel_destination_recommendation_item_desc_subtitle.text = item.prefix
                tv_travel_destination_recommendation_item_desc_title.text = item.value

                setOnClickListener { actionListener.clickAndRedirect(item.appUrl) }
            }
        }

        companion object {
            val LAYOUT = R.layout.item_travel_destination_recommendation
        }
    }
}