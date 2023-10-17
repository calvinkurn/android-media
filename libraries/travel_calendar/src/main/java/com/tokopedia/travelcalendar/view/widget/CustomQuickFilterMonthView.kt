package com.tokopedia.travelcalendar.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.travelcalendar.databinding.ItemMonthQuickFilterBinding
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 28/12/18.
 */
class CustomQuickFilterMonthView @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: ItemMonthQuickFilterBinding = ItemMonthQuickFilterBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setTextMonth(month: String) {
        binding.month.text = month
    }
}
