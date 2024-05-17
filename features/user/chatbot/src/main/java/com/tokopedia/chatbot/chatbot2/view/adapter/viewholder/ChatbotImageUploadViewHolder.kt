package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ImageUploadUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.ImageUploadViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageUploadListener
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.util.generateLeftMessageBackgroundWithoutCorner
import com.tokopedia.chatbot.chatbot2.view.util.generateRightMessageBackgroundWithoutCorner
import com.tokopedia.chatbot.chatbot2.view.util.helper.ChatBotTimeConverter
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadSecureImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.abstraction.R as abstractionR
import com.tokopedia.chat_common.R as chat_commonR
import com.tokopedia.resources.common.R as resourcescommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ChatbotImageUploadViewHolder(
    itemView: View?,
    private val listener: ImageUploadListener,
    private val userSession: UserSessionInterface
) :
    ImageUploadViewHolder(itemView, listener) {

    override fun alwaysShowTime() = true
    override fun useWhiteReadStatus() = true
    override fun getImageId() = R.id.image
    override fun getProgressBarSendImageId() = R.id.progress_bar
    override fun getLeftActionId() = R.id.left_action
    override fun getChatBalloonId() = R.id.fl_image_container
    override fun getReadStatusId() = chat_commonR.id.chat_status

    private val datContainer: CardView? = itemView?.findViewById(R.id.dateContainer)
    private val cancelUpload = itemView?.findViewById<ImageView>(R.id.progress_cross)
    private val imageRetry = itemView?.findViewById<ImageUnify>(R.id.image_retry)
    private val imageShimmer = itemView?.findViewById<LoaderUnify>(R.id.image_shimmer)

    private val bgSender = generateRightMessageBackgroundWithoutCorner(
        chatBalloon
    )
    private val bgOpposite = generateLeftMessageBackgroundWithoutCorner(
        chatBalloon
    )

    private val attachmentUnify get() = attachment as? ImageUnify

    override fun bind(element: ImageUploadUiModel) {
        super.bind(element)
        chatStatus?.let { bindChatReadStatus(element, it) }
        bindBackground(element.isSender)
        cancelUpload?.setOnClickListener { listener.onImageUploadCancelClicked(element) }
        setHeaderDate(element)
    }

    private fun bindBackground(sender: Boolean) {
        if (sender) {
            chatBalloon?.background = bgSender
        } else {
            chatBalloon?.background = bgOpposite
        }
    }

    override fun bindClickListener(element: ImageUploadUiModel) {
        chatBalloon?.setOnClickListener { view ->
            val imageUrl = element.imageUrl.toEmptyStringIfNull()
            val replyTime = element.replyTime.toEmptyStringIfNull()
            if (imageUrl.isNotEmpty() && replyTime.isNotEmpty()) {
                listener.onImageUploadClicked(
                    imageUrl,
                    replyTime,
                    false
                )
            }
        }
    }

    override fun bindImageAttachment(element: ImageUploadUiModel) {
        changeHourColor(
            MethodChecker.getColor(
                itemView.context,
                unifyprinciplesR.color.Unify_NN0
            )
        )
        attachment?.scaleType = ImageView.ScaleType.CENTER_CROP
        if (element.isDummy) {
            setVisibility(progressBarSendImage, View.VISIBLE)
        } else {
            setVisibility(progressBarSendImage, View.GONE)
        }
        element.imageUrl?.let { imageUrl ->
            attachmentUnify?.let { attachementUnify ->
                loadImage(
                    attachementUnify,
                    imageUrl
                )
            }
        }
    }

    private fun loadImage(
        imageview: ImageView,
        url: String?
    ) {
        try {
            loadSecureImage(imageview, url)
        } catch (e: Exception) {
            if (imageview.context != null) {
                imageview.setImageDrawable(
                    ContextCompat.getDrawable(
                        imageview.context,
                        resourcescommonR.drawable.chatbot_image_placeloader
                    )
                )
            }
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun loadSecureImage(
        imageview: ImageView,
        url: String?
    ) {
        if (imageview.context != null) {
            if (url?.endsWith(GIF_EXTENSION, true) == true) {
                loadGif(imageview, url)
            } else {
                imageview.loadSecureImage(url, userSession)
            }
        }
    }

    private fun loadGif(imageview: ImageView, url: String) {
        imageRetry?.gone()
        imageShimmer?.show()
        imageview.loadAsGif(url) {
            setPlaceHolder(resourcescommonR.drawable.chatbot_image_placeloader)
            setErrorDrawable(abstractionR.drawable.error_drawable)
            listener(
                onSuccess = { _, _ ->
                    imageShimmer?.gone()
                },
                onSuccessGif = { gifDrawable, _ ->
                    imageShimmer?.gone()
                    gifDrawable?.setLoopCount(1)
                },
                onError = {
                    imageShimmer?.gone()
                    imageRetry?.visible()
                    imageRetry?.setOnClickListener {
                        loadGif(imageview, url)
                    }
                }
            )
        }
    }

    private fun bindChatReadStatus(element: ImageUploadUiModel, checkMark: ImageView) {
        if (element.isShowTime && element.isSender) {
            checkMark.show()
            val imageResource = when {
                element.isDummy -> chat_commonR.drawable.ic_chatcommon_check_rounded_grey
                else -> chat_commonR.drawable.ic_chatcommon_check_read_rounded_green
            }
            val drawable = MethodChecker.getDrawable(checkMark.context, imageResource)
            checkMark.setImageDrawable(drawable)
        } else {
            checkMark.gone()
        }
    }

    override fun setHeaderDate(element: BaseChatUiModel) {
        if (date == null) return
        val time = element.replyTime?.let {
            ChatBotTimeConverter.getDateIndicatorTime(
                it,
                itemView.context.getString(chat_commonR.string.chat_today_date),
                itemView.context.getString(chat_commonR.string.chat_yesterday_date)
            )
        }
        date?.text = time
        if (date != null && element.isShowDate && !TextUtils.isEmpty(time)
        ) {
            datContainer?.show()
        } else if (date != null) {
            datContainer?.hide()
        }
    }

    override val dateId: Int
        get() = R.id.date

    companion object {
        const val GIF_EXTENSION = ".gif"
        val LAYOUT = R.layout.item_chatbot_image_upload
    }
}
