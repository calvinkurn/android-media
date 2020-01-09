package com.tokopedia.travelhomepage.destination.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.OnClickListener
import com.tokopedia.travelhomepage.destination.listener.OnViewHolderBindListener
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.presentation.adapter.TravelDestinationArticleAdapter
import kotlinx.android.synthetic.main.layout_travel_destination_article.view.*

/**
 * @author by jessica on 2020-01-03
 */

class TravelDestinationArticleViewHolder(itemView: View, private val onViewHolderBindListener: OnViewHolderBindListener,
                                         private val onClickListener: OnClickListener)
    : AbstractViewHolder<TravelArticleModel>(itemView){

    lateinit var adapter: TravelDestinationArticleAdapter

    override fun bind(element: TravelArticleModel) {
        if (element.isLoaded) {
            with(itemView) {
                if (element.isSuccess) {
                    tv_travel_destination_article_title.text = element.meta.title
                    tv_travel_destination_article_see_all.text = "Lihat Semua"
                    setOnClickListener { onClickListener.clickAndRedirect(element.meta.appUrl, element.meta.webUrl) }

                    if (element.items.isNotEmpty()) {
                        iv_travel_destination_article_item.loadImage(element.items[0].imageUrl)
                        tv_travel_destination_article_item_title.text = element.items[0].title
                        tv_travel_destination_article_item_subtitle.text = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS, element.items[0].publishedDate))
                        travel_destination_highlight_article.setOnClickListener { onClickListener.clickAndRedirect(element.items[0].appUrl,
                                element.items[0].webUrl) }

                        if (!::adapter.isInitialized) {
                            adapter = TravelDestinationArticleAdapter(element.items.subList(1, element.items.size - 1), onClickListener)
                            rv_travel_destination_article_item.layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
                            rv_travel_destination_article_item.adapter = adapter
                        } else {
                            adapter.updateList(element.items.subList(1, element.items.size - 1))
                        }
                    }
                } else {
                    hide()
                }
            }
        } else {
            onViewHolderBindListener.onCityArticleVHBind()
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_travel_destination_article
    }
}