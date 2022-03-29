package com.tokopedia.tokofood.feature.ordertracking.presentation.partialview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.PartialOrderDetailStickyHelpButtonBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton

class OrderTrackingStickyHelpButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

    private var binding: PartialOrderDetailStickyHelpButtonBinding? = null

    init {
        binding = PartialOrderDetailStickyHelpButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }
}