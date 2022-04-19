package com.tokopedia.common.travel.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.travel.R
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * Created by nabillasabbaha on 29/08/18.
 */
class DepartureTripLabelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var tvDestination: TextView? = null
    private var tvName: TextView? = null
    private var tvTime: TextView? = null
    private var tvPrice: TextView? = null
    private var imageDeparture: AppCompatImageView? = null
    private var icon: Drawable? = null

    init {
        init(attrs)
    }

    private fun init() {
        val view = inflate(context, R.layout.widget_departure_trip_label, this)
        tvDestination = view.findViewById(R.id.tv_destination_departure_header)
        tvName = view.findViewById(R.id.tv_name_departure_header)
        tvTime = view.findViewById(R.id.tv_time_departure_header)
        tvPrice = view.findViewById(R.id.tv_price_departure_header)
        imageDeparture = view.findViewById(R.id.image_departure_header)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.DepartureTripLabelView)

            // get resource full name, to use later for checking is it png or vector
            val value = TypedValue()
            context.resources.getValue(styledAttributes.getResourceId(R.styleable.DepartureTripLabelView_dlv_icon, R.drawable.ic_travel_flight), value, true)
            try {
                if (value.string.toString().contains(SVG_EXTENSION)) {
                    icon = VectorDrawableCompat.create(context.resources, styledAttributes.getResourceId(R.styleable.DepartureTripLabelView_dlv_icon, R.drawable.ic_travel_flight), context.theme)
                } else if (value.string.toString().contains(PNG_EXTENSION)) {
                    icon = styledAttributes.getDrawable(R.styleable.DepartureTripLabelView_dlv_icon)
                }
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (icon != null) {
            imageDeparture?.setImageDrawable(icon)
        }
    }

    fun setValueDestination(destination: String?) {
        tvDestination?.text = destination
    }

    fun setValueName(name: String?) {
        tvName?.text = name
    }

    fun setValueTime(time: String?) {
        tvTime?.text = time
    }

    fun setValuePrice(price: String?) {
        tvPrice?.text = MethodChecker.fromHtml(String.format(resources.getString(
                R.string.travel_departure_trip_price_value), price))
    }

    companion object {
        private const val PNG_EXTENSION = "png"
        private const val SVG_EXTENSION = "xml"
    }
}