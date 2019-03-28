package com.tokopedia.kol.feature.post.view.adapter.viewholder

import android.view.View
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.Caption
import com.tokopedia.feedcomponent.data.pojo.template.templateitem.TemplateBody
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.DynamicPostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.grid.GridPostAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.image.ImagePostViewHolder
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.poll.PollAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.post.youtube.YoutubeViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.widget.CardTitleView
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction

import kotlinx.android.synthetic.main.item_dynamic_post.view.*
import kotlinx.android.synthetic.main.item_posttag.view.*
import kotlinx.android.synthetic.main.partial_card_title.view.*


/**
 * @author by nisie on 26/03/19.
 */
class KolPostDetailViewHolder(private val kolView: View,
                              private val listener: DynamicPostListener,
                              private val cardTitleListener: CardTitleView.CardTitleListener,
                              private val imagePostListener: ImagePostViewHolder.ImagePostListener,
                              private val youtubePostListener: YoutubeViewHolder.YoutubePostListener,
                              private val pollOptionListener: PollAdapter.PollOptionListener,
                              private val gridItemListener: GridPostAdapter.GridItemListener) :
        DynamicPostViewHolder(kolView, listener, cardTitleListener,
                imagePostListener, youtubePostListener, pollOptionListener, gridItemListener,
                DynamicPostViewHolder.TYPE_DETAIL) {


    override fun bind(element: DynamicPostViewModel?) {
        super.bind(element)
        val footer = kolView.findViewById<View>(R.id.footer)
        footer.visibility = View.GONE
    }

    override fun bindCaption(caption: Caption, template: TemplateBody) {
        itemView.caption.shouldShowWithAction(template.caption) {
            if (caption.text.isEmpty()) {
                itemView.caption.visibility = View.GONE
            } else {
                itemView.caption.text = caption.text.replace(NEWLINE, " ")
            }
        }

    }

}