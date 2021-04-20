package com.tokopedia.play.widget.ui.adapter.viewholder.medium

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetMediumChannelUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*

/**
 * Created by jegul on 04/11/20
 */
class PlayWidgetCardMediumTranscodeViewHolder(
        itemView: View,
        private val imageBlurUtil: ImageBlurUtil,
        private val listener: Listener
) : RecyclerView.ViewHolder(itemView) {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main.immediate)

    private val thumbnail: ImageView = itemView.findViewById(R.id.play_widget_thumbnail)
    private val totalViewBadge: View = itemView.findViewById(R.id.play_widget_badge_total_view)
    private val promoBadge: View = itemView.findViewById(R.id.play_widget_badge_promo)
    private val tvTitle: TextView = itemView.findViewById(R.id.play_widget_channel_title)
    private val tvAuthor: TextView = itemView.findViewById(R.id.play_widget_channel_name)
    private val tvTotalView: TextView = itemView.findViewById(R.id.viewer)
    private val llWidgetContainer: LinearLayout = itemView.findViewById(R.id.play_widget_info_container)
    private val llLoadingContainer: LinearLayout = itemView.findViewById(R.id.ll_loading_container)
    private val loaderLoading: LoaderUnify = itemView.findViewById(R.id.loader_loading)
    private val llError: LinearLayout = itemView.findViewById(R.id.ll_error)
    private val btnErrorDelete: UnifyButton = itemView.findViewById(R.id.btn_error_delete)

    private val transcodingCoverTarget = object : CustomTarget<Bitmap>() {
        override fun onLoadCleared(placeholder: Drawable?) {

        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            scope.launch {
                thumbnail.setImageBitmap(imageBlurUtil.blurImage(resource, BLUR_RADIUS))
            }
        }
    }

    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) {
                job.cancelChildren()
            }

            override fun onViewAttachedToWindow(v: View?) {
                loaderLoading.visibility = loaderLoading.visibility
            }
        })
    }

    fun bind(item: PlayWidgetMediumChannelUiModel) {
        Glide.with(thumbnail.context)
                .asBitmap()
                .load(item.video.coverUrl)
                .into(transcodingCoverTarget)

        when (item.channelType) {
            PlayWidgetChannelType.Transcoding -> setTranscodingModel(item)
            PlayWidgetChannelType.FailedTranscoding -> setFailedTranscodingModel(item)
            else -> {}
        }
    }

    private fun setTranscodingModel(model: PlayWidgetMediumChannelUiModel) {
        totalViewBadge.visibility = if (model.totalViewVisible) View.VISIBLE else View.GONE
        promoBadge.visibility = if (model.hasPromo) View.VISIBLE else View.GONE

        tvTitle.visibility = if (model.title.isNotEmpty()) View.VISIBLE else View.GONE
        tvAuthor.visibility = if (model.partner.name.isNotEmpty()) View.VISIBLE else View.GONE

        tvAuthor.text = model.partner.name
        tvTitle.text = model.title
        tvTotalView.text = model.totalView

        llWidgetContainer.visibility = View.VISIBLE
        llLoadingContainer.visibility = View.VISIBLE
        llError.visibility = View.GONE
    }

    private fun setFailedTranscodingModel(model: PlayWidgetMediumChannelUiModel) {
        totalViewBadge.visibility = View.GONE
        promoBadge.visibility = View.GONE

        llWidgetContainer.visibility = View.GONE
        llLoadingContainer.visibility = View.GONE
        llError.visibility = View.VISIBLE

        btnErrorDelete.setOnClickListener {
            listener.onFailedTranscodingChannelDeleteButtonClicked(itemView, model, adapterPosition)
        }
    }

    companion object {
        @LayoutRes
        val layoutRes = R.layout.item_play_widget_card_transcode_medium

        private const val BLUR_RADIUS = 20.0f
    }

    interface Listener {

        fun onFailedTranscodingChannelDeleteButtonClicked(
                view: View,
                item: PlayWidgetMediumChannelUiModel,
                position: Int
        )
    }
}