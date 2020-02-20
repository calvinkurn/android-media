package com.tokopedia.autocomplete.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.autocomplete.R
import com.tokopedia.design.item.DeletableItemView

class AutocompleteDeletableItemView : DeletableItemView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initView(context: Context) {
        rootView = View.inflate(context, R.layout.widget_deleteable_autocomplete, this);
        textView = rootView.findViewById(R.id.item_name)
        buttonView = rootView.findViewById(R.id.delete_button)
    }
}