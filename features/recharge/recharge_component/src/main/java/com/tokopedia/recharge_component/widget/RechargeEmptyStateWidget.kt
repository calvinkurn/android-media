package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recharge_component.databinding.WidgetRechargeEmptyStateBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeEmptyStateWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeEmptyStateBinding = WidgetRechargeEmptyStateBinding.inflate(LayoutInflater.from(context), this, true)
    var imageUrl = ""
        set(url: String) {
            field = url
            rechargeEmptyStateBinding.emptyStateWidgetImage.setImageUrl(url)
        }
}