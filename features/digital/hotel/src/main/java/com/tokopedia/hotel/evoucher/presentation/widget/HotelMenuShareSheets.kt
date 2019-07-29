
package com.tokopedia.hotel.evoucher.presentation.widget

import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.hotel.R

/**
 * @author by furqan on 11/04/19
 */
class HotelMenuShareSheets : BottomSheets() {

    lateinit var listener: HotelShareListener

    private lateinit var menuOrderList: TextViewCompat
    private lateinit var menuPromo: TextViewCompat
    private lateinit var menuHelp: TextViewCompat
    private lateinit var firstSeparator: View
    private lateinit var secondSeparator: View

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_menu

    override fun initView(view: View) {
        with(view) {
            menuOrderList = findViewById(R.id.tv_hotel_menu_order_list)
            menuPromo = findViewById(R.id.tv_hotel_menu_promo)
            menuHelp = findViewById(R.id.tv_hotel_menu_help)
            firstSeparator = findViewById(R.id.v_first_separator)
            secondSeparator = findViewById(R.id.v_second_separator)

            menuOrderList.setOnClickListener {
                if (::listener.isInitialized) listener.onShareAsImageClicked()
                dismiss()
            }

            menuPromo.setOnClickListener {
                if (::listener.isInitialized) listener.onShareAsPdfClicked()
                dismiss()
            }

            menuOrderList.text = context.getString(R.string.hotel_share_as_image)
            menuPromo.text = context.getString(R.string.hotel_share_as_pdf)
            menuHelp.visibility = View.GONE
            secondSeparator.visibility = View.GONE
        }
    }

    override fun title(): String = ""

    interface HotelShareListener {

        fun onShareAsImageClicked()

        fun onShareAsPdfClicked()

    }
}