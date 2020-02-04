package com.tokopedia.common_digital.common.widget

import android.content.Context
import android.util.AttributeSet
import com.tokopedia.common_digital.R
import com.tokopedia.design.text.SearchInputView
import org.jetbrains.annotations.NotNull

class DigitalSearchInputView @JvmOverloads constructor(@NotNull context: Context,
                                                       attrs: AttributeSet? = null,
                                                       defStyleAttr: Int = 0)
    : SearchInputView(context, attrs, defStyleAttr) {

    override fun getLayout(): Int {
        return R.layout.widget_digital_search_input_view
    }

    override fun getSearchImageViewResourceId(): Int {
        return R.id.image_view_search
    }

    override fun getSearchTextViewResourceId(): Int {
        return R.id.edit_text_search
    }

    override fun getCloseImageButtonResourceId(): Int {
        return R.id.image_button_close
    }

}