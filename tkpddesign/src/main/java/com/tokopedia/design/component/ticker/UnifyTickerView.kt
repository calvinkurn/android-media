package com.tokopedia.design.component.ticker

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.tokopedia.design.R
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.component.TextViewCompat

/**
 * @author by furqan on 15/02/19
 *
 * type = announcement, warning, danger
 */
class UnifyTickerView : BaseCustomView {

    var mType: Int = 1
    var showCloseButton: Boolean = true
    var stateVisibility: Int = VISIBLE

    private lateinit var baseView: View
    private lateinit var tvContent: TextViewCompat
    private lateinit var imgClose: ImageView
    private lateinit var container: View
    private lateinit var baseTicker: View
    private lateinit var listener: UnifyTickerListener

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(context, attrs)
    }

    fun buildView() {
        imgClose.visibility = if (showCloseButton) {
            VISIBLE
        } else {
            GONE
        }

        imgClose.setOnClickListener {
            if (::listener.isInitialized) {
                listener.OnTickerCloseClickListener()
            }
            baseView.visibility = GONE
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putInt(SAVED_STATE_VISIBILITY, stateVisibility)
        bundle.putInt(SAVED_TICKER_TYPE, mType)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            stateVisibility = state.getInt(SAVED_STATE_VISIBILITY)
            mType = state.getInt(SAVED_TICKER_TYPE)
        }
        super.onRestoreInstanceState(state)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        invalidate()
        requestLayout()
    }

    fun setTickerContent(content: CharSequence) {
        tvContent.text = content
    }

    fun setTickerListener(listener: UnifyTickerListener) {
        this.listener = listener
    }

    private fun initView() {
        baseView = inflate(context, R.layout.widget_unify_ticker, this)
        baseTicker = view.findViewById(R.id.parent_view)
        container = view.findViewById(R.id.container)
        tvContent = view.findViewById(R.id.ticker_content)
        imgClose = view.findViewById(R.id.ticker_close_button)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.UnifyTickerView)

            configTickerType(attributeArray)
            configCloseButton(attributeArray)

            attributeArray.recycle()
            initView()
        }
    }

    private fun configTickerType(attributeArray: TypedArray) {
        mType = attributeArray.getInteger(R.styleable.UnifyTickerView_type, 1)
        when (mType) {
            ANNOUNCEMENT -> configAnnouncementTicker()
            WARNING -> configWarningTicker()
            DANGER -> configDangerTicker()
            else -> configAnnouncementTicker()
        }
    }

    private fun configCloseButton(attributeArray: TypedArray) {
        showCloseButton = attributeArray.getBoolean(R.styleable.UnifyTickerView_showCloseButton, true)
    }

    private fun configAnnouncementTicker() {
        baseTicker.setBackgroundColor(ContextCompat
                .getColor(context, ANNOUNCEMENT_COLOR_HIGHLIGHT_UNIFY_TICKER))
    }

    private fun configWarningTicker() {
        baseTicker.setBackgroundColor(ContextCompat
                .getColor(context, WARNING_COLOR_HIGHLIGHT_UNIFY_TICKER))
    }

    private fun configDangerTicker() {
    }

    public interface UnifyTickerListener {
        fun OnTickerCloseClickListener()
    }

    companion object {
        private val ANNOUNCEMENT = 1
        private val WARNING = 2
        private val DANGER = 3

        private val SAVED_STATE_VISIBILITY = "utv_saved_state_visibility"
        private val SAVED_TICKER_TYPE = "utv_saved_type"

        val ANNOUNCEMENT_COLOR_HIGHLIGHT_UNIFY_TICKER = R.color.tkpd_main_light_green
        val WARNING_COLOR_HIGHLIGHT_UNIFY_TICKER = R.color.unify_warning_ticker
    }

}