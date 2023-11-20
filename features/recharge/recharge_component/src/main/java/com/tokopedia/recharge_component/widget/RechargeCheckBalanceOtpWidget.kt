package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeCheckBalanceOtpBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.NotificationUnify
import org.jetbrains.annotations.NotNull

class RechargeCheckBalanceOtpWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetRechargeCheckBalanceOtpBinding.inflate(LayoutInflater.from(context), this, true)

    fun setTitle(title: String) {
        binding.checkBalanceOtpTitle.text = title
    }

    fun setNotificationLabel(label: String) {
        if (label.isNotEmpty()) {
            binding.checkBalanceOtpLabel.setNotification(
                label,
                NotificationUnify.TEXT_TYPE,
                NotificationUnify.COLOR_PRIMARY
            )
            binding.checkBalanceOtpLabel.show()
        }
    }
}
