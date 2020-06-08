package com.tokopedia.seller.search.feature.initialsearch.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.seller.search.R
import com.tokopedia.unifycomponents.BaseCustomView

class GlobalSearchView: BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_global_search_view, this)
    }
}