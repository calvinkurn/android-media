package com.tokopedia.hotel.destination.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.hotel.R

/**
 * Wee need this class instead using layoutRef in app:layout in the .xml file
 * This is a hacky way to get package R in this module (for dynamic feature)
 */
class HotelDeletableItemView : DeletableItemView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun initView(context: Context) {
        rootView = View.inflate(context, R.layout.layout_widget_deletable_item, this);
        textView = rootView.findViewById(R.id.item_name)
        buttonView = rootView.findViewById(R.id.delete_button)
    }
}