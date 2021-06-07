package com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheets_emoney_menu.view.*

/**
 * @author by resakemal on 26/08/19
 */
class EmoneyMenuBottomSheets : BottomSheetUnify() {

    lateinit var listener: MenuListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_emoney_menu, null)
        setChild(view)
        initView(view)
    }

    private fun initView(view: View) {
        with(view) {
            setTitle("")
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
        fun newInstance(): EmoneyMenuBottomSheets {
            return EmoneyMenuBottomSheets()
        }
    }

    interface MenuListener {
        fun onOrderListClicked()
        fun onHelpClicked()
    }
}