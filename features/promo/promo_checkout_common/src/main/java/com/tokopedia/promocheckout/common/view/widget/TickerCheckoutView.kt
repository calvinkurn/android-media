package com.tokopedia.promocheckout.common.view.widget

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.promocheckout.common.R
import kotlinx.android.synthetic.main.layout_checkout_ticker.view.*

class TickerCheckoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    var state: State = State.EMPTY
        set(value) {
            field = value
            initView()
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
        val inflater = (context as Activity).layoutInflater
        inflater.inflate(getLayout(), this)
        attrs?.let{
            val styledAttributes = context.obtainStyledAttributes(it, R.styleable.TickerCheckoutView)
            try {
                state = State.fromId(styledAttributes.getInteger(R.styleable.TickerCheckoutView_state, 4))
                title = styledAttributes.getString(R.styleable.TickerCheckoutView_title) ?: ""
                desc = styledAttributes.getString(R.styleable.TickerCheckoutView_desc) ?: ""
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun initView() {
        when(state){
            State.INACTIVE -> setViewInactive()
            State.ACTIVE -> setViewActive()
            State.FAILED -> setViewFailed()
            State.EMPTY -> setViewEmpty()
        }
        descCoupon?.text = desc
        titleCoupon?.text = title
        imageClose?.setOnClickListener {
            resetView()
            actionListener?.onDisablePromoDiscount()
        }
        layoutUsePromo?.setOnClickListener {
            actionListener?.onClickUsePromo()
        }
        layoutTicker?.setOnClickListener {
            actionListener?.onClickDetailPromo()
        }
        invalidate()
        requestLayout()
    }

    fun resetView(){
        state = State.EMPTY
    }

    private fun setViewEmpty() {
        layoutUsePromo?.visibility = View.VISIBLE
        layoutTicker?.visibility = View.GONE
    }

    private fun setViewCouponShow(){
        layoutUsePromo?.visibility = View.GONE
        layoutTicker?.visibility = View.VISIBLE
    }

    private fun setViewFailed() {
        setViewCouponShow()
        val drawableBackground = layoutTicker.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.bright_red))
        imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_red)
        imageCheck.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_failed_promo_checkout))
        imageTitleCoupon.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_coupon_red_promo_checkout))
        imageClose.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_close_red_promo_checkout))
        titleCoupon.setTextColor(ContextCompat.getColor(context, R.color.background_error))
    }

    private fun setViewActive() {
        setViewCouponShow()
        val drawableBackground = layoutTicker.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.green_200))
        imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_green)
        imageCheck.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_check_black))
        imageTitleCoupon.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_coupon_green_promo_checkout))
        imageClose.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_close_green_promo_checkout))
        titleCoupon.setTextColor(ContextCompat.getColor(context,R.color.tkpd_main_green))
    }

    private fun setViewInactive() {
        setViewCouponShow()
        val drawableBackground = layoutTicker.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.grey_300))
        imageCheck.background = ContextCompat.getDrawable(context, R.drawable.half_circle_grey)
        imageCheck.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_question_promo_checkout))
        imageTitleCoupon.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_coupon_grey_promo_checkout))
        imageClose.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.ic_close_grey_promo_checkout))
        titleCoupon.setTextColor(ContextCompat.getColor(context,R.color.font_black_secondary_54))
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