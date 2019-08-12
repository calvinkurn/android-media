package com.tokopedia.hotel.destination.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.item.DeletableItemView
import com.tokopedia.hotel.R

class HotelDeletableItemView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    DeletableItemView(context, attrs, defStyleAttr) {

    override fun initView(context: Context) {
        rootView = View.inflate(context, R.layout.layout_widget_deletable_item, this);
        textView = rootView.findViewById(R.id.item_name)
        buttonView = rootView.findViewById(R.id.delete_button)
    }
}