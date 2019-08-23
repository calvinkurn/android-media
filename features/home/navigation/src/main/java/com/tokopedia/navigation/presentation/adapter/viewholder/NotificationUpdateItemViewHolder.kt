package com.tokopedia.navigation.presentation.adapter.viewholder

import android.graphics.drawable.GradientDrawable
import android.support.annotation.LayoutRes
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.navigation.R
import com.tokopedia.navigation.presentation.view.listener.NotificationUpdateItemListener
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel.Companion.BUYER_TYPE
import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel.Companion.SELLER_TYPE


/**
 * @author : Steven 10/04/19
 */
class NotificationUpdateItemViewHolder(itemView: View, var listener: NotificationUpdateItemListener)
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
    private val label: TextView

    init {
        container = itemView.findViewById(R.id.container)
        icon = itemView.findViewById(R.id.icon)
        title = itemView.findViewById(R.id.title)
        body = itemView.findViewById(R.id.body)
        contentImage = itemView.findViewById(R.id.image)
        type = itemView.findViewById(R.id.type)
        time = itemView.findViewById(R.id.time)
        label = itemView.findViewById(R.id.label)
    }

    override fun bind(element: NotificationUpdateItemViewModel) {
        var color: Int =
                if (element.isRead) MethodChecker.getColor(container.context, R.color.white)
                else MethodChecker.getColor(container.context, R.color.Green_G100)

        container.setBackgroundColor(color)
        if (element.contentUrl.isNotBlank()) {
            ImageHandler.loadImage2(contentImage, element.contentUrl, R.drawable.ic_loading_toped_new)
        }
        ImageHandler.loadImage2(icon, element.iconUrl, R.drawable.ic_loading_toped_new)
        title.text = element.title
        body.text = element.body
        time.text = element.time

        type.text = element.sectionTitle

        convertTypeUser(element.label)

        container.setOnClickListener {
            listener.itemClicked(element.notificationId, adapterPosition, !element.isRead, element.templateKey)
            element.isRead = true
            RouteManager.route(itemView.context, element.appLink)
        }
    }

    private fun convertTypeUser(labelIndex: Int) {
        label.visibility = View.GONE

        if (labelIndex == BUYER_TYPE) {
            getStringResource(R.string.buyer_label)?.apply {
                label.text = this
                label.setTextColor(getColorResource(R.color.Neutral_N200))
                label.visibility = View.VISIBLE
            }

            label.background.let {
                if (it is GradientDrawable) {
                    it.setColor(getColorResource(R.color.Neutral_N50))
                }
            }
        } else if (labelIndex == SELLER_TYPE) {
            getStringResource(R.string.seller_label)?.apply {
                label.text = this
                label.setTextColor(getColorResource(R.color.Green_G500))
                label.visibility = View.VISIBLE
            }

            label.background.let {
                if (it is GradientDrawable) {
                    it.setColor(getColorResource(R.color.Green_G200))
                }
            }
        }
    }

    private fun getStringResource(stringId: Int): String? {
        return itemView.context?.getString(stringId)
    }

    private fun getColorResource(colorId: Int): Int {
        return MethodChecker.getColor(itemView.context, colorId)
    }


}