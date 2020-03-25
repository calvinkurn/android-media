package com.tokopedia.promocheckout.common.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.promocheckout.common.R
import kotlinx.android.synthetic.main.layout_item_promo_checkout.view.*

/**
 * Created by fwidjaja on 2020-02-27.
 */
class ButtonPromoCheckoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr)  {

    private fun getLayout(): Int {
        return R.layout.layout_item_promo_checkout
    }

    var state: State = State.ACTIVE
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

    init {
        inflate(context, getLayout(), this)
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.PromoCheckoutButtonView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.PromoCheckoutButtonView_stateButton, 4))
            title = styledAttributes.getString(R.styleable.PromoCheckoutButtonView_title) ?: ""
            desc = styledAttributes.getString(R.styleable.PromoCheckoutButtonView_desc) ?: ""

        } finally {
            styledAttributes.recycle()
        }
    }

    private fun initView() {
        cl_promo_checkout?.background = ViewUtils.generateBackgroundWithoutShadow(cl_promo_checkout)
        when (state) {
            State.LOADING -> setViewLoading()
            State.ACTIVE -> setViewActive()
            State.INACTIVE -> setViewInactive()
        }

        invalidate()
        requestLayout()
    }

    private fun setViewLoading() {
        tv_promo_checkout_title?.visibility = View.GONE
        tv_promo_checkout_desc?.visibility = View.GONE
        promo_checkout_loading_state?.visibility = View.VISIBLE
        iv_promo_checkout_left?.setImageResource(R.drawable.ic_promo_checkout_percentage)
        iv_promo_checkout_right?.setImageResource(R.drawable.ic_promo_checkout_chevron_right)
    }

    private fun setViewActive() {
        promo_checkout_loading_state?.visibility = View.GONE
        tv_promo_checkout_title?.visibility = View.VISIBLE
        tv_promo_checkout_title?.text = title

        if (desc.isEmpty()) {
            tv_promo_checkout_desc?.visibility = View.GONE
        } else {
            tv_promo_checkout_desc?.visibility = View.VISIBLE
            tv_promo_checkout_desc?.text = desc
        }

        iv_promo_checkout_left?.setImageResource(R.drawable.ic_promo_checkout_percentage)
        iv_promo_checkout_right?.setImageResource(R.drawable.ic_promo_checkout_chevron_right)
    }

    private fun setViewInactive() {
        promo_checkout_loading_state?.visibility = View.GONE
        tv_promo_checkout_title?.visibility = View.VISIBLE
        tv_promo_checkout_desc?.visibility = View.VISIBLE
        tv_promo_checkout_title?.text = title
        tv_promo_checkout_desc?.text = context.getString(R.string.promo_checkout_failed_info)
        iv_promo_checkout_left?.setImageResource(R.drawable.ic_promo_checkout_percentage_inactive)
        iv_promo_checkout_right?.setImageResource(R.drawable.ic_promo_checkout_refresh)
    }

    enum class State(val id: Int) : Parcelable {

        LOADING(0),
        ACTIVE(1),
        INACTIVE(2);

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<State> {

            fun fromId(id: Int): State {
                for (state: State in values()) {
                    if (id == state.id) {
                        return state
                    }
                }
                return ACTIVE
            }

            override fun createFromParcel(parcel: Parcel): State {
                return values()[parcel.readInt()]
            }

            override fun newArray(size: Int): Array<State?> {
                return arrayOfNulls(size)
            }
        }
    }
}