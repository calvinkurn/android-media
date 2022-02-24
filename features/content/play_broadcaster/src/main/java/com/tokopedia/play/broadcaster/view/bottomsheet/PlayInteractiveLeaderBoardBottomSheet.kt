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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.view.state.PlayLiveViewState
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.factory.PlayBroadcastViewModelFactory
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayWinnerUiModel
import com.tokopedia.play_common.ui.leaderboard.adapter.PlayInteractiveLeaderboardAdapter
import com.tokopedia.play_common.ui.leaderboard.viewholder.PlayInteractiveLeaderboardViewHolder
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject
import com.tokopedia.play_common.R as commonR


/**
 * Created by mzennis on 06/07/21.
 */
class PlayInteractiveLeaderBoardBottomSheet @Inject constructor(
    private val parentViewModelFactoryCreator: PlayBroadcastViewModelFactory.Creator,
    private val analytic: PlayBroadcastAnalytic,
) : BottomSheetDialogFragment() {

    private val leaderboardAdapter = PlayInteractiveLeaderboardAdapter(object : PlayInteractiveLeaderboardViewHolder.Listener{
        override fun onChatWinnerButtonClicked(winner: PlayWinnerUiModel, position: Int) {
            analytic.onClickChatWinnerIcon(
                parentViewModel.channelId,
                parentViewModel.interactiveId,
                parentViewModel.activeInteractiveTitle
            )
            RouteManager.route(
                requireContext(),
                "${ApplinkConst.TOPCHAT_ROOM_ASKBUYER_WITH_MSG}$ADDITIONAL_ARG",
                winner.id,
                winner.topChatMessage
            )
        }
    })
    private val leaderboardAdapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (itemCount > 0) rvLeaderboard.smoothScrollToPosition(0)
        }
    }

    private lateinit var parentViewModel: PlayBroadcastViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var rvLeaderboard: RecyclerView
    private lateinit var errorView: ConstraintLayout
    private lateinit var btnRefresh: UnifyButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentViewModel = ViewModelProvider(
            requireActivity(),
            parentViewModelFactoryCreator.create(requireActivity()),
        ).get(PlayBroadcastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            commonR.layout.view_play_interactive_leaderboard,
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterAdapterObserver()
    }

    private fun setupView(view: View) {
        with(view) {
            rvLeaderboard = findViewById(commonR.id.rv_leaderboard)
            errorView = findViewById(commonR.id.cl_leaderboard_error)
            btnRefresh = findViewById(commonR.id.btn_action_leaderboard_error)

            findViewById<TextView>(commonR.id.tv_sheet_title)
                .setText(com.tokopedia.play_common.R.string.play_interactive_leaderboard_title)

            findViewById<ImageView>(commonR.id.iv_sheet_close)
                .setOnClickListener {
                   dismiss()
                }

            btnRefresh.setOnClickListener {
                parentViewModel.getLeaderboardData()
            }

            rvLeaderboard.adapter = leaderboardAdapter

            registerAdapterObserver()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun showError(shouldShow: Boolean) {
        if (shouldShow) {
            errorView.show()
            rvLeaderboard.hide()
        } else {
            errorView.hide()
            rvLeaderboard.show()
        }
    }

    private fun observeLeaderboardInfo() {
        parentViewModel.observableLeaderboardInfo.observe(viewLifecycleOwner) {
           when (it) {
               NetworkResult.Loading -> {
                   showError(false)
                   btnRefresh.isLoading = true
               }
               is NetworkResult.Fail -> {
                   showError(true)
                   btnRefresh.isLoading = false
               }
               is NetworkResult.Success -> {
                   showError(false)
                   btnRefresh.isLoading = false
                   if(needRebindLeaderboard()) {
                       leaderboardAdapter.setItems(it.data.leaderboardWinners)
                       leaderboardAdapter.notifyDataSetChanged()
                   }
                   else {
                       leaderboardAdapter.setItemsAndAnimateChanges(it.data.leaderboardWinners)
                   }
               }
           }
        }
    }

    private fun needRebindLeaderboard(): Boolean {
        val liveState = parentViewModel.observableLiveViewState.value
        return liveState != null && (liveState is PlayLiveViewState.Stopped || liveState is PlayLiveViewState.Error)
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

    private fun registerAdapterObserver() {
        leaderboardAdapter.registerAdapterDataObserver(leaderboardAdapterObserver)
    }

    private fun unregisterAdapterObserver() {
        leaderboardAdapter.unregisterAdapterDataObserver(leaderboardAdapterObserver)
    }

    companion object {
        private const val TAG = "PlayInteractiveLeaderBoardBottomSheet"
        private const val HEIGHT_MULTIPLIER = 0.67f
        private const val ADDITIONAL_ARG = "&source=tx_ask_buyer"
    }
}