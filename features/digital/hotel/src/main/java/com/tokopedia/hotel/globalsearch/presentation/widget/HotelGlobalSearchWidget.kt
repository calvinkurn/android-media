package com.tokopedia.hotel.globalsearch.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_hotel_global_search.view.*

/**
 * @author by furqan on 19/11/2019
 */
class HotelGlobalSearchWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    lateinit var checkInDate: String
    lateinit var checkOutDate: String
    var numOfGuests: Int = 0
    var numOfRooms: Int = 0

    var globalSearchListener: GlobalSearchListener? = null
        set(value) {
            field = value
            globalSearchListener?.let { listener ->
                tg_hotel_widget_global_search_change.setOnClickListener {
                    listener.onChangeClick()
                    navigateToChangePreferencePage()
                }
            }
        }

    init {
        View.inflate(context, R.layout.widget_hotel_global_search, this)

        tg_hotel_widget_global_search_change.setOnClickListener { navigateToChangePreferencePage() }
    }

    fun setPreferencesData(checkInDate: String, checkOutDate: String, numOfGuests: Int, numOfRooms: Int) {
        this.checkInDate = checkInDate
        this.checkOutDate = checkOutDate
        this.numOfGuests = numOfGuests
        this.numOfRooms = numOfRooms
    }

    fun buildView() {
        val checkInString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR,
                TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkInDate))
        val checkOutString = TravelDateUtil.dateToString(TravelDateUtil.VIEW_FORMAT_WITHOUT_YEAR,
                TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, checkOutDate))
        tg_hotel_widget_global_search_pref.text = context.getString(R.string.template_search_subtitle,
                checkInString, checkOutString, numOfRooms, numOfGuests)
    }

    private fun navigateToChangePreferencePage() {

    }

    /**
     * (optional) if you want to add some process when change button clicked, such as tracking, etc.
     */
    interface GlobalSearchListener {
        fun onChangeClick()
    }

}