package com.tokopedia.hotel.common.presentation.widget

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.hotel.R
import com.tokopedia.hotel.common.util.ErrorHandlerHotel

/**
 * @author by jessica on 11/06/19
 */
class HotelErrorBottomSheets : BottomSheets() {

    lateinit var listener: OnRetryListener
    var errorThrowable: Throwable? = null

    private lateinit var buttonRetry: Button
    private lateinit var errorTitleTv: TextView
    private lateinit var errorSubtitleTv: TextView
    private lateinit var errorImage: ImageView

    override fun getLayoutResourceId(): Int = R.layout.item_network_error_view

    override fun initView(view: View) {
        with(view) {
            buttonRetry = findViewById(R.id.button_retry)
            errorImage = findViewById(R.id.iv_icon)
            errorTitleTv = findViewById(R.id.message_retry)
            errorSubtitleTv = findViewById(R.id.sub_message_retry)

            setupErrorMessage()

            buttonRetry.setOnClickListener { listener.onRetryClicked() }
        }
    }

    fun setupErrorMessage() {
        if (errorThrowable != null) {
            errorImage.setImageResource(ErrorHandlerHotel.getErrorImage(errorThrowable))
            errorTitleTv.text = ErrorHandlerHotel.getErrorTitle(context, errorThrowable)
            errorSubtitleTv.text = ErrorHandlerHotel.getErrorMessage(context, errorThrowable)
        }
    }

    override fun title(): String = ""

    interface OnRetryListener {
        fun onRetryClicked()
    }
}