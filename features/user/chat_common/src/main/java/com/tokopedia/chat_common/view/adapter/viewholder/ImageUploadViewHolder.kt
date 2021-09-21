package com.tokopedia.chat_common.view.adapter.viewholder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.R
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener


/**
 * Created by stevenfredian on 11/28/17.
 */

open class ImageUploadViewHolder(itemView: View?, private val listener: ImageUploadListener)
    : BaseChatViewHolder<ImageUploadViewModel>(itemView) {

    protected val chatStatus: ImageView? = itemView?.findViewById(getReadStatusId())
    protected val chatBalloon: View? = itemView?.findViewById(getChatBalloonId())
    private val name: TextView? = itemView?.findViewById(getChatNameId())
    private val label: TextView? = itemView?.findViewById(getLabelId())
    private val dot: TextView? = itemView?.findViewById(getDotId())
    private val action: ImageView? = itemView?.findViewById(getLeftActionId())
    protected val progressBarSendImage: View? = itemView?.findViewById(getProgressBarSendImageId())
    protected val attachment: ImageView? = itemView?.findViewById(getImageId())

    protected open fun getImageId() = R.id.image
    protected open fun getChatNameId() = R.id.name
    protected open fun getLabelId() = R.id.label
    protected open fun getDotId() = R.id.dot
    protected open fun getProgressBarSendImageId() = R.id.progress_bar
    protected open fun getLeftActionId() = R.id.left_action
    protected open fun getReadStatusId() = R.id.chat_status
    protected open fun getChatBalloonId() = R.id.card_group_chat_message

    override fun bind(element: ImageUploadViewModel?) {
        if (element == null) return
        super.bind(element)
        prerequisiteUISetup(element)
        setupChatBubbleAlignment(chatBalloon, element)
        bindClickListener(element)
        bindImageAttachment(element)
        bindRetry(element)
        setVisibility(attachment, View.VISIBLE)
    }

    private fun bindRetry(element: ImageUploadViewModel) {
        if (element.isRetry) {
            setRetryView(element)
        }
    }

    private fun bindClickListener(element: ImageUploadViewModel) {
        view.setOnClickListener { view ->
            if (element.imageUrl != null && element.replyTime != null) {
                listener.onImageUploadClicked(element.imageUrl!!, element.replyTime!!)
            }
        }
    }

    protected open fun bindImageAttachment(element: ImageUploadViewModel) {
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
            ImageHandler.loadImageBlurredWithListener(
                    attachment,
                    element.imageUrl,
                    BLUR_WIDTH,
                    BLUR_HEIGHT,
                    DynamicSizeImageRequestListener()
            )
        } else {
            setVisibility(progressBarSendImage, View.GONE)
            ImageHandler.loadImageWithListener(
                    attachment,
                    element.imageUrl,
                    DynamicSizeImageRequestListener()
            )
        }
    }

    private fun setupChatBubbleAlignment(chatBalloon: View?, element: ImageUploadViewModel) {
        if (element.isSender) {
            setChatRight(chatBalloon)
            bindChatReadStatus(element)
        } else {
            setChatLeft(chatBalloon)
        }
    }

    private fun setChatLeft(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_LEFT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_LEFT, hour)
        setVisibility(chatStatus, View.GONE)
        setVisibility(name, View.GONE)
        setVisibility(label, View.GONE)
        setVisibility(dot, View.GONE)
    }

    private fun setChatRight(chatBalloon: View?) {
        setAlignParent(RelativeLayout.ALIGN_PARENT_RIGHT, chatBalloon)
        alignHour(RelativeLayout.ALIGN_PARENT_RIGHT, hour)
        setVisibility(chatStatus, View.VISIBLE)
    }

    protected open fun alignHour(alignment: Int, hour: TextView) {
        setAlignParent(alignment, hour)
    }

    private fun setAlignParent(alignment: Int, view: View?) {
        if (view?.layoutParams !is RelativeLayout.LayoutParams) return
        val params = view.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0)
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0)
        params.addRule(alignment)
        view.layoutParams = params
    }

    private fun setRetryView(element: ImageUploadViewModel) {
        setVisibility(action, View.VISIBLE)
        setVisibility(hour, View.GONE)
        setVisibility(chatStatus, View.GONE)
        setVisibility(progressBarSendImage, View.GONE)
        action?.setOnClickListener {
            listener.onRetrySendImage(element)
        }
    }

    @SuppressLint("DefaultLocale")
    protected fun prerequisiteUISetup(element: ImageUploadViewModel) {
        action?.visibility = View.GONE
        progressBarSendImage?.visibility = View.GONE
        if (!TextUtils.isEmpty(element.fromRole)
                && element.fromRole.toLowerCase() != ROLE_USER.toLowerCase()
                && element.isSender
                && !element.isDummy
                && element.isShowRole) {
            if (name != null) name.text = element.from
            if (label != null) label.text = element.fromRole
            setVisibility(name, View.VISIBLE)
            setVisibility(dot, View.VISIBLE)
            setVisibility(label, View.VISIBLE)
        } else {
            setVisibility(name, View.GONE)
            setVisibility(dot, View.GONE)
            setVisibility(label, View.GONE)
        }
    }

    protected fun setVisibility(view: View?, visibility: Int) {
        if (view != null) {
            view.visibility = visibility
        }
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        if (attachment != null) {
            ImageHandler.clearImage(attachment)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_image_upload
        private val ROLE_USER = "User"
        private val BLUR_WIDTH = 30
        private val BLUR_HEIGHT = 30
    }
}
