package com.tokopedia.feedcomponent.view.adapter.viewholder.relatedpost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.R
import com.tokopedia.feedcomponent.data.pojo.FeedPostRelated
import com.tokopedia.feedcomponent.view.viewmodel.relatedpost.RelatedPostItemViewModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import kotlinx.android.synthetic.main.item_related_post_child.view.*

/**
 * @author by milhamj on 2019-08-12.
 */
class RelatedPostAdapter(private val relatedPostList: List<RelatedPostItemViewModel>,
                         private val listener: RelatedPostListener)
    : RecyclerView.Adapter<RelatedPostAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_related_post_child, parent, false)
        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int = relatedPostList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(relatedPostList[position])
    }

    class ViewHolder(v: View,
                     private val listener: RelatedPostListener)
        : RecyclerView.ViewHolder(v) {

        fun bind(element: RelatedPostItemViewModel) {
            with(itemView) {
                val content = element.data.content
                val firstMedia = content.body.media.firstOrNull()?.thumbnail ?: ""
                videoPreviewImage.loadImage(firstMedia)
                videoPreviewImage.addOnImpressionListener(element.impressionHolder) {
                    listener.onRelatedPostImpression(element.data)
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
                        listener.onRelatedPostClicked(element.data)
                    }
                }
            }
        }
    }

    interface RelatedPostListener {
        fun onRelatedPostClicked(post: FeedPostRelated.Datum)

        fun onRelatedPostImpression(post: FeedPostRelated.Datum)
    }
}