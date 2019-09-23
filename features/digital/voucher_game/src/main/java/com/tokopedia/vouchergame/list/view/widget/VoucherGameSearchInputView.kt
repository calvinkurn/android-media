package com.tokopedia.vouchergame.list.view.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.vouchergame.R
import org.jetbrains.annotations.NotNull

class VoucherGameSearchInputView @JvmOverloads constructor(@NotNull context: Context,
                                                               attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : SearchInputView(context, attrs, defStyleAttr) {

    override fun getLayout(): Int {
        return R.layout.widget_vg_search_input_view
    }

}