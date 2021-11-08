package com.tokopedia.recharge_pdp_emoney.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.tokopedia.recharge_pdp_emoney.databinding.BottomSheetsEmoneyMenuBinding
import com.tokopedia.unifycomponents.BottomSheetUnify

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
        val view = BottomSheetsEmoneyMenuBinding.inflate(LayoutInflater.from(context))
        setChild(view.root)
        initView(view)
    }

    private fun initView(view:  BottomSheetsEmoneyMenuBinding) {
        with(view) {
            setTitle("")
            menuHelp.setOnClickListener {
                if (::listener.isInitialized) listener.onHelpClicked()
                dismiss()
            }

            menuOrderList.setOnClickListener {
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