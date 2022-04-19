package com.tokopedia.common.travel.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.widget.NumberPickerWithCounterView.OnPickerActionListener
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * Created by alvarisi on 10/25/17.
 */
class SelectPassengerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var passengerImageView: AppCompatImageView? = null
    private var titleTextView: AppCompatTextView? = null
    private var subtitleTextView: AppCompatTextView? = null
    private var numberPickerWithCounterView: NumberPickerWithCounterView? = null
    private var icon: Drawable? = null
    private var selectedNumber = 0
    private var maxValue = 0
    private var minValue = 0
    private var title: String? = null
    private var subtitle: String? = null
    private var onPassengerCountChangeListener: OnPassengerCountChangeListener? = null

    interface OnPassengerCountChangeListener {
        fun onChange(number: Int): Boolean
    }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SelectPassengerView)

            // get resource full name, to use later for checking is it png or vector
            val value = TypedValue()
            context.resources.getValue(styledAttributes.getResourceId(R.styleable.SelectPassengerView_spv_icon, R.drawable.ic_travel_passenger_adult), value, true)
            try {
                if (value.string.toString().contains(SVG_EXTENSION)) {
                    icon = VectorDrawableCompat.create(context.resources, styledAttributes.getResourceId(R.styleable.SelectPassengerView_spv_icon, R.drawable.ic_travel_passenger_adult), context.theme)
                } else if (value.string.toString().contains(PNG_EXTENSION)) {
                    icon = styledAttributes.getDrawable(R.styleable.SelectPassengerView_spv_icon)
                }
                maxValue = styledAttributes.getInteger(R.styleable.SelectPassengerView_spv_max_value, DEFAULT_MAX_VALUE)
                minValue = styledAttributes.getInteger(R.styleable.SelectPassengerView_spv_min_value, DEFAULT_MIN_VALUE)
                selectedNumber = styledAttributes.getInteger(R.styleable.SelectPassengerView_spv_value, DEFAULT_VALUE)
                title = styledAttributes.getString(R.styleable.SelectPassengerView_spv_title)
                subtitle = styledAttributes.getString(R.styleable.SelectPassengerView_spv_subtitle)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun init() {
        val view = inflate(context, R.layout.widget_select_passenger_view, this)
        numberPickerWithCounterView = view.findViewById<View>(R.id.number_picker_passenger) as NumberPickerWithCounterView
        passengerImageView = view.findViewById<View>(R.id.image_passenger_icon) as AppCompatImageView
        titleTextView = view.findViewById<View>(R.id.textview_title) as AppCompatTextView
        subtitleTextView = view.findViewById<View>(R.id.textview_subtitle) as AppCompatTextView
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        numberPickerWithCounterView!!.setOnPickerActionListener(onPickerActionListener)
        numberPickerWithCounterView!!.setMaxValue(maxValue)
        numberPickerWithCounterView!!.setMinValue(minValue)
        numberPickerWithCounterView!!.setNumber(selectedNumber)
        if (icon != null) {
            passengerImageView!!.setImageDrawable(icon)
        }
        if (!TextUtils.isEmpty(title)) {
            titleTextView!!.text = title
        }
        if (!TextUtils.isEmpty(subtitle)) {
            subtitleTextView!!.text = subtitle
        } else {
            subtitleTextView!!.visibility = GONE
        }
    }

    private val onPickerActionListener: OnPickerActionListener
        get() = object : OnPickerActionListener {
            override fun onNumberChange(num: Int) {
                if (onPassengerCountChangeListener != null) onPassengerCountChangeListener!!.onChange(num)
            }
        }

    var value: Int
        get() = numberPickerWithCounterView?.value ?: 0
        set(numberOfPassenger) {
            numberPickerWithCounterView?.setNumber(numberOfPassenger)
        }

    fun setOnPassengerCountChangeListener(listener: OnPassengerCountChangeListener?) {
        onPassengerCountChangeListener = listener
        numberPickerWithCounterView?.setOnPickerActionListener(onPickerActionListener)
    }

    fun setMinimalPassenger(number: Int) {
        numberPickerWithCounterView?.setMinValue(number)
    }

    fun setMaximalPassenger(number: Int) {
        numberPickerWithCounterView?.setMaxValue(number)
    }

    fun hideSubtitle() {
        subtitleTextView?.visibility = GONE
    }

    companion object {
        private const val DEFAULT_VALUE = 0
        private const val DEFAULT_MIN_VALUE = 0
        private const val DEFAULT_MAX_VALUE = 100
        private const val PNG_EXTENSION = "png"
        private const val SVG_EXTENSION = "xml"
    }
}