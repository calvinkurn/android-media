package com.tokopedia.promocheckout.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.promocheckout.R
import kotlinx.android.synthetic.main.layout_checkout_ticker.view.*

class TickerCheckoutView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var state: State = State.INACTIVE
        set(value) {
            initView()
        }

    var title: String = ""
        set(value) {
            initView()
        }
    var desc: String = ""
        set(value) {
            initView()
        }

    init {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TickerCheckoutView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.TickerCheckoutView_state, 1))
            title = styledAttributes.getString(R.styleable.TickerCheckoutView_title) ?: ""
            desc = styledAttributes.getString(R.styleable.TickerCheckoutView_desc) ?: ""

        } finally {
            styledAttributes.recycle()
        }
        inflate(context, getLayout(), this)
    }

    private fun initView() {
        when(state){
            State.INACTIVE -> setViewInactive()
            State.ACTIVE -> setViewActive()
            State.FAILED -> setViewFailed()
        }
        rootView.descCoupon.text = desc
        rootView.titleCoupon.text = title
        invalidate()
        requestLayout()
    }

    private fun setViewFailed() {
        val drawableBackground = rootView.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.bright_red))
        rootView.imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_red)
    }

    private fun setViewActive() {
        val drawableBackground = rootView.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.green_200))
        rootView.imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_green)
    }

    private fun setViewInactive() {
        val drawableBackground = rootView.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.grey_300))
        rootView.imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_grey)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun getLayout(): Int {
        return R.layout.layout_checkout_ticker
    }

    enum class State(val id: Int) {
        INACTIVE(1),
        ACTIVE(2),
        FAILED(3);

        companion object {
            fun fromId(id: Int): State {
                for (state: State in State.values()) {
                    if (id == state.id) {
                        return state
                    }
                }
                return State.INACTIVE
            }
        }
    }
}