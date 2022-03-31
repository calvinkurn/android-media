package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.tokofood.databinding.TokofoodPartialOrderDetailStickyHelpButtonBinding
import com.tokopedia.unifycomponents.BaseCustomView


class OrderTrackingStickyHelpButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: TokofoodPartialOrderDetailStickyHelpButtonBinding? = null

    init {
        binding = TokofoodPartialOrderDetailStickyHelpButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }
}