package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.recharge_credit_card.databinding.WidgetCcBankListBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

class RechargeCCBankListWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var listener: RechargeCCBankListListener? = null
    private var binding: WidgetCcBankListBinding = WidgetCcBankListBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        initClickListener()
    }

    private fun initClickListener() {
        binding.root.setOnClickListener {
            listener?.onClickBankList()
        }
    }

    fun setListener(listener: RechargeCCBankListListener) {
        this.listener = listener
    }

    interface RechargeCCBankListListener {
        fun onClickBankList()
    }
}
