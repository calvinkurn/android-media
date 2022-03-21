package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_component.databinding.WidgetRechargeSimplifyBuyBinding
import com.tokopedia.recharge_component.listener.RechargeSimplifyWidgetListener
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeSimplifyBuyWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var rechargeSimplifyBuyWidgetBinding = WidgetRechargeSimplifyBuyBinding.inflate(
            LayoutInflater.from(context), this, true)

    fun showSimplifyBuyWidget(listener: RechargeSimplifyWidgetListener){
        with(rechargeSimplifyBuyWidgetBinding){
            btnSimplifyBuyWidget.setOnClickListener {
                listener.onClickedButton()
            }
        }
    }

    fun isLoadingButton(isLoadingActive: Boolean){
        with(rechargeSimplifyBuyWidgetBinding){
            btnSimplifyBuyWidget.run {
                isLoading = isLoadingActive
            }
        }
    }

}