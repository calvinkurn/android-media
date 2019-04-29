package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.item_hotel_room_facility_list.view.*

/**
 * @author by jessica on 29/04/19
 */

class FacilityTextView(context: Context) : BaseCustomView(context) {

    init {
        View.inflate(context, R.layout.item_hotel_room_facility_list, this)
    }

    fun setIconAndText(iconUrl: String, text: String) {
        try {
            Glide.with(context)
                    .load(iconUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(com.tokopedia.design.R.drawable.ic_loading_image)
                    .error(R.drawable.ic_facility_add)
                    .centerCrop()
                    .into(facility_icon)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        facility_text_view.text = text
    }

    fun setIconAndText(iconDrawableResId: Int, text: String) {
        facility_icon.setImageResource(iconDrawableResId)
        facility_text_view.text = text
    }

}