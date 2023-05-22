package com.tokopedia.play.widget.ui.widget.carousel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.databinding.ViewPlayWidgetCardCarouselUpcomingBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.reminded

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
        showReminderButton(false, animate = false)
    }

    fun setModel(model: PlayWidgetChannelUiModel, invalidate: Boolean = true) {
        this.mModel = model
        if (invalidate) invalidateUi(model)
    }

    private fun invalidateUi(model: PlayWidgetChannelUiModel) {
        binding.tvStartTime.text = model.startTime
        binding.viewPlayWidgetCaption.root.text = model.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = model.partner.name
        binding.imgCover.setImageUrl(model.video.coverUrl)

        binding.viewPlayWidgetPartnerInfo.tvName.text = model.partner.name
        binding.viewPlayWidgetPartnerInfo.imgAvatar.setImageUrl(model.partner.avatarUrl)
        if (model.partner.badgeUrl.isNullOrBlank()) {
            binding.viewPlayWidgetPartnerInfo.imgBadge.hide()
        } else {
            binding.viewPlayWidgetPartnerInfo.imgBadge.setImageUrl(model.partner.badgeUrl)
            binding.viewPlayWidgetPartnerInfo.imgBadge.show()
        }

        setReminded(model.reminderType.reminded)
        setRemindedListener(model)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
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
        val lottieComposition = LottieCompositionFactory.fromRawRes(
            binding.root.context,
            if (shouldRemind) R.raw.play_widget_lottie_reminder_off_on
            else R.raw.play_widget_lottie_reminder_on_off
        )

        lottieComposition.addListener { composition ->
            binding.viewPlayWidgetActionButton.root.setComposition(composition)

            if (animate) binding.viewPlayWidgetActionButton.root.playAnimation()
            else binding.viewPlayWidgetActionButton.root.progress = 1f
        }
    }

    private fun setRemindedListener(data: PlayWidgetChannelUiModel) {
        binding.viewPlayWidgetActionButton.root.setOnClickListener {
            mListener?.onReminderClicked(
                this,
                data,
                data.reminderType,
            )
        }
    }

    interface Listener {
        fun onReminderClicked(
            view: PlayWidgetCardCarouselUpcomingView,
            item: PlayWidgetChannelUiModel,
            reminderType: PlayWidgetReminderType,
        )
    }
}
