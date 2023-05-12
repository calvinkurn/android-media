package com.tokopedia.play.widget.ui.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieCompositionFactory
import com.tokopedia.play.widget.databinding.ItemPlayWidgetUpcomingContentBinding
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.R
import com.tokopedia.play.widget.ui.model.reminded

/**
 * Created by kenny.hadisaputra on 04/05/23
 */
class PlayWidgetUpcomingContentViewHolder(
    private val binding: ItemPlayWidgetUpcomingContentBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        setReminded(false)
    }
    fun bind(data: PlayWidgetChannelUiModel) {
        binding.tvStartTime.text = data.startTime
        binding.viewPlayWidgetCaption.root.text = data.title
        binding.viewPlayWidgetPartnerInfo.tvName.text = data.partner.name

        setReminded(data.reminderType.reminded)
        setRemindedListener(data)
    }

    fun bind(data: PlayWidgetChannelUiModel, payloads: Set<String>) {
        payloads.forEach {
            when (it) {
                PlayWidgetCarouselDiffCallback.PAYLOAD_REMINDED_CHANGE -> {
                    setReminded(data.reminderType.reminded, animate = true)
                    setRemindedListener(data)
                }
            }
        }
    }

    private fun setReminded(shouldRemind: Boolean, animate: Boolean = false) {
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
            listener.onReminderClicked(
                this,
                data,
                !data.reminderType.reminded,
            )
        }
    }

    companion object {

        fun create(parent: ViewGroup, listener: Listener): PlayWidgetUpcomingContentViewHolder {
            return PlayWidgetUpcomingContentViewHolder(
                ItemPlayWidgetUpcomingContentBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        }
    }

    interface Listener {
        fun onReminderClicked(
            viewHolder: PlayWidgetUpcomingContentViewHolder,
            data: PlayWidgetChannelUiModel,
            isReminded: Boolean,
        )
    }
}
