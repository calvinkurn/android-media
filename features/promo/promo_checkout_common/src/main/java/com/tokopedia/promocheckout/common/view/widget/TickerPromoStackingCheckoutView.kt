package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.promocheckout.common.R
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
    var desc: String = ""
        set(value) {
            field = value
            initView()
        }
    var actionListener: ActionListener? = null

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
            actionListener?.onResetPromoDiscount()
        }
        layoutUsePromoGlobal?.setOnClickListener {
            if (state != State.DISABLED) actionListener?.onClickUsePromo()
        }
        layoutTickerFrameGlobal?.setOnClickListener {
            if (state != State.DISABLED) actionListener?.onClickDetailPromo()
        }
    }

    private fun resetView() {
        state = State.EMPTY
    }

    fun disableView() {
        state = State.DISABLED
        resetView()
        setViewEmpty()
        setViewDisabled()
        setActionListener()
    }

    fun enableView() {
        resetView()
        setViewEnabled()
        state = State.INACTIVE
        setActionListener()
    }

    private fun setViewEmpty() {
        layoutUsePromoGlobal?.visibility = View.VISIBLE
        layoutTickerFrameGlobal?.visibility = View.GONE
    }

    private fun setViewCouponShow() {
        layoutUsePromoGlobal?.visibility = View.GONE
        layoutTickerFrameGlobal?.visibility = View.VISIBLE
    }

    private fun setViewFailed() {
        setViewCouponShow()
        val drawableBackground = layoutTickerFrameGlobal.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.red_error_coupon))

        bg_active_up.setImageResource(R.drawable.background_checkout_ticker_error_up)
        bg_active_down.setImageResource(R.drawable.background_checkout_ticker_error_down)
    }

    private fun setViewActive() {
        setViewCouponShow()
        val drawableBackground = layoutTickerFrameGlobal.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.green_active_coupon))

        bg_active_up.setImageResource(R.drawable.background_checkout_ticker_active_up)
        bg_active_down.setImageResource(R.drawable.background_checkout_ticker_active_down)
    }

    private fun setViewInactive() {
        setViewCouponShow()
        val drawableBackground = layoutTickerFrameGlobal.background.current.mutate() as GradientDrawable
        drawableBackground.setColor(ContextCompat.getColor(context, R.color.orange_halfactive_coupon))

        bg_active_up.setImageResource(R.drawable.background_checkout_ticker_inactive_up)
        bg_active_down.setImageResource(R.drawable.background_checkout_ticker_inactive_down)
    }

    private fun setViewGlobal() {
        ic_button_coupon.setImageResource(R.drawable.ic_kupon_telur)
        title_button_coupon.setText(R.string.promo_global_title)
    }

    private fun setViewMerchant() {
        ic_button_coupon.setImageResource(R.drawable.ic_merchant_promo)
        title_button_coupon.setText(R.string.promo_merchant_title)
    }

    private fun setViewDisabled() {
        if (variant != Variant.GLOBAL) {
            val nonActiveTextColor = ContextCompat.getColor(context, R.color.promo_checkout_grey_nonactive_text)
            title_button_coupon.setTextColor(nonActiveTextColor)
            title_action_coupon.setTextColor(nonActiveTextColor)
            setImageFilterGrayScale()
        }
    }

    private fun setViewEnabled() {
        title_button_coupon.setTextColor(ContextCompat.getColor(context, R.color.tkpd_main_green))
        title_action_coupon.setTextColor(ContextCompat.getColor(context, R.color.black_70))
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

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    private fun getLayout(): Int {
        return R.layout.layout_checkout_ticker_promostacking
    }

    enum class Variant(val id: Int) : Parcelable {

        GLOBAL(0),
        MERCHANT(1);

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