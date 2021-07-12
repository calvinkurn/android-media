
package com.tokopedia.hotel.evoucher.presentation.widget

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
class HotelMenuShareSheets : BottomSheetUnify() {

    lateinit var listener: HotelShareListener

    private lateinit var menuOrderList: TextView
    private lateinit var menuPromo: TextView
    private lateinit var menuHelp: TextView
    private lateinit var firstSeparator: View
    private lateinit var secondSeparator: View

    init {
        isFullpage = false
        isDragable = false
        setTitle("")
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

            menuHelp.setOnClickListener {
                if (::listener.isInitialized) listener.onSaveImageClicked()
                dismiss()
            }

            menuOrderList.text = context.getString(R.string.hotel_share_as_image)
            menuPromo.text = context.getString(R.string.hotel_share_as_pdf)
            menuHelp.text = context.getString(R.string.hotel_save_as_image)
        }
    }

    interface HotelShareListener {

        fun onShareAsImageClicked()

        fun onShareAsPdfClicked()

        fun onSaveImageClicked()

    }
}