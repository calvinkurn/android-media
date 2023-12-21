package com.tokopedia.hotel.common.presentation.widget

import android.content.Context
import android.view.LayoutInflater
import com.tokopedia.hotel.databinding.ItemHotelRoomFacilityListBinding
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * @author by jessica on 29/04/19
 */

class FacilityTextView(context: Context) : BaseCustomView(context) {

    private var binding = ItemHotelRoomFacilityListBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setIconAndText(iconUrl: String, text: String) {
        with(binding) {
            facilityIcon.loadIcon(iconUrl){
                setPlaceHolder(unifycomponentsR.drawable.imagestate_placeholder)
            }
            facilityTextView.text = text
        }
    }

    fun setIconAndText(iconDrawableResId: Int, text: String) {
        with(binding) {
            facilityIcon.setImageResource(iconDrawableResId)
            facilityTextView.text = text
        }
    }

}
