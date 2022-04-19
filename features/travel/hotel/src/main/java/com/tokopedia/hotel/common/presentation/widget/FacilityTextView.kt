package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.hotel.R
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.item_hotel_room_facility_list.view.*

/**
 * @author by jessica on 29/04/19
 */

class FacilityTextView(context: Context) : BaseCustomView(context) {

    init {
        View.inflate(context, R.layout.item_hotel_room_facility_list, this)
    }

    fun setIconAndText(iconUrl: String, text: String) {
        facility_icon.loadIcon(iconUrl){
            setPlaceHolder(com.tokopedia.unifycomponents.R.drawable.imagestate_placeholder)
        }
        facility_text_view.text = text
    }

    fun setIconAndText(iconDrawableResId: Int, text: String) {
        facility_icon.setImageResource(iconDrawableResId)
        facility_text_view.text = text
    }

}