package com.tokopedia.common.topupbills.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.common.topupbills.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheets_topup_bills_menu.view.*

/**
 * @author by resakemal on 26/08/19
 */
class TopupBillsMenuBottomSheets : BottomSheetUnify() {

    lateinit var listener: MenuListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_topup_bills_menu, null)
        setChild(view)
        initView(view)
    }

    private fun initView(view: View) {
        with(view) {
            setTitle("")
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

    companion object {

        fun newInstance(): TopupBillsMenuBottomSheets {
            return TopupBillsMenuBottomSheets()
        }
    }

    interface MenuListener {

        fun onOrderListClicked()

        fun onPromoClicked()

        fun onHelpClicked()

    }
}