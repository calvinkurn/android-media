package com.tokopedia.common.topupbills.view.bottomsheet

import android.view.View
import com.tokopedia.common.topupbills.R
import com.tokopedia.design.component.BottomSheets
import kotlinx.android.synthetic.main.bottom_sheets_topup_bills_menu.view.*

/**
 * @author by resakemal on 26/08/19
 */
class TopupBillsMenuBottomSheets : BottomSheets() {

    lateinit var listener: MenuListener

    override fun getLayoutResourceId(): Int = R.layout.bottom_sheets_topup_bills_menu

    override fun initView(view: View) {
        with(view) {
            menu_promo.setOnClickListener {
                if (::listener.isInitialized) listener.onPromoClicked()
                dismiss()
            }

            menu_help.setOnClickListener {
                if (::listener.isInitialized) listener.onHelpClicked()
                dismiss()
            }

            menu_order_list.setOnClickListener {
                if (::listener.isInitialized) listener.onOrderListClicked()
                dismiss()
            }
        }
    }

    override fun title(): String = ""

    override fun state(): BottomSheetsState = BottomSheetsState.FLEXIBLE

    interface MenuListener {

        fun onOrderListClicked()

        fun onPromoClicked()

        fun onHelpClicked()

    }
}