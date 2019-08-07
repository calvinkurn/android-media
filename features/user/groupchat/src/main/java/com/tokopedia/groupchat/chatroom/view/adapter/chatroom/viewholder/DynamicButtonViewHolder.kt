package com.tokopedia.groupchat.chatroom.view.adapter.chatroom.viewholder

import android.support.annotation.LayoutRes
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.widget.ViewTooltip
import com.tokopedia.groupchat.R
import com.tokopedia.groupchat.chatroom.view.adapter.chatroom.typefactory.DynamicButtonTypeFactory
import com.tokopedia.groupchat.chatroom.view.listener.ChatroomContract
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButton
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Action1
import java.util.concurrent.TimeUnit

/**
 * @author : Steven 22/05/19
 */

class DynamicButtonViewHolder(
        itemView: View,
        var listener: ChatroomContract.DynamicButtonItem.DynamicButtonListener
) : BaseDynamicButtonViewHolder<Visitable<DynamicButtonTypeFactory>>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_icon_item
    }

    var icon: ImageView
    var notification: View
    val TOOLTIP_DURATION_DEFAULT = 3

    init {
        icon = itemView.findViewById(R.id.icon)
        notification = itemView.findViewById(R.id.notification)
    }
    
    override fun bind(e: Visitable<DynamicButtonTypeFactory>) {
        var element = e as DynamicButton
        ImageHandler.loadImage(icon.context, icon, element.imageUrl, R.drawable.ic_play_dynamic_icon)
        icon.setOnClickListener {
            listener.onDynamicButtonClicked(element)
            element.hasNotification = false
            notification.visibility = View.INVISIBLE
        }

        if (element.hasNotification) {
            notification.visibility = View.VISIBLE
        } else {
            notification.visibility = View.INVISIBLE
        }

        if (element.tooltip.isNotBlank()) {
            if(element.tooltipDuration == 0) {
                element.tooltipDuration = TOOLTIP_DURATION_DEFAULT
            }

            var timeToShow = element.tooltipDuration
            var intervalToHideMs = ((element.tooltipDuration - 1)*1000).toLong()
            var index = if(element.priority > 0) element.priority-1 else position

            val showTooltip = Action1<Long> {
                ViewTooltip.on(icon)
                        .autoHide(true, intervalToHideMs)
                        .corner(30)
                        .clickToHide(false)
                        .color(MethodChecker.getColor(icon.context, R.color.white))
                        .textColor(MethodChecker.getColor(icon.context, R.color.black_70))
                        .position(ViewTooltip.Position.TOP)
                        .text(element.tooltip)
                        .textSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        .show()
            }

            val onError = Action1<Throwable> { it.printStackTrace() }

            Observable.timer((timeToShow*index).toLong(), TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(showTooltip, onError)
        }
    }
}