package com.tokopedia.flight.common.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.flight.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class FlightMenuBottomSheet : BottomSheetUnify() {

    lateinit var listener: FlightMenuListener

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
        val view = View.inflate(context, R.layout.flight_menu_bottom_sheet, null)
        setChild(view)
        initView(view)
    }

    fun initView(view: View) {
        with(view) {
            menuOrderList = findViewById(R.id.tv_flight_menu_order_list)
            menuPromo = findViewById(R.id.tv_flight_menu_promo)
            menuHelp = findViewById(R.id.tv_flight_menu_help)

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

    interface FlightMenuListener {

        fun onOrderListClicked()

        fun onPromoClicked()

        fun onHelpClicked()

    }
}