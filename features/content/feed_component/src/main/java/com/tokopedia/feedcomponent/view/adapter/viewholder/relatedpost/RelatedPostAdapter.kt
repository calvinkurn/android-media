package com.tokopedia.feedcomponent.view.adapter.viewholder.relatedpost

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_related_post_child.view.*

/**
 * @author by milhamj on 2019-08-12.
 */
class RelatedPostAdapter(private val relatedPostList: List<FeedPostRelated.Datum>,
                         private val onPostClicked: (FeedPostRelated.Datum) -> Unit)
    : RecyclerView.Adapter<RelatedPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_related_post_child, parent, false)
        return ViewHolder(view, onPostClicked)
    }

    override fun getItemCount(): Int = relatedPostList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(relatedPostList[position])
    }

    class ViewHolder(v: View,
                     private val onPostClicked: (FeedPostRelated.Datum) -> Unit)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: FeedPostRelated.Datum) {
            with(itemView) {
                val content = element.content
                val firstMedia = content.body.media.firstOrNull()?.thumbnail ?: ""
                if (firstMedia.isNotEmpty()) {
                    image.loadImage(firstMedia)
                }
                val avatarBadge = content.header.avatarBadge
                if (avatarBadge.isNotEmpty()) {
                    kolBadge.loadImage(avatarBadge)
                    kolBadge.visible()
                } else {
                    kolBadge.gone()
                }
                name.text = content.header.avatarTitle

                description.text = MethodChecker.fromHtml(content.body.caption.text)

                setOnClickListener {
                    val applink = content.body.media.firstOrNull()?.applink ?: ""
                    if (applink.isNotEmpty()) {
                        onPostClicked.invoke(element)
                    }
                }
            }
        }
    }
}