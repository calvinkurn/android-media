package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recharge_component.databinding.WidgetRechargeTickerBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeTickerWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeTickerViewBinding = WidgetRechargeTickerBinding.inflate(LayoutInflater.from(context), this, true)

    fun setText(text: String) {
        rechargeTickerViewBinding.tickerWidgetText.text = MethodChecker.fromHtml(text)
    }
}