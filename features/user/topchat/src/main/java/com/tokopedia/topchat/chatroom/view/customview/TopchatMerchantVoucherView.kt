package com.tokopedia.topchat.chatroom.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.merchantvoucher.common.widget.MerchantVoucherView
import com.tokopedia.topchat.R

class TopchatMerchantVoucherView : MerchantVoucherView {

    private var voucherContainer: LinearLayout? = null
    private var readStatusContainer: LinearLayout? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun getVoucherLayout(): Int {
        return R.layout.item_topchat_widget_merchant_voucher_view
    }

    override fun initView(view: View) {
        voucherContainer = view.findViewById(R.id.vgVoucherView)
        readStatusContainer = view.findViewById(R.id.llReadStatusContainer)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildWithMargins(readStatusContainer, widthMeasureSpec, 0, heightMeasureSpec, 0)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun maxYModifier(): Int {
        readStatusContainer?.let {
            val readStatusContainerParam = it.layoutParams as LinearLayout.LayoutParams
            return it.measuredHeight - readStatusContainerParam.bottomMargin
        }
        return 0
    }

    override fun getShadowColor(): Int {
        return ContextCompat.getColor(context, R.color.shadow_topchat_voucher_attachment)
    }
}