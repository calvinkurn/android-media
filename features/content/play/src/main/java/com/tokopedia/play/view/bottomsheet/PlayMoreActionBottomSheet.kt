package com.tokopedia.play.view.bottomsheet

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.ViewCompat
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
                this.childView = View.inflate(context, R.layout.bottom_sheet_play_more_action, null)
                setChild(this.childView)
                setCloseClickListener { this.dismiss() }
            }
        }
    }

    private val moreActionAdapter = PlayMoreActionAdapter()

    private val watchModeAction = PlayMoreActionUiModel(
            type = PlayMoreActionType.WatchMode,
            iconRes = com.tokopedia.iconunify.R.drawable.iconunify_screen_full,
            subtitleRes = R.string.play_watch_mode,
            onClick = { listener?.onWatchModeClicked(this@PlayMoreActionBottomSheet) }
    )

    private var listener: Listener? = null
    private var childView: View? = null

    private lateinit var rvActionList: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        setupView(view)
        setInsets(view)
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog!!.window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val window: Window = dialog!!.window!!
            window.findViewById<View>(com.google.android.material.R.id.container).fitsSystemWindows = false
            // dark navigation bar icons
            val decorView: View = window.decorView
            decorView.systemUiVisibility = decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    fun setState(isFreeze: Boolean) {
        setActionList(isFreeze)
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

    private fun setActionList(isFreeze: Boolean) {
        val actionList = mutableListOf<PlayMoreActionUiModel>().apply {
            if (!isFreeze) add(watchModeAction)
        }
        if (actionList.isNotEmpty()) {
            moreActionAdapter.setItems(actionList)
            moreActionAdapter.notifyDataSetChanged()
        }
        else listener?.onNoAction(this)
    }

    private fun setInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

            if (childView != null) {
                val lpMargin = childView!!.layoutParams as ViewGroup.MarginLayoutParams
                lpMargin.bottomMargin = insets.systemWindowInsetBottom
            }

            insets
        }
    }

    interface Listener {

        fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet)
        fun onNoAction(bottomSheet: PlayMoreActionBottomSheet)
    }
}