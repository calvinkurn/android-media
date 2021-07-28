package com.tokopedia.travelhomepage.destination.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.travelhomepage.R
import com.tokopedia.travelhomepage.destination.listener.ActionListener
import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import kotlinx.android.synthetic.main.item_travel_destination_article.view.*

/**
 * @author by jessica on 2020-01-03
 */

class TravelDestinationArticleAdapter(private var list: List<TravelArticleModel.Item>,
                                      private var actionListener: ActionListener)
    : RecyclerView.Adapter<TravelDestinationArticleAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_travel_destination_article, parent, false), actionListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    fun updateList(list: List<TravelArticleModel.Item>) {
        this.list = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val actionListener: ActionListener) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: TravelArticleModel.Item, position: Int) {
            with(itemView) {
                iv_article.loadImage(item.imageUrl)
                tv_article_subtitle.text = item.publishedDate.toDate(DateUtil.YYYY_MM_DD_T_HH_MM_SS).toString(DateUtil.DEFAULT_VIEW_FORMAT)
                tv_article_title.text = item.title
                setOnClickListener {
                    actionListener.onTrackArticleItemClick(item, position)
                    actionListener.clickAndRedirect(item.appUrl, item.webUrl)
                }
            }
        }
    }
}