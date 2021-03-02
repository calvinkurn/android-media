package com.tokopedia.notifcenter.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.notifcenter.R

class ItemOrderListLinearLayout : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    constructor(
            context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        initView()
    }

    private fun initView() {
        View.inflate(context, R.layout.partial_notification_card_order_list, this)?.apply {
            initViewBinding(this)
        }
    }

    private fun initViewBinding(view: View) {

    }
}