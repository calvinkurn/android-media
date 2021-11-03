package com.tokopedia.play_common.ui.leaderboard.viewholder

import android.view.View
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play_common.R
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode

/**
 * Created by mzennis on 30/06/21.
 */
class PlayInteractiveWinnerViewHolder(itemView: View, private val listener: Listener) : BaseViewHolder(itemView) {

    private val container = itemView.findViewById<ContainerUnify>(R.id.container_winner)
    private val tvNumber = itemView.findViewById<Typography>(R.id.tv_winner_number)
    private val tvName = itemView.findViewById<Typography>(R.id.tv_winner_name)
    private val lblWinner = itemView.findViewById<Label>(R.id.lbl_winner)
    private val iconChat = itemView.findViewById<IconUnify>(R.id.icon_winner_chat)
    private val ivCrown = itemView.findViewById<ImageUnify>(R.id.img_crown)
    private val ivWinner = itemView.findViewById<ImageUnify>(R.id.img_winner)
    private val borderIvWinner = itemView.findViewById<FrameLayout>(R.id.fl_border_img_winner)

    fun bind(winner: PlayWinnerUiModel) {
        tvNumber.text = winner.rank.toString()
        tvName.text = winner.name
        ivWinner.setImageUrl(winner.imageUrl)

        handleFirstWinner(winner)

        if (winner.allowChat()) {
            iconChat.show()
            iconChat.setOnClickListener { listener.onChatButtonClicked(winner, adapterPosition) }
        } else {
            iconChat.hide()
            iconChat.setOnClickListener(null)
        }
    }

    private fun handleFirstWinner(winner: PlayWinnerUiModel) {
        if (winner.rank == FIRST_WINNER) {
            ivCrown.show()
            borderIvWinner.show()
            lblWinner.show()

            if (container.context.isDarkMode()) {
                container.setCustomContainerColor(
                    Pair(
                        MethodChecker.getColor(
                            container.context,
                            R.color.play_dms_leaderboard_winner_dark_color_1
                        ),
                        MethodChecker.getColor(
                            container.context,
                            R.color.play_dms_leaderboard_winner_dark_color_2
                        )
                    )
                )
            } else {
                container.setContainerColor(ContainerUnify.YELLOW)
            }
        } else {
            ivCrown.hide()
            borderIvWinner.hide()
            lblWinner.hide()
            val containerColor = MethodChecker.getColor(
                container.context,
                R.color.play_dms_bottom_sheet_background
            )
            container.setCustomContainerColor(
                Pair(containerColor, containerColor)
            )
        }
    }

    interface Listener {
        fun onChatButtonClicked(item: PlayWinnerUiModel, position: Int)
    }

    companion object {

        val LAYOUT = R.layout.item_play_interactive_leaderboard_winner

        private const val FIRST_WINNER = 1
    }
}