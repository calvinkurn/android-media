package com.tokopedia.hotel.common.presentation.widget

import android.view.View
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.hotel.R

/**
 * @author by furqan on 11/04/19
 */
class HotelMenuBottomSheets : BottomSheets() {

    lateinit var listener: HotelMenuListener

    private lateinit var menuOrderList: TextView
    private lateinit var menuPromo: TextView
    private lateinit var menuHelp: TextView

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_hotel_menu

    override fun initView(view: View) {
        with(view) {
            menuOrderList = findViewById(R.id.tv_hotel_menu_order_list)
            menuPromo = findViewById(R.id.tv_hotel_menu_promo)
            menuHelp = findViewById(R.id.tv_hotel_menu_help)

            menuOrderList.setOnClickListener {
                if (::listener.isInitialized) listener.onOrderListClicked()
                dismiss()
            }

            menuPromo.setOnClickListener {
                if (::listener.isInitialized) listener.onPromoClicked()
                dismiss()
            }

            menuHelp.setOnClickListener {
                if (::listener.isInitialized) listener.onHelpClicked()
                dismiss()
            }
        }
    }

    override fun title(): String = ""

    override fun state(): BottomSheetsState = BottomSheetsState.FLEXIBLE

    interface HotelMenuListener {

        fun onOrderListClicked()

        fun onPromoClicked()

        fun onHelpClicked()

    }
}