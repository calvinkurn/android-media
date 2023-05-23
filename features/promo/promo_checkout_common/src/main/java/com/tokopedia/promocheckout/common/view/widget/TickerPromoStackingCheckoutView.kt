package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.promocheckout.common.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.layout_checkout_ticker_promostacking.view.*


class TickerPromoStackingCheckoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val IMAGE_ALPHA_DISABLED = 128
    private val IMAGE_ALPHA_ENABLED = 255

    var state: State = State.EMPTY
        set(value) {
            field = value
            initView()
        }
    var variant: Variant = Variant.GLOBAL
        set(value) {
            field = value
            initView()
        }
    var title: String = ""
        set(value) {
            field = value
            initView()
        }
    var counterCoupons: String = ""
        set(value) {
            field = value
            initView()
        }
    var desc: String = ""
        set(value) {
            field = value
            initView()
        }
    var isLoading: Boolean = false
        set(value) {
            field = value
            if (value) loading_view.visibility = View.VISIBLE
            else loading_view.visibility = View.GONE
        }
    var actionListener: ActionListener? = null

    init {
        inflate(context, getLayout(), this)
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TickerCheckoutView)
        try {
            state = State.fromId(styledAttributes.getInteger(R.styleable.TickerCheckoutView_state, 4))
            title = styledAttributes.getString(R.styleable.TickerCheckoutView_title) ?: ""
            counterCoupons = styledAttributes.getString(R.styleable.TickerCheckoutView_counter) ?: ""
            desc = styledAttributes.getString(R.styleable.TickerCheckoutView_desc) ?: ""

        } finally {
            styledAttributes.recycle()
        }
    }

    private fun initView() {
        when (state) {
            State.INACTIVE -> setViewInactive()
            State.ACTIVE -> setViewActive()
            State.FAILED -> setViewFailed()
            State.EMPTY -> setViewEmpty()
            State.DISABLED -> setViewDisabled()
        }

        when (variant) {
            Variant.GLOBAL -> setViewGlobal()
            Variant.MERCHANT -> setViewMerchant()
            Variant.LOGISTIC -> setViewLogistic()
        }

        titleCouponGlobal?.text = title
        if (desc.isNotEmpty()) {
            descCouponGlobal?.text = desc
            descCouponGlobal.visibility = View.VISIBLE
        }

        setActionListener()
        invalidate()
        requestLayout()
    }

    private fun setActionListener() {
        imageCloseGlobal?.setOnClickListener {
            if (!isLoading) actionListener?.onResetPromoDiscount()
        }
        relativeLayoutUsePromoGlobal?.setOnClickListener {
            if (state != State.DISABLED && !isLoading) actionListener?.onClickUsePromo()
        }
        layoutTickerFrameGlobal?.setOnClickListener {
            if (state != State.DISABLED && !isLoading) actionListener?.onClickDetailPromo()
        }
    }

    fun disableView() {
        setViewEmpty()
        state = State.DISABLED
        initView()
    }

    fun enableView() {
        setViewEnabled()
        state = State.EMPTY
        initView()
    }

    private fun setViewEmpty() {
        relativeLayoutUsePromoGlobal?.visibility = View.VISIBLE
        layoutState.visibility = View.GONE
    }

    private fun setViewCouponShow() {
        relativeLayoutUsePromoGlobal?.visibility = View.GONE
        layoutState.visibility = View.VISIBLE
    }

    private fun setViewFailed() {
        setViewCouponShow()
        val drawableBackground = layoutTickerFrameGlobal.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R400))

        bg_active_up.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.background_checkout_ticker_error_up))
        bg_active_down.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.background_checkout_ticker_error_down))
    }

    private fun setViewActive() {
        setViewCouponShow()
        val drawableBackground = layoutTickerFrameGlobal.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400))

        bg_active_up.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.background_checkout_ticker_active_up))
        bg_active_down.setImageDrawable(
                        MethodChecker.getDrawable(getContext(),R.drawable.background_checkout_ticker_active_down))
    }

    private fun setViewInactive() {
        setViewCouponShow()
        val drawableBackground = layoutTickerFrameGlobal.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y400))

        bg_active_up.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.background_checkout_ticker_inactive_up))
        bg_active_down.setImageDrawable(
                MethodChecker.getDrawable(getContext(),R.drawable.background_checkout_ticker_inactive_down))
    }

    private fun setViewGlobal() {
        ic_button_coupon.setImageResource(R.drawable.ic_kupon_telur)
        ic_button_coupon.visibility = View.VISIBLE
        if (title.isEmpty()) {
            title_button_coupon.setText(R.string.promo_global_title)
        } else {
            title_button_coupon.text = title
        }

        if (counterCoupons.isEmpty()) {
            layout_counter_coupons.visibility = View.GONE
        } else {
            layout_counter_coupons.visibility = View.VISIBLE
            counter_coupons.text = counterCoupons
        }
    }

    private fun setViewMerchant() {
        ic_button_coupon.setImageResource(R.drawable.ic_merchant_promo)
        ic_button_coupon.visibility = View.GONE
        layout_counter_coupons.visibility = View.GONE
        title_button_coupon.setText(R.string.promo_merchant_title)
    }

    private fun setViewLogistic() {
        ic_button_coupon.visibility = View.GONE
        layout_counter_coupons.visibility = View.GONE
        if (state == State.ACTIVE) {
            imageCloseGlobal?.visibility = View.GONE
        } else {
            imageCloseGlobal?.visibility = View.VISIBLE
        }
    }

    private fun setViewDisabled() {
        if (variant != Variant.GLOBAL) {
            val nonActiveTextColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_20)
            title_button_coupon.setTextColor(nonActiveTextColor)
            title_action_coupon.setTextColor(nonActiveTextColor)
            setImageFilterGrayScale()
        }
    }

    private fun setViewEnabled() {
        title_button_coupon.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G400))
        title_action_coupon.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
        setImageFilterNormal()
    }

    private fun setImageFilterGrayScale() {
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        val disabledColorFilter = ColorMatrixColorFilter(matrix)
        ic_button_coupon.colorFilter = disabledColorFilter
        ic_button_coupon.imageAlpha = IMAGE_ALPHA_DISABLED
        bg_button_coupon.colorFilter = disabledColorFilter
        bg_button_coupon.imageAlpha = IMAGE_ALPHA_DISABLED
    }

    private fun setImageFilterNormal() {
        ic_button_coupon.colorFilter = null
        ic_button_coupon.imageAlpha = IMAGE_ALPHA_ENABLED
        bg_button_coupon.colorFilter = null
        bg_button_coupon.imageAlpha = IMAGE_ALPHA_ENABLED
    }

    fun toggleLoading(state: Boolean) {
        isLoading = state
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun getLayout(): Int {
        return R.layout.layout_checkout_ticker_promostacking
    }

    enum class Variant(val id: Int) : Parcelable {

        GLOBAL(0),
        MERCHANT(1),
        LOGISTIC(2);

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(id)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Variant> {
            override fun createFromParcel(parcel: Parcel): Variant {
                return Variant.values()[parcel.readInt()]
            }

            override fun newArray(size: Int): Array<Variant?> {
                return arrayOfNulls(size)
            }
        }


    }

    enum class State(val id: Int) : Parcelable {

        INACTIVE(0),
        ACTIVE(1),
        FAILED(2),
        EMPTY(3),
        DISABLED(4);

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
        fun onResetPromoDiscount()
        fun onClickDetailPromo()
        fun onDisablePromoDiscount()
    }
}