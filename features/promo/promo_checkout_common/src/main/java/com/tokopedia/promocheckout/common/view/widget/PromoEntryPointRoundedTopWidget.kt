package com.tokopedia.promocheckout.common.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ViewSwitcher
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import com.tokopedia.promocheckout.common.R
import com.tokopedia.unifycomponents.BaseCustomView

class PromoEntryPointRoundedTopWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private fun getLayout(): Int {
        return R.layout.layout_widget_promo_checkout_switcher
    }

    var state: ButtonPromoCheckoutView.State = ButtonPromoCheckoutView.State.ACTIVE
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
//    var margin: ButtonPromoCheckoutView.Margin = ButtonPromoCheckoutView.Margin.WITH_BOTTOM
//        set(value) {
//            field = value
//            initView()
//        }

    var chevronIcon: Int = 0
        set(value) {
            field = value
            initView()
        }

    private var loadingView: View? = null
    private var switcherView: ViewSwitcher? = null
    private var activeView: View? = null
    private var inActiveView: View? = null
    private var errorView: View? = null

    init {
        inflate(context, getLayout(), this)
        setupViews()
//        val styledAttributes =
//            context.obtainStyledAttributes(attrs, R.styleable.PromoCheckoutButtonView)
//        try {
//            state = ButtonPromoCheckoutView.State.fromId(
//                styledAttributes.getInteger(
//                    R.styleable.PromoCheckoutButtonView_stateButton,
//                    1
//                )
//            )
//            title = styledAttributes.getString(R.styleable.PromoCheckoutButtonView_title) ?: ""
//            desc = styledAttributes.getString(R.styleable.PromoCheckoutButtonView_desc) ?: ""
//            chevronIcon = styledAttributes.getInt(R.styleable.PromoCheckoutButtonView_icon_right, 0)
////            margin = ButtonPromoCheckoutView.Margin.fromId(
////                styledAttributes.getInteger(
////                    R.styleable.PromoCheckoutButtonView_marginButton,
////                    0
////                )
////            )
//
//        } finally {
//            styledAttributes.recycle()
//        }
    }

    private fun setupViews() {
        loadingView = findViewById(R.id.loader_promo_checkout)
        errorView = findViewById(R.id.error_promo_checkout)
        switcherView = findViewById(R.id.switcher_promo_checkout)
        activeView = switcherView?.get(0)
        inActiveView = switcherView?.get(1)

        loadingView?.background = ResourcesCompat.getDrawable(resources, R.drawable.background_promo_checkout_teal_rounded, null)
        activeView?.background = ResourcesCompat.getDrawable(resources, R.drawable.background_promo_checkout_active_teal_rounded, null)
        val inActiveBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal_rounded,
            null
        )
        (inActiveBackground as? GradientDrawable)?.setColor(ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_NN50, null))
        inActiveView?.background = inActiveBackground
        val errorBackground = ResourcesCompat.getDrawable(
            resources,
            R.drawable.background_promo_checkout_teal_rounded,
            null
        )
        (errorBackground as? GradientDrawable)?.setColor(ResourcesCompat.getColor(resources, com.tokopedia.unifyprinciples.R.color.Unify_YN50, null))
        errorView?.background = errorBackground
    }

    private fun initView() {
        when (state) {
            ButtonPromoCheckoutView.State.LOADING -> showLoading()
            ButtonPromoCheckoutView.State.ACTIVE -> setViewActive()
            ButtonPromoCheckoutView.State.INACTIVE -> setViewInactive()
        }

//        when (margin) {
//            ButtonPromoCheckoutView.Margin.WITH_BOTTOM -> setViewWithMarginBottom()
//            ButtonPromoCheckoutView.Margin.NO_BOTTOM -> setViewWIthNoMarginBottom()
//        }

        setChevronIcon()

        invalidate()
        requestLayout()
    }

    private fun setChevronIcon() {
        if (chevronIcon == 0) {
            chevronIcon = R.drawable.ic_promo_checkout_chevron_right
        }
//        iv_promo_checkout_right.setImageDrawable(MethodChecker.getDrawable(context, chevronIcon))
    }

    fun setListenerChevronIcon(actionListener: () -> Unit) {
//        iv_promo_checkout_right.setOnClickListener {
//            actionListener.invoke()
//        }
    }

    fun showLoading() {
        loadingView?.visibility = View.VISIBLE
        switcherView?.visibility = View.GONE
        errorView?.visibility = View.GONE
    }

    private fun setViewActive() {

        if (desc.isEmpty()) {
        } else {
        }

    }

    private fun setViewInactive() {
    }

    private fun setViewWithMarginBottom() {
    }

    private fun setViewWIthNoMarginBottom() {
    }
}
