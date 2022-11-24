package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.R
import com.tokopedia.play.view.adapter.PlayMoreActionAdapter
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play_common.R as commonR

/**
 * @author by astidhiyaa on 08/12/21
 */
class KebabMenuSheetViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_kebab_menu_sheet) {

    private val rvActionList: RecyclerView = findViewById(R.id.rv_action_list)
    private val moreActionAdapter = PlayMoreActionAdapter()

    init {
        findViewById<ImageView>(commonR.id.iv_sheet_close)
            .setOnClickListener {
                listener.onCloseButtonClicked(this@KebabMenuSheetViewComponent)
            }

        findViewById<ImageView>(commonR.id.tv_sheet_title).hide()

        rvActionList.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = moreActionAdapter
        }
    }

    fun show(actions: List<PlayMoreActionUiModel>) {
        if (actions.isEmpty()) return
        val finalList = actions.sortedBy { it.priority }
        moreActionAdapter.setItemsAndAnimateChanges(finalList)
        show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    interface Listener {
        fun onCloseButtonClicked(view: KebabMenuSheetViewComponent)
    }
}
