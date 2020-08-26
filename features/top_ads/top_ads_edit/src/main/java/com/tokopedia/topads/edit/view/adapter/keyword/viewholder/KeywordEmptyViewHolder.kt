package com.tokopedia.topads.edit.view.adapter.keyword.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.view.adapter.keyword.viewmodel.KeywordEmptyViewModel


class KeywordEmptyViewHolder(val view: View): KeywordViewHolder<KeywordEmptyViewModel>(view) {

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.topads_common_keyword_empty_view
    }

}