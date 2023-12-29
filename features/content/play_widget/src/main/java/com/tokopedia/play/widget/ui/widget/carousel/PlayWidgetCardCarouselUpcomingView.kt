package com.tokopedia.play.widget.ui.widget.carousel

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardCarouselUpcomingBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.model.switch

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
@Suppress("LateinitUsage")
class PlayWidgetCardCarouselUpcomingView : FrameLayout {

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

    private val binding = ViewPlayWidgetCardCarouselUpcomingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private lateinit var mModel: PlayWidgetChannelUiModel

    private var mListener: Listener? = null

    private val offset12 = resources.getDimensionPixelOffset(R.dimen.play_widget_dp_12)

    private val downloadedLottieSet = mutableSetOf<Int>()

    init {
        preloadLottie()
        showReminderButton(false, animate = false)

        binding.viewPlayWidgetOverlay.root.setOnClickListener {
            mListener?.onOverlayClicked(this, mModel)
        }

        binding.viewPlayWidgetActionButton.root.doOnLayout {
            val parent = it.parent
            if (parent !is ViewGroup) return@doOnLayout

            val rect = Rect()
            it.getHitRect(rect)
            rect.inset(-offset12, -offset12)

            parent.touchDelegate = TouchDelegate(rect, it)
        }
    }

    fun setModel(model: PlayWidgetChannelUiModel, invalidate: Boolean = true) {
        this.mModel = model
        if (invalidate) invalidateUi(model)
    }

    private fun invalidateUi(model: PlayWidgetChannelUiModel) {
        binding.tvStartTime.text = model.startTime
        binding.viewPlayWidgetCaption.root.text = model.title

        binding.imgCover.scaleType = ImageView.ScaleType.CENTER
        binding.imgCover.loadImage(model.video.coverUrl) {
            listener(
                onSuccess = { _, _ -> binding.imgCover.scaleType = ImageView.ScaleType.CENTER_CROP }
            )
        }

        binding.viewPlayWidgetPartnerInfo.tvName.text = model.partner.name
        binding.viewPlayWidgetPartnerInfo.imgAvatar.loadImage(model.partner.avatarUrl)
        if (model.partner.badgeUrl.isBlank()) {
            binding.viewPlayWidgetPartnerInfo.imgBadge.hide()
        } else {
            binding.viewPlayWidgetPartnerInfo.imgBadge.setImageUrl(model.partner.badgeUrl)
            binding.viewPlayWidgetPartnerInfo.imgBadge.show()
        }
        binding.viewPlayWidgetPartnerInfo.root.setOnClickListener {
            mListener?.onPartnerClicked(this, model)
        }

        setReminded(model.reminderType.reminded)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun setShowOverlay(isOverlayShown: Boolean) {
        binding.viewPlayWidgetOverlay.root.showWithCondition(isOverlayShown)
    }

    fun showReminderButton(shouldShow: Boolean, animate: Boolean = true) {
        if (animate) {
            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition()
                    .addTarget(binding.viewPlayWidgetActionButton.root)
                    .setDuration(DURATION_ACTION_TRANSITION)
            )
        }

        binding.viewPlayWidgetActionButton.root.showWithCondition(shouldShow)
    }

    fun setReminded(shouldRemind: Boolean, animate: Boolean = false) {
        val lottieRes = if (shouldRemind) {
            R.string.lottie_reminder_off_on
        } else {
            R.string.lottie_reminder_on_off
        }

        if (!downloadedLottieSet.contains(lottieRes) || !animate) {
            binding.viewPlayWidgetActionButton.lottieAction.hide()
            binding.viewPlayWidgetActionButton.iconActionFallback.setImage(
                if (shouldRemind) IconUnify.BELL_FILLED else IconUnify.BELL
            )
            binding.viewPlayWidgetActionButton.iconActionFallback.show()
        } else {
            LottieCompositionFactory.fromUrl(context, context.getString(lottieRes))
                .addListener { composition ->
                    binding.viewPlayWidgetActionButton.iconActionFallback.hide()
                    binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                    binding.viewPlayWidgetActionButton.lottieAction.show()
                    binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
                }
        }

        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            mListener?.onReminderClicked(
                this,
                mModel,
                mModel.reminderType.switch()
            )
        }
    }

    private fun preloadLottie() {
        listOf(
            R.string.lottie_reminder_off_on,
            R.string.lottie_reminder_on_off
        ).forEach { res ->
            LottieCompositionFactory.fromUrl(context, context.getString(res))
                .addListener {
                    downloadedLottieSet.add(res)
                }
        }
    }

    companion object {
        private const val DURATION_ACTION_TRANSITION = 200L
    }

    interface Listener {
        fun onReminderClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType
        )

        fun onPartnerClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel
        )

        fun onOverlayClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel
        )
    }
}
