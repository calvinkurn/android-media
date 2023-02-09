package com.tokopedia.play.widget.ui.widget.large

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.getWidthAndHeight
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play_common.util.blur.ImageBlurUtil
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.coroutines.*

/**
 * Created By : Jonathan Darwin on February 09, 2023
 */
class PlayWidgetCardLargeTranscodeView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val thumbnail: ImageView
    private val totalViewBadge: View
    private val promoBadge: View
    private val tvTitle: TextView
    private val tvAuthor: TextView
    private val tvTotalView: TextView
    private val llWidgetContainer: LinearLayout
    private val llLoadingContainer: LinearLayout
    private val loaderLoading: LoaderUnify
    private val llError: LinearLayout
    private val btnErrorDelete: UnifyButton

    private val job = SupervisorJob()
    private val scope = CoroutineScope(job + Dispatchers.Main.immediate)

    private var mListener: Listener? = null

    private val imageBlurUtil = ImageBlurUtil(context)

    private val transcodingCoverTarget: CustomTarget<Bitmap>

    init {
        val view = View.inflate(context, R.layout.view_play_widget_card_large_transcode, this)
        thumbnail = view.findViewById(R.id.play_widget_thumbnail)
        totalViewBadge = view.findViewById(R.id.play_widget_badge_total_view)
        promoBadge = view.findViewById(R.id.play_widget_badge_promo)
        tvTitle = view.findViewById(R.id.play_widget_channel_title)
        tvAuthor = view.findViewById(R.id.play_widget_channel_name)
        tvTotalView = view.findViewById(com.tokopedia.play_common.R.id.viewer)
        llWidgetContainer = view.findViewById(R.id.play_widget_info_container)
        llLoadingContainer = view.findViewById(R.id.ll_loading_container)
        loaderLoading = view.findViewById(R.id.loader_loading)
        llError = view.findViewById(R.id.ll_error)
        btnErrorDelete = view.findViewById(R.id.btn_error_delete)

        transcodingCoverTarget = object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                scope.launch {
                    thumbnail.setImageBitmap(imageBlurUtil.blurImage(resource, BLUR_RADIUS))
                }
            }
        }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setData(data: PlayWidgetChannelUiModel) {

        layoutParams = ViewGroup.LayoutParams(
            data.gridType.getWidthAndHeight(context).first,
            data.gridType.getWidthAndHeight(context).second
        )

        Glide.with(thumbnail.context)
            .asBitmap()
            .load(data.video.coverUrl)
            .into(transcodingCoverTarget)

        when (data.channelType) {
            PlayWidgetChannelType.Transcoding -> setTranscodingModel(data)
            PlayWidgetChannelType.FailedTranscoding -> setFailedTranscodingModel(data)
            else -> {}
        }
    }

    private fun setTranscodingModel(model: PlayWidgetChannelUiModel) {
        totalViewBadge.visibility = if (model.totalView.isVisible) View.VISIBLE else View.GONE
        promoBadge.visibility = if (model.hasPromo) View.VISIBLE else View.GONE

        tvTitle.visibility = if (model.title.isNotEmpty()) View.VISIBLE else View.GONE
        tvAuthor.visibility = if (model.partner.name.isNotEmpty()) View.VISIBLE else View.GONE

        tvAuthor.text = model.partner.name
        tvTitle.text = model.title
        tvTotalView.text = model.totalView.totalViewFmt

        llWidgetContainer.visibility = View.VISIBLE
        llLoadingContainer.visibility = View.VISIBLE
        llError.visibility = View.GONE
    }

    private fun setFailedTranscodingModel(model: PlayWidgetChannelUiModel) {
        totalViewBadge.visibility = View.GONE
        promoBadge.visibility = View.GONE

        llWidgetContainer.visibility = View.GONE
        llLoadingContainer.visibility = View.GONE
        llError.visibility = View.VISIBLE

        btnErrorDelete.setOnClickListener {
            mListener?.onFailedTranscodingChannelDeleteButtonClicked(this, model)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        loaderLoading.visibility = loaderLoading.visibility
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        job.cancelChildren()
    }

    companion object {
        private const val BLUR_RADIUS = 20.0f
    }

    interface Listener {

        fun onFailedTranscodingChannelDeleteButtonClicked(
            view: View,
            item: PlayWidgetChannelUiModel,
        )
    }
}
