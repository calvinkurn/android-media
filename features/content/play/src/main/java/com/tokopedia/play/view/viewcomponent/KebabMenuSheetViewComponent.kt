package com.tokopedia.play.view.viewcomponent

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.play.R
import com.tokopedia.play.view.adapter.PlayMoreActionAdapter
import com.tokopedia.play.view.type.PlayMoreActionType
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play_common.R as commonR
import com.tokopedia.unifycomponents.R as unifyR

/**
 * @author by astidhiyaa on 08/12/21
 */
class KebabMenuSheetViewComponent(
    container: ViewGroup,
    private val listener: Listener
) : ViewComponent(container, R.id.cl_kebab_menu_sheet) {

    private val rvActionList: RecyclerView = findViewById(R.id.rv_action_list)
    private val moreActionAdapter = PlayMoreActionAdapter()
    private val ctx: Context
        get() = rootView.context

    private val reportAction = PlayMoreActionUiModel(
        type = PlayMoreActionType.Report,
        icon = getIconUnifyDrawable(ctx, IconUnify.WARNING, MethodChecker.getColor(ctx, unifyR.color.Unify_NN900)),
        isIconAvailable = true,
        subtitleRes = R.string.play_kebab_report_title,
        onClick = { listener.onReportClick(this@KebabMenuSheetViewComponent) }
    )

    private val pipAction = PlayMoreActionUiModel(
        type = PlayMoreActionType.PiP,
        icon = MethodChecker.getDrawable(ctx, R.drawable.ic_play_pip),
        isIconAvailable = true,
        subtitleRes = R.string.play_kebab_pip,
        onClick = { listener.onPipClicked(this@KebabMenuSheetViewComponent) }
    )

    private val chromecastAction = PlayMoreActionUiModel(
        type = PlayMoreActionType.Chromecast,
        icon = null,
        isIconAvailable = false,
        subtitleRes = R.string.play_kebab_chromecast,
        onClick = { listener.onChromecastClicked(this@KebabMenuSheetViewComponent) }
    )

    private val watchAction = PlayMoreActionUiModel(
        type = PlayMoreActionType.WatchMode,
        icon = getIconUnifyDrawable(ctx, IconUnify.VISIBILITY, MethodChecker.getColor(ctx, unifyR.color.Unify_NN900)),
        isIconAvailable = true,
        subtitleRes = R.string.play_kebab_watch_mode,
        onClick = { listener.onWatchModeClick(this@KebabMenuSheetViewComponent) }
    )

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
        moreActionAdapter.setItemsAndAnimateChanges(actions)
        show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        rootView.requestApplyInsetsWhenAttached()
    }

    interface Listener {
        fun onReportClick(view: KebabMenuSheetViewComponent)
        fun onCloseButtonClicked(view: KebabMenuSheetViewComponent)
        fun onPipClicked(view: KebabMenuSheetViewComponent)
        fun onChromecastClicked(view: KebabMenuSheetViewComponent)
        fun onWatchModeClick(view: KebabMenuSheetViewComponent)
    }
}
