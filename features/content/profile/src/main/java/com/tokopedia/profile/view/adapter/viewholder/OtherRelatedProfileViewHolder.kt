package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.OtherRelatedProfileViewModel
import kotlinx.android.synthetic.main.item_other_profile_post.view.*

class OtherRelatedProfileViewHolder(val v: View,
                                    val onOtherProfilePostItemClick: ((applink: String, authorId: String) -> Unit)) : AbstractViewHolder<OtherRelatedProfileViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_other_profile_post
    }

    override fun bind(element: OtherRelatedProfileViewModel) {
        with(itemView) {
            val content = element.feedPostRelatedDatum.content
            val firstMedia = content.body.media[0].thumbnail
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
                val applink = content.body.media[0].applink
                if (applink.isNotEmpty()) {
                    onOtherProfilePostItemClick.invoke(applink, content.tracking.authorID)
                }
            }
        }
    }

}