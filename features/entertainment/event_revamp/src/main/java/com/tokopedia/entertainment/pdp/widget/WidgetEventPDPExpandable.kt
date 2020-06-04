package com.tokopedia.entertainment.pdp.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.core.content.ContextCompat
import com.tokopedia.entertainment.R
import com.tokopedia.expandable.BaseExpandableOption
import com.tokopedia.expandable.BaseExpandableOptionText
import kotlinx.android.synthetic.main.custom_event_expandable_parent.view.*

class WidgetEventPDPExpandable  @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0): BaseExpandableOption(context,attrs,defStyleAttr){
    lateinit var imageUp: Drawable
    lateinit var imageDown: Drawable
    private var isUseRotateAnimation = false
    private var hasFinishInflate = false
    lateinit var eventWidgetExpandableListener:EventWidgetExpandableListener


    override fun init() {
        setHeaderLayoutRes(R.layout.custom_event_expandable_parent)
        super.init()
    }

    override fun init(attrs: AttributeSet) {
        super.init(attrs)
        hasFinishInflate = false
        val styledAttributes: TypedArray = context.obtainStyledAttributes(attrs, com.tokopedia.expandable.R.styleable.BaseExpandableOption)
        try {
            imageDown = ContextCompat.getDrawable(context, R.drawable.ent_pdp_expand_arrow_down)!!
            imageUp = ContextCompat.getDrawable(context, R.drawable.ent_pdp_expand_arrow_up)!!
            isUseRotateAnimation = true
        } finally {
            styledAttributes.recycle()
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        container_event_pdp_expandable.setOnClickListener {
            if(isEnabled){
                toggle()
                eventWidgetExpandableListener.onParentClicked(isEnabled)
            }
        }

        hasFinishInflate = true
    }

    override fun setExpand(isExpanded: Boolean) {
        super.setExpand(isExpanded)
        if (isExpanded) {
            if (isUseRotateAnimation) {
                animateExpand(hasFinishInflate)
            } else {
                event_arrow_expandable.setImageDrawable(imageUp)
            }
        } else {
            if (isUseRotateAnimation) {
                animateCollapse(hasFinishInflate)
            } else {
                event_arrow_expandable.setImageDrawable(imageDown)
            }
        }
    }

    private fun animateExpand(isAnimate: Boolean) {
        event_arrow_expandable.setImageDrawable(imageDown)
        val rotate = RotateAnimation(360f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        if (isAnimate) {
            rotate.duration = 300
        } else {
            rotate.duration = 1
        }
        rotate.fillAfter = true
        event_arrow_expandable.startAnimation(rotate)
    }

    private fun animateCollapse(isAnimate: Boolean) {
        event_arrow_expandable.setImageDrawable(imageDown)
        val rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        if (isAnimate) {
            rotate.duration = 300
        } else {
            rotate.duration = 1
        }
        rotate.fillAfter = true
        event_arrow_expandable.startAnimation(rotate)
    }

    override fun initView(view: View?) {

    }

    fun setListener(eventWidgetExpandableListener: EventWidgetExpandableListener){
        this.eventWidgetExpandableListener = eventWidgetExpandableListener
    }

    interface EventWidgetExpandableListener {
        fun onParentClicked(toogle: Boolean)
    }
}