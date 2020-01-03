package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.presentation.adapter.TravelDestinationArticleAdapter
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemBindListener
import com.tokopedia.travelhomepage.homepage.presentation.listener.OnItemClickListener
import kotlinx.android.synthetic.main.layout_travel_destination_article.view.*

/**
 * @author by jessica on 2020-01-03
 */

class TravelDestinationArticleViewHolder(itemView: View,
                                         private val onItemBindListener: OnItemBindListener,
                                         private val onItemClickListener: OnItemClickListener)
    : AbstractViewHolder<TravelArticleModel>(itemView){

    lateinit var adapter: TravelDestinationArticleAdapter

    override fun bind(element: TravelArticleModel) {
        if (element.isLoaded) {
            with(itemView) {
                tv_travel_destination_article_title.text = element.meta.title
                tv_travel_destination_article_see_all.text = "Lihat Semua"

                if (element.items.isNotEmpty()) {
                    iv_travel_destination_article_item.loadImage(element.items[0].imageUrl)
                    tv_travel_destination_article_item_title.text = element.items[0].title
                    tv_travel_destination_article_item_subtitle.text = element.items[0].publishedDate

                    if (!::adapter.isInitialized) {
                        adapter = TravelDestinationArticleAdapter(element.items.subList(1, element.items.size - 1))
                        rv_travel_destination_article_item.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
                        rv_travel_destination_article_item.adapter = adapter
                    } else {
                        adapter.updateList(element.items.subList(1, element.items.size - 1))
                    }
                }
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_travel_destination_article
    }
}