package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.promocheckout.common.R
import kotlinx.android.synthetic.main.layout_checkout_ticker.view.*

class TickerCheckoutView @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var state: State = State.EMPTY
        set(value) {
            initView()
            field = value
        }

    var title: String = ""
        set(value) {
            field = value
            initView()
        }
    var desc: String = ""
        set(value) {
            field = value
            initView()
        }
    var actionListener : ActionListener? = null

    init {
        inflate(context, getLayout(), this)
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TickerCheckoutView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.TickerCheckoutView_state, 4))
            title = styledAttributes.getString(R.styleable.TickerCheckoutView_title) ?: ""
            desc = styledAttributes.getString(R.styleable.TickerCheckoutView_desc) ?: ""

        } finally {
            styledAttributes.recycle()
        }
    }

    private fun initView() {
        when(state){
            State.INACTIVE -> setViewInactive()
            State.ACTIVE -> setViewActive()
            State.FAILED -> setViewFailed()
            State.EMPTY -> setViewEmpty()
        }
        rootView.descCoupon.text = desc
        rootView.titleCoupon.text = title
        rootView.imageClose.setOnClickListener {
            resetView()
            actionListener?.onDisablePromoDiscount()
        }
        rootView.layoutUsePromo.setOnClickListener {
            actionListener?.onClickUsePromo()
        }
        rootView.layoutTicker.setOnClickListener {
            actionListener?.onClickDetailPromo()
        }
        invalidate()
        requestLayout()
    }

    fun resetView(){
        state = State.EMPTY
    }

    private fun setViewEmpty() {
        rootView.layoutUsePromo.visibility = View.VISIBLE
        rootView.layoutTicker.visibility = View.GONE
    }

    private fun setViewCouponShow(){
        rootView.layoutUsePromo.visibility = View.GONE
        rootView.layoutTicker.visibility = View.VISIBLE
    }

    private fun setViewFailed() {
        setViewCouponShow()
        val drawableBackground = rootView.layoutTicker.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.bright_red))
        rootView.imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_red)
    }

    private fun setViewActive() {
        setViewCouponShow()
        val drawableBackground = rootView.layoutTicker.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.green_200))
        rootView.imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_green)
    }

    private fun setViewInactive() {
        setViewCouponShow()
        val drawableBackground = rootView.layoutTicker.background.current.mutate() as GradientDrawable
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

    enum class State(val id: Int) : Parcelable {

        INACTIVE(0),
        ACTIVE(1),
        FAILED(2),
        EMPTY(3);

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<State> {

            fun fromId(id: Int): State {
                for (state: State in State.values()) {
                    if (id == state.id) {
                        return state
                    }
                }
                return EMPTY
            }
            override fun createFromParcel(parcel: Parcel): State {
                return State.values()[parcel.readInt()]
            }

            override fun newArray(size: Int): Array<State?> {
                return arrayOfNulls(size)
            }
        }
    }

    interface ActionListener {
        fun onClickUsePromo()
        fun onDisablePromoDiscount()
        fun onClickDetailPromo()
    }
}