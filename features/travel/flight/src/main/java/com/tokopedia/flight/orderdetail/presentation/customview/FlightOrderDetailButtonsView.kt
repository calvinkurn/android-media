package com.tokopedia.flight.orderdetail.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.flight.R
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailButtonModel
import kotlinx.android.synthetic.main.view_flight_order_detail_check_in_detail.view.*

/**
 * @author by furqan on 13/11/2020
 */
class FlightOrderDetailButtonsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    var listener: Listener? = null

    private lateinit var title: String
    private lateinit var topButtonData: FlightOrderDetailButtonModel
    private lateinit var bottomButtonData: FlightOrderDetailButtonModel

    init {
        View.inflate(context, R.layout.view_flight_order_detail_check_in_detail, this)
    }

    fun setData(title: String,
                topButtonData: FlightOrderDetailButtonModel,
                bottomButtonData: FlightOrderDetailButtonModel) {
        this.title = title
        this.topButtonData = topButtonData
        this.bottomButtonData = bottomButtonData
    }

    fun buildView() {
        tgFlightOrderButtonViewTitle.text = title
        renderTopButton()
        renderBottomButton()
    }

    private fun renderTopButton() {
        if (topButtonData.isVisible) {
            cvFlightOrderTopButton.visibility = View.VISIBLE
            ivFlightOrderTopButtonLeftIcon.setImageDrawable(topButtonData.leftIcon)
            tgFlightOrderTopButtonTopText.text = topButtonData.topText
            if (topButtonData.bottomText.isNotEmpty()) {
                tgFlightOrderTopButtonBottomText.text = topButtonData.bottomText
                tgFlightOrderTopButtonBottomText.visibility = View.VISIBLE
            } else {
                tgFlightOrderTopButtonBottomText.visibility = View.GONE
            }
            cvFlightOrderTopButton.setOnClickListener {
                listener?.onTopButtonClicked()
            }
            if (topButtonData.isActionAvailable) {
                ivFlightOrderTopButtonRightIcon.visibility = View.VISIBLE
            } else {
                ivFlightOrderTopButtonRightIcon.visibility = View.GONE
            }

            cvFlightOrderTopButton.isEnabled = topButtonData.isClickable
            ivFlightOrderTopButtonLeftIcon.isEnabled = topButtonData.isClickable
            tgFlightOrderTopButtonTopText.isEnabled = topButtonData.isClickable
            tgFlightOrderTopButtonBottomText.isEnabled = topButtonData.isClickable
            ivFlightOrderTopButtonRightIcon.isEnabled = topButtonData.isClickable
        } else {
            cvFlightOrderTopButton.visibility = View.GONE
        }
    }

    private fun renderBottomButton() {
        if (bottomButtonData.isVisible) {
            cvFlightOrderBottomButton.visibility = View.VISIBLE
            ivFlightOrderBottomButtonLeftIcon.setImageDrawable(bottomButtonData.leftIcon)
            tgFlightOrderBottomButtonTopText.text = bottomButtonData.topText
            if (bottomButtonData.bottomText.isNotEmpty()) {
                tgFlightOrderBottomButtonBottomText.text = bottomButtonData.bottomText
                tgFlightOrderBottomButtonBottomText.visibility = View.VISIBLE
            } else {
                tgFlightOrderBottomButtonBottomText.visibility = View.GONE
            }
            cvFlightOrderBottomButton.setOnClickListener {
                listener?.onBottomButtonClicked()
            }
            if (bottomButtonData.isActionAvailable) {
                ivFlightOrderBottomButtonRightIcon.visibility = View.VISIBLE
            } else {
                ivFlightOrderBottomButtonRightIcon.visibility = View.GONE
            }
            cvFlightOrderBottomButton.isEnabled = bottomButtonData.isClickable
            ivFlightOrderBottomButtonLeftIcon.isEnabled = bottomButtonData.isClickable
            tgFlightOrderBottomButtonTopText.isEnabled = bottomButtonData.isClickable
            tgFlightOrderBottomButtonBottomText.isEnabled = bottomButtonData.isClickable
            ivFlightOrderBottomButtonRightIcon.isEnabled = bottomButtonData.isClickable
        } else {
            cvFlightOrderBottomButton.visibility = View.GONE
        }
    }

    interface Listener {
        fun onTopButtonClicked()
        fun onBottomButtonClicked()
    }

}