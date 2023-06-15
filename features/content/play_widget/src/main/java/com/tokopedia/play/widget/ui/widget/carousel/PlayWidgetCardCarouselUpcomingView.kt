package com.tokopedia.play.widget.ui.widget.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
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
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded
import com.tokopedia.play.widget.ui.model.switch

/**
 * Created by kenny.hadisaputra on 17/05/23
 */
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

    init {
        preloadLottie()
        showReminderButton(false, animate = false)
    }

    fun setModel(model: PlayWidgetChannelUiModel, invalidate: Boolean = true) {
        this.mModel = model
        if (invalidate) invalidateUi(model)
    }

    private fun invalidateUi(model: PlayWidgetChannelUiModel) {
        binding.tvStartTime.text = model.startTime
        binding.viewPlayWidgetCaption.root.text = model.title
        binding.imgCover.loadImage(model.video.coverUrl)

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

    fun setUnClickable(isUnClickable: Boolean) {
        binding.viewPlayWidgetNoClick.root.showWithCondition(isUnClickable)
    }

    fun showReminderButton(shouldShow: Boolean, animate: Boolean = true) {
        if (animate) {
            TransitionManager.beginDelayedTransition(
                binding.root,
                AutoTransition()
                    .addTarget(binding.viewPlayWidgetActionButton.root)
            )
        }

        binding.viewPlayWidgetActionButton.root.showWithCondition(shouldShow)
    }

    fun setReminded(shouldRemind: Boolean, animate: Boolean = false) {
        val lottieUrl = context.getString(
            if (shouldRemind) R.string.lottie_reminder_off_on
            else R.string.lottie_reminder_on_off
        )

        LottieCompositionFactory.fromUrl(context, lottieUrl)
            .addFailureListener {
                binding.viewPlayWidgetActionButton.lottieAction.hide()

                binding.viewPlayWidgetActionButton.iconActionFallback.setImage(
                    if (shouldRemind) IconUnify.BELL_FILLED else IconUnify.BELL
                )
                binding.viewPlayWidgetActionButton.iconActionFallback.show()
            }
            .addListener { composition ->
                binding.viewPlayWidgetActionButton.iconActionFallback.hide()

                binding.viewPlayWidgetActionButton.lottieAction.setComposition(composition)
                binding.viewPlayWidgetActionButton.lottieAction.show()

                if (animate) binding.viewPlayWidgetActionButton.lottieAction.playAnimation()
                else binding.viewPlayWidgetActionButton.lottieAction.progress = 1f
            }

        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            mListener?.onReminderClicked(
                this,
                mModel,
                mModel.reminderType.switch(),
            )
        }
    }

    private fun preloadLottie() {
        listOf(
            R.string.lottie_reminder_off_on,
            R.string.lottie_reminder_on_off
        ).forEach {
            LottieCompositionFactory.fromUrl(context, context.getString(it))
        }
    }

    interface Listener {
        fun onReminderClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
        )

        fun onPartnerClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
        )
    }
}
