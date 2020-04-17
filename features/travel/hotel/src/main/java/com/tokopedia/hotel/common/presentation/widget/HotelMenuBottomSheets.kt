package com.tokopedia.hotel.common.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.hotel.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by furqan on 11/04/19
 */
class HotelMenuBottomSheets : BottomSheetUnify() {

    lateinit var listener: HotelMenuListener

    private lateinit var menuOrderList: TextView
    private lateinit var menuPromo: TextView
    private lateinit var menuHelp: TextView

    init {
        isFullpage = false
        setTitle("")
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_menu, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
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

    interface HotelMenuListener {

        fun onOrderListClicked()

        fun onPromoClicked()

        fun onHelpClicked()

    }
}