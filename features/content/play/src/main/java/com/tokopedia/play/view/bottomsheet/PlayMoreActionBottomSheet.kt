package com.tokopedia.play.view.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.R
import com.tokopedia.play.view.adapter.PlayMoreActionAdapter
import com.tokopedia.play.view.type.PlayMoreActionType
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionBottomSheet : BottomSheetUnify() {

    companion object {

        private const val TAG = "PlayMoreActionBottomSheet"

        @JvmStatic
        fun newInstance(context: Context, listener: Listener): PlayMoreActionBottomSheet {
            return PlayMoreActionBottomSheet().apply {
                this.listener = listener
                setChild(View.inflate(context, R.layout.bottom_sheet_play_more_action, null))
                setCloseClickListener { this.dismiss() }
            }
        }
    }

    private val moreActionAdapter = PlayMoreActionAdapter().apply {
        setItems(
                listOf(
                    PlayMoreActionUiModel(
                            type = PlayMoreActionType.WatchMode,
                            iconRes = R.drawable.ic_play_watch_mode,
                            subtitleRes = R.string.play_watch_mode,
                            onClick = { listener?.onWatchModeClicked(this@PlayMoreActionBottomSheet) }
                    )
                )
        )
    }

    private var listener: Listener? = null

    private lateinit var rvActionList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initView(view: View) {
        with(view) {
            rvActionList = findViewById(R.id.rv_action_list)
        }
    }

    private fun setupView(view: View) {
        rvActionList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = moreActionAdapter
        }
    }

    interface Listener {

        fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet)
    }
}