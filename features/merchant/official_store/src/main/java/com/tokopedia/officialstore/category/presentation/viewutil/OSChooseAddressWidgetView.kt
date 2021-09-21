package com.tokopedia.officialstore.category.presentation.viewutil

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isDeviceAnimationDisabled
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.category.presentation.listener.OSContainerListener
import com.tokopedia.officialstore.category.presentation.widget.getValueAnimator
import com.tokopedia.officialstore.official.presentation.listener.OSChooseAddressWidgetCallback
import kotlinx.android.synthetic.main.layout_choose_address.view.*

/**
 * Created by yfsx on 5/20/21.
 */
class OSChooseAddressWidgetView : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_choose_address, this)
        this.itemView = view
        this.itemContext = view.context
    }

    private val itemContext: Context
    private var animationExpand: ValueAnimator?= null
    private var animationCollapse: ValueAnimator?= null
    private var isExpand = true
    private var totalScrollUp: Int = 0
    private var itemView: View
    private var viewMaxHeight: Int = 0
    private var viewMinHeight: Int = 0
    private var chooseAddressWidgetInitialized = false
    private lateinit var osContainerListener: OSContainerListener

    fun getChooseAddressWidget(): ChooseAddressWidget {
        return widget_choose_address
    }

    fun setMeasuredHeight() {
        viewMaxHeight = this.measuredHeight
    }

    fun initChooseAddressWidget(needToShowChooseAddress: Boolean,
                                listener: OSContainerListener,
                                fragment: Fragment,
                                widgetShown: () -> Unit = {},
                                widgetGone: () -> Unit = {}) {
        if (!chooseAddressWidgetInitialized) {
            osContainerListener = listener
            widget_choose_address.bindChooseAddress(OSChooseAddressWidgetCallback(context, listener, fragment))
            widget_choose_address.run {
                if (needToShowChooseAddress) {
                    widgetShown.invoke()
                } else {
                    widgetGone.invoke()
                }
            }
            chooseAddressWidgetInitialized = true
        }
    }

    fun forceExpandView(whenWidgetShow: () -> Unit) {
        expandView(whenWidgetShow)
    }

    fun updateChooseAddressInitializedState(state: Boolean) {
        chooseAddressWidgetInitialized = state
    }

    fun adjustViewCollapseOnScrolled(dy: Int, whenWidgetShow: () -> Unit = {}, whenWidgetGone: () -> Unit = {}) {
        if (dy == 0) {
            return
        }

        if (dy < 0) {
            totalScrollUp -= dy
        } else {
            totalScrollUp = 0
        }

        adjustCollapseExpandView(totalScrollUp in 0..10, whenWidgetShow, whenWidgetGone)
    }


    private fun adjustCollapseExpandView(isCollapse: Boolean, whenWidgetShow: () -> Unit = {}, whenWidgetGone: () -> Unit = {}) {
        if (isCollapse) {
            collapseView(whenWidgetGone)
        } else {
            expandView(whenWidgetShow)
        }
    }

    private fun expandView(whenWidgetShow: () -> Unit = {}) {
        if(animationExpand == null)
            animationExpand = getValueAnimator(0f, viewMaxHeight.toFloat(), 300, AccelerateDecelerateInterpolator()) {
                if (this.layoutParams != null) {
                    val params: ViewGroup.LayoutParams = layoutParams
                    params.height = it.toInt()
                    requestLayout()
                }
            }
        whenWidgetShow.invoke()
        if (itemContext.isDeviceAnimationDisabled()) {
            this.show()
        } else {
            if (this.measuredHeight == 0) {
                animationExpand?.start()
                isExpand = true
                motionlayout_choose_address.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    }

                    override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, progress: Float) {
                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    }

                    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                    }
                })
                motionlayout_choose_address.transitionToStart()
            }
        }

    }

    private fun collapseView(whenWidgetGone: () -> Unit = {}) {
        if(animationCollapse == null) animationCollapse =
                getValueAnimator(viewMaxHeight.toFloat(), 0f, 300, AccelerateDecelerateInterpolator()) {
                    if (this.layoutParams != null) {
                        val params: ViewGroup.LayoutParams = layoutParams
                        params.height = it.toInt()
                        requestLayout()
                    }
                }
        whenWidgetGone.invoke()
        if (itemContext.isDeviceAnimationDisabled()) {
            this.gone()
        } else {
            if (this.measuredHeight == viewMaxHeight) {
                animationCollapse?.start()
                isExpand = false
                motionlayout_choose_address.setTransitionListener(object : MotionLayout.TransitionListener {
                    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                    }

                    override fun onTransitionChange(p0: MotionLayout, p1: Int, p2: Int, progress: Float) {
                    }

                    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    }

                    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                    }
                })
                motionlayout_choose_address.transitionToEnd()
            }
        }
    }
}