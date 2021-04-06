package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presentation.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tkpd.library.utils.ImageHandler
import com.tokopedia.ordermanagement.orderhistory.R
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by kris on 11/7/17. Tokopedia
 */
class OrderHistoryStepperLayout : LinearLayout {
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.order_history_stepper, this, true)
    }

    fun setStepperStatus(model: OrderHistoryData) {
        val title = findViewById<Typography>(R.id.stepper_title)
        val stepperImage = findViewById<ImageView>(R.id.stepper_image)
        title.text = model.stepperStatusTitle
        model.orderListData.firstOrNull()?.color?.let {
            title.setTextColor(Color.parseColor(it))
        }
        if (model.historyImage.isEmpty()) visibility = View.GONE else ImageHandler.LoadImage(stepperImage, model.historyImage)
    }
}