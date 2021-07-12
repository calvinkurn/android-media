package com.tokopedia.play.broadcaster.view.bottomsheet

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.model.ui.PlayLeaderboardUiModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder
import javax.inject.Inject


/**
 * Created by mzennis on 06/07/21.
 */
class PlayInteractiveLeaderBoardBottomSheet @Inject constructor() : BottomSheetDialogFragment() {

    private val leaderboardAdapter = PlayInteractiveLeaderboardAdapter(object : PlayInteractiveLeaderboardViewHolder.Listener{
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {

        }
    })

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.view_play_interactive_leaderboard,
            container,
            false
        )
        dialog?.let { setupDialog(it) }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
    }

    private fun setupView(view: View) {
        with(view) {
            val rvLeaderboard: RecyclerView = findViewById(com.tokopedia.play_common.R.id.rv_leaderboard)

            findViewById<TextView>(com.tokopedia.play_common.R.id.tv_sheet_title)
                .setText(com.tokopedia.play_common.R.string.play_interactive_leaderboard_title)

            findViewById<ImageView>(com.tokopedia.play_common.R.id.iv_sheet_close)
                .setOnClickListener {
                   dismiss()
                }

            rvLeaderboard.adapter = leaderboardAdapter

            /**
             * TODO: Mock
             */
            leaderboardAdapter.setItems(
                List(2) {
                    PlayLeaderboardUiModel(
                        title = listOf("Giveaway Kotak Pensil", "Giveaway LCD tv", "Giveaway CD Blackpink").random(),
                        winners = if (it%2 == 1) emptyList() else List(5) { child ->
                            PlayWinnerUiModel(
                                rank = child + 1,
                                id = "${child + 1}",
                                name = listOf("Nick", "Elon", "Selena", "Suzane", "Eggy").random(),
                                imageUrl = "https://static.nike.com/a/images/t_PDP_1728_v1/f_auto,b_rgb:f5f5f5/gueo3qthwrv8y5laemzs/joyride-run-flyknit-running-shoe-sqfqGQ.jpg",
                                allowChat = { true },
                                topChatMessage = "Selamat"
                            )
                        },
                        otherParticipantText =  if (it%2 == 1) "" else "Dari 100 peserta game"
                    )
                }
            )
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun setupDialog(dialog: Dialog) {
        dialog.setOnShowListener {
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val maxHeight = (HEIGHT_MULTIPLIER * getScreenHeight()).toInt()
            bottomSheet?.layoutParams = bottomSheet?.layoutParams?.apply {
                height = maxHeight
            }
            bottomSheet?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheet?.let {
                bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.peekHeight = maxHeight
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    companion object {
        private const val TAG = "PlayInteractiveLeaderBoardBottomSheet"
        private const val HEIGHT_MULTIPLIER = 0.80f
    }
}