package com.tokopedia.hotel.globalsearch.presentation.widget

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.WidgetHotelGlobalSearchBinding
import com.tokopedia.hotel.globalsearch.presentation.activity.HotelGlobalSearchActivity
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString

/**
 * @author by furqan on 19/11/2019
 */
class HotelGlobalSearchWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BaseCustomView(context, attrs, defStyleAttr) {

    private val binding = WidgetHotelGlobalSearchBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var title: String = "Default Title"

    private lateinit var checkInDate: String
    private lateinit var checkOutDate: String
    private var numOfGuests: Int = 0
    private var numOfRooms: Int = 0

    lateinit var globalSearchListener: GlobalSearchListener

    init {
        binding.tgHotelWidgetGlobalSearchChange.setOnClickListener {
            if (::globalSearchListener.isInitialized) {
                globalSearchListener.onClick(HotelGlobalSearchActivity.getIntent(context,
                        checkInDate, checkOutDate, numOfGuests, numOfRooms, title))
            } else {
                throw UnsupportedOperationException("${HotelGlobalSearchWidget::class.java.simpleName} click listener is not implemented")
            }
        }
    }

    fun setPreferencesData(checkInDate: String, checkOutDate: String, numOfGuests: Int, numOfRooms: Int) {
        this.checkInDate = checkInDate
        this.checkOutDate = checkOutDate
        this.numOfGuests = numOfGuests
        this.numOfRooms = numOfRooms
    }

    fun buildView() {
        val checkInString = checkInDate.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.VIEW_FORMAT_WITHOUT_YEAR)
        val checkOutString = checkOutDate.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.VIEW_FORMAT_WITHOUT_YEAR)
        binding.tgHotelWidgetGlobalSearchPref.text = context.getString(R.string.template_search_subtitle,
                checkInString, checkOutString, numOfRooms, numOfGuests)
    }

    /**
     * (optional) if you want to add some process when change button clicked, such as tracking, etc.
     */
    interface GlobalSearchListener {

        fun onClick(intent: Intent)

    }

}
