package com.tokopedia.hotel.globalsearch.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_hotel_global_search.view.*

/**
 * @author by furqan on 19/11/2019
 */
class HotelGlobalSearchWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

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

    fun setPreferencesText(preferencesText: String) {
        tg_hotel_widget_global_search_pref.text = preferencesText
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