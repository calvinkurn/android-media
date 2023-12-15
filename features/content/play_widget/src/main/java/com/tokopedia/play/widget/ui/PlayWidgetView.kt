package com.tokopedia.play.widget.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleObserver
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.analytic.ImpressionableModel
import com.tokopedia.play.widget.analytic.PlayWidgetAnalyticListener
import com.tokopedia.play.widget.ui.carousel.PlayWidgetCarouselView
import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener
import com.tokopedia.play.widget.ui.listener.PlayWidgetListener
import com.tokopedia.play.widget.ui.model.PlayWidgetType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.error.InflateErrorException

/**
 * Created by jegul on 08/10/20
 */
data class PlayWidgetState(
    val model: PlayWidgetUiModel = PlayWidgetUiModel.Empty,
    val widgetType: PlayWidgetType = PlayWidgetType.Unknown,
    val isLoading: Boolean = false,
    val impressHolder: ImpressionableModel = object : ImpressionableModel {
        override val impressHolder: ImpressHolder = ImpressHolder()
    }
)

class PlayWidgetView : LinearLayout, LifecycleObserver, IPlayWidgetView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mAnalyticListener: PlayWidgetAnalyticListener? = null
    private var mWidgetListener: PlayWidgetListener? = null
    private var mWidgetInternalListener: PlayWidgetInternalListener? = null

    override fun onViewRemoved(child: View?) {
        when (child) {
            is PlayWidgetMediumView -> child.setAnalyticListener(null)
            is PlayWidgetLargeView -> child.setAnalyticListener(null)
            is PlayWidgetCarouselView -> child.setAnalyticListener(null)
        }
        super.onViewRemoved(child)
    }

    override fun setWidgetInternalListener(listener: PlayWidgetInternalListener?) {
        mWidgetInternalListener = listener
        when (val child = getFirstChild()) {
            is PlayWidgetMediumView -> child.setWidgetInternalListener(listener)
            is PlayWidgetLargeView -> child.setWidgetInternalListener(listener)
            is PlayWidgetCarouselView -> child.setWidgetInternalListener(listener)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mWidgetInternalListener?.onWidgetDetached(this)
    }

    fun setCustomHeader(header: View) {
        when (val child = getFirstChild()) {
            is PlayWidgetMediumView -> child.setCustomHeader(header)
            else -> {}
        }
    }

    fun getHeader(): View? {
        return when (val child = getFirstChild()) {
            is PlayWidgetMediumView -> child.getHeader()
            else -> null
        }
    }

    fun setState(state: PlayWidgetState) {
        if (state.isLoading) {
            addPlaceholderView()
            return
        }

        when (state.widgetType) {
            PlayWidgetType.Medium -> addMediumView(state.model)
            PlayWidgetType.Large -> addLargeView(state.model)
            PlayWidgetType.Carousel -> addCarouselView(state.model)
            else -> {
                removeCurrentView()
                mWidgetListener?.onWidgetError(this, InflateErrorException())
            }
        }
    }

    fun setAnalyticListener(listener: PlayWidgetAnalyticListener?) {
        mAnalyticListener = listener
        when (val child = getFirstChild()) {
            is PlayWidgetMediumView -> child.setAnalyticListener(listener)
            is PlayWidgetLargeView -> child.setAnalyticListener(listener)
            is PlayWidgetCarouselView -> child.setAnalyticListener(listener)
        }
    }

    fun setWidgetListener(listener: PlayWidgetListener?) {
        mWidgetListener = listener
        when (val child = getFirstChild()) {
            is PlayWidgetMediumView -> child.setWidgetListener(listener)
            is PlayWidgetLargeView -> child.setWidgetListener(listener)
            is PlayWidgetCarouselView -> child.setWidgetListener(listener)
        }
    }

    private fun addMediumView(model: PlayWidgetUiModel) {
        val widgetView = addWidgetView { PlayWidgetMediumView(context) } ?: return

        val isWidgetEmpty = model.items.isEmpty()
        if (isWidgetEmpty) {
            widgetView.hide()
        } else {
            widgetView.show()
            widgetView.setData(model)
            widgetView.setWidgetListener(mWidgetListener)
            widgetView.setWidgetInternalListener(mWidgetInternalListener)
            widgetView.setAnalyticListener(mAnalyticListener)
        }
    }

    private fun addLargeView(model: PlayWidgetUiModel) {
        val widgetView = addWidgetView { PlayWidgetLargeView(context) } ?: return
        if (model.items.isEmpty()) {
            widgetView.hide()
        } else {
            widgetView.show()
            widgetView.setData(model)
            widgetView.setWidgetListener(mWidgetListener)
            widgetView.setWidgetInternalListener(mWidgetInternalListener)
            widgetView.setAnalyticListener(mAnalyticListener)
        }
    }

    private fun addCarouselView(model: PlayWidgetUiModel) {
        val widgetView = addWidgetView { PlayWidgetCarouselView(context) } ?: return
        if (model.items.isEmpty()) {
            widgetView.hide()
        } else {
            widgetView.show()
            widgetView.setData(model)
            widgetView.setWidgetListener(mWidgetListener)
            widgetView.setWidgetInternalListener(mWidgetInternalListener)
            widgetView.setAnalyticListener(mAnalyticListener)
        }
    }

    private fun addPlaceholderView() {
        val widgetView = addWidgetView { PlayWidgetPlaceholderView(context) } ?: return

        widgetView.setData()
    }

    private inline fun <reified T : View> addWidgetView(creator: () -> T): T? {
        val firstChild = getFirstChild()
        return try {
            if (firstChild !is T) {
                removeCurrentView()
                val widget = creator().apply {
                    layoutParams = getChildLayoutParams()
                }
                addView(widget)

                widget
            } else {
                firstChild
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getFirstChild(): View? = getChildAt(0)

    private fun removeCurrentView() {
        if (childCount > 0) removeViewAt(0)
    }

    private fun getChildLayoutParams() =
        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
}
