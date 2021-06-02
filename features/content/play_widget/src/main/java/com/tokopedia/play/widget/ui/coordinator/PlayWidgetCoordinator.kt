package com.tokopedia.play.widget.ui.coordinator

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.analytic.impression.ImpressionHelper
import com.tokopedia.play.widget.ui.PlayWidgetView
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigProvider
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import kotlinx.android.synthetic.main.item_play_widget.view.*
import kotlinx.coroutines.*

/**
 * Created by jegul on 13/10/20
 */
class PlayWidgetCoordinator(
        lifecycleOwner: LifecycleOwner? = null,
        mainCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        workCoroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : LifecycleObserver, PlayWidgetAutoRefreshCoordinator.Listener {

    private val scope = CoroutineScope(mainCoroutineDispatcher)

    private var mWidget: PlayWidgetView? = null
    private var mModel: PlayWidgetUiModel = PlayWidgetUiModel.Placeholder

    private var mListener: PlayWidgetListener? = null
    private var mAnalyticListener: PlayWidgetAnalyticListener? = null

    private val widgetHolderListener = object : PlayWidgetViewHolder.Listener {

        override fun onWidgetImpressed(view: PlayWidgetView, item: PlayWidgetUiModel, position: Int) {
            mAnalyticListener?.onImpressPlayWidget(view, item, position)
        }
    }

    private val autoPlayCoordinator = PlayWidgetAutoPlayCoordinator(scope, mainCoroutineDispatcher)

    private val autoRefreshCoordinator = PlayWidgetAutoRefreshCoordinator(
            scope,
            mainCoroutineDispatcher,
            workCoroutineDispatcher,
            this
    )

    private val mWidgetInternalListener = object : PlayWidgetInternalListener {
        override fun onWidgetAttached(widgetCardsContainer: RecyclerView) {
            autoPlayCoordinator.onWidgetAttached(widgetCardsContainer)
        }

        override fun onWidgetCardsScrollChanged(widgetCardsContainer: RecyclerView) {
            autoPlayCoordinator.onWidgetCardsScrollChanged(widgetCardsContainer)
        }

        override fun onWidgetDetached(widget: View) {
            autoPlayCoordinator.onWidgetDetached(widget)
        }
    }

    private var impressionHelper = ImpressionHelper()

    init {
        lifecycleOwner?.let { configureLifecycle(it) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        autoPlayCoordinator.onPause()
        autoRefreshCoordinator.onPause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        val currentModel = mModel
        if (currentModel is PlayWidgetConfigProvider) {
            autoRefreshCoordinator.configureAutoRefresh(currentModel.config)
        }
        autoPlayCoordinator.onResume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        autoPlayCoordinator.onDestroy()
        scope.coroutineContext.cancelChildren()
    }

    override fun onWidgetShouldRefresh() {
        mWidget?.let { mListener?.onWidgetShouldRefresh(it) }
    }

    fun controlWidget(widget: PlayWidgetView) {
        mWidget = widget
        widget.setAnalyticListener(mAnalyticListener)
        widget.setWidgetInternalListener(mWidgetInternalListener)
        widget.setWidgetListener(mListener)
    }

    fun controlWidget(widgetViewHolder: PlayWidgetViewHolder) {
        controlWidget(widgetViewHolder.itemView.play_widget_view as PlayWidgetView)
        widgetViewHolder.setListener(widgetHolderListener)
    }

    fun setListener(listener: PlayWidgetListener?) {
        mListener = listener
        mWidget?.setWidgetListener(mListener)
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
        mWidget?.setAnalyticListener(listener)
    }

    fun connect(widget: PlayWidgetView, model: PlayWidgetUiModel) {
        mModel = model
        widget.setModel(model)

        if (model is PlayWidgetConfigProvider) {
            autoRefreshCoordinator.configureAutoRefresh(model.config)
            autoPlayCoordinator.configureAutoPlay(widget, model.config)
        }
    }

    fun setImpressionHelper(helper: ImpressionHelper) {
        impressionHelper = helper
    }

    fun getImpressionHelper(): ImpressionHelper {
        return impressionHelper
    }

    private fun configureLifecycle(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Fragment) {
            lifecycleOwner.viewLifecycleOwnerLiveData.observe(lifecycleOwner, Observer {
                it.lifecycle.addObserver(this)
            })
        } else {
            lifecycleOwner.lifecycle.addObserver(this)
        }
    }
}