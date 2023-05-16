package com.tokopedia.recharge_component.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.databinding.WidgetRechargeCheckBalanceBinding
import com.tokopedia.recharge_component.model.check_balance.RechargeCheckBalanceUnitModel
import com.tokopedia.recharge_component.presentation.adapter.RechargeCheckBalanceUnitAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull
import com.tokopedia.recharge_component.presentation.util.CustomDividerItemDecorator
import com.tokopedia.recharge_component.R

class RechargeCheckBalanceWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetRechargeCheckBalanceBinding.inflate(LayoutInflater.from(context), this, true)
    private var checkBalanceAdapter: RechargeCheckBalanceUnitAdapter? = null

    fun setBalanceInfo(balanceInfo: List<RechargeCheckBalanceUnitModel>) {
        binding.run {
            checkBalanceAdapter = RechargeCheckBalanceUnitAdapter().apply {
                setBalanceInfo(balanceInfo)
            }
            val dividerDrawable = ContextCompat.getDrawable(
                context,
                R.drawable.divider_client_number_check_balance
            )
            val layoutManager = GridLayoutManager(context, balanceInfo.size)
            checkBalanceRv.layoutManager = layoutManager
            checkBalanceRv.adapter = checkBalanceAdapter
            checkBalanceRv.addItemDecoration(
                CustomDividerItemDecorator(
                    context,
                    DividerItemDecoration.HORIZONTAL,
                    dividerDrawable
                )
            )
        }
    }

    fun showWarningMessage(
        message: String,
        iconUrl: String,
        onClick: () -> Unit
    ) {
        binding.run {
            checkBalanceWarningContainer.show()
            checkBalanceWarningIcon.loadImage(iconUrl)
            checkBalanceWarningTxt.text = MethodChecker.fromHtml(message)
            checkBalanceWarningContainer.setOnClickListener {
                onClick.invoke()
            }
        }
    }

    fun hideWarningMessage() {
        binding.checkBalanceWarningContainer.hide()
    }
}
