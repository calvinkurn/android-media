package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.EmptyNotificationUiModel
import com.tokopedia.unifyprinciples.Typography

class EmptyNotificationViewHolder(
    itemView: View?
) : AbstractViewHolder<EmptyNotificationUiModel>(itemView) {

    private val image: ImageView? = itemView?.findViewById(R.id.iv_icon)
    private val emptyFilterText: Typography? = itemView?.findViewById(R.id.tp_empty_filter)
    private val emptyTitleText: Typography? =
        itemView?.findViewById(R.id.tp_empty_notification_title)

    override fun bind(element: EmptyNotificationUiModel) {
        bindImage(element)
        if (element.description.isNullOrEmpty()) {
            bindEmptyFilterText(element)
        } else {
            emptyFilterText?.show()
            emptyTitleText?.text = element.title
            emptyFilterText?.text = element.description
        }
    }

    private fun bindImage(element: EmptyNotificationUiModel) {
        ImageHandler.LoadImage(image, emptyImageUrl)
    }

    private fun bindEmptyFilterText(element: EmptyNotificationUiModel) {
        emptyFilterText?.showWithCondition(element.hasFilter)
    }

    companion object {
        val LAYOUT = R.layout.item_notification_empty
        const val emptyImageUrl = TokopediaImageUrl.NOTIF_EMPTY_IMAGE_URL
    }
}
