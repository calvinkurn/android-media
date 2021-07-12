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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder
import javax.inject.Inject
import com.tokopedia.play_common.R as commonR


/**
 * Created by mzennis on 06/07/21.
 */
class PlayInteractiveLeaderBoardBottomSheet @Inject constructor(
    private val viewModelFactory: ViewModelFactory,
) : BottomSheetDialogFragment() {

    private val leaderboardAdapter = PlayInteractiveLeaderboardAdapter(object : PlayInteractiveLeaderboardViewHolder.Listener{
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {
            RouteManager.route(
                requireContext(),
                ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG,
                winner.id,
                winner.topChatMessage
            )
        }
    })

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProviders.of(requireActivity(), viewModelFactory).get(PlayBroadcastViewModel::class.java)
    }

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

        observeLeaderboardInfo()
    }

    private fun setupView(view: View) {
        with(view) {
            val rvLeaderboard: RecyclerView = findViewById(commonR.id.rv_leaderboard)

            findViewById<TextView>(commonR.id.tv_sheet_title)
                .setText(com.tokopedia.play_common.R.string.play_interactive_leaderboard_title)

            findViewById<ImageView>(commonR.id.iv_sheet_close)
                .setOnClickListener {
                   dismiss()
                }

            rvLeaderboard.adapter = leaderboardAdapter
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun observeLeaderboardInfo() {
        parentViewModel.observableLeaderboardInfo.observe(viewLifecycleOwner, Observer {
            leaderboardAdapter.setItems(it.leaderboardWinners)
        })
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