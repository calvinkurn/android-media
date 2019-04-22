package com.tokopedia.navigation.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel


/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateItemViewHolder(itemView: View)
    : AbstractViewHolder<NotificationUpdateItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.notification_update_default_item
    }

    private val container: ConstraintLayout
    private val icon: ImageView
    private val title: TextView
    private val body: TextView
    private val contentImage: ImageView
    private val type: TextView
    private val time: TextView

    init {
        container = itemView.findViewById(R.id.container)
        icon = itemView.findViewById(R.id.icon)
        title = itemView.findViewById(R.id.title)
        body = itemView.findViewById(R.id.body)
        contentImage = itemView.findViewById(R.id.image)
        type = itemView.findViewById(R.id.type)
        time = itemView.findViewById(R.id.time)
    }

    override fun bind(element: NotificationUpdateItemViewModel) {
        var color: Int =
            if (element.isRead) MethodChecker.getColor(container.context, R.color.white)
            else MethodChecker.getColor(container.context, R.color.read_yet)

        container.setBackgroundColor(color)
        ImageHandler.loadImage2(contentImage, element.contentUrl, R.drawable.ic_loading_toped_new)
        title.text = element.title
        body.text = element.body
        time.text = element.time
        type.text = element.sectionTitle
    }
}