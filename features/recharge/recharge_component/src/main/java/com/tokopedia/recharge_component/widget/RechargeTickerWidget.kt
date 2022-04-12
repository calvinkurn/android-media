package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.recharge_component.databinding.WidgetRechargeTickerBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeTickerWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeTickerViewBinding = WidgetRechargeTickerBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        initView()
    }

    private fun initView() {
       rechargeTickerViewBinding.tickerWidgetIcon.setImageUrl(TOKOPEDIA_ICON_URL)
    }

    fun setText(text: String) {
        rechargeTickerViewBinding.tickerWidgetText.text = MethodChecker.fromHtml(text)
    }

    companion object {
        private const val TOKOPEDIA_ICON_URL = "https://images.tokopedia.net/img/android/recharge/recharge_component/recharge_component_icon-tokopedia.webp"
    }
}