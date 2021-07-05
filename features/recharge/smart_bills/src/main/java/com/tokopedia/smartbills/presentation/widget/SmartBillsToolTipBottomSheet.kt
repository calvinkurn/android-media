package com.tokopedia.smartbills.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.RouteManager
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottomsheet_smartbills_tooltip.*

class SmartBillsToolTipBottomSheet : BottomSheetUnify() {
    lateinit var tooltipListener: Listener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initView(view: View) {
        with(view) {
            btn_tooltip_sbm.setOnClickListener {
                tooltipListener.onClickMoreLearn()
            }
        }
    }

    companion object {
        private const val TAG = "SmartBillsToolTipBottomSheet"

        @JvmStatic
        fun newInstance(context: Context, listener: SmartBillsToolTipBottomSheet.Listener): SmartBillsToolTipBottomSheet {
            return SmartBillsToolTipBottomSheet().apply {
                val childView = View.inflate(context, R.layout.bottomsheet_smartbills_tooltip, null)
                tooltipListener = listener
                setChild(childView)
                setCloseClickListener { this.dismiss() }
            }
        }
    }

    interface Listener{
        fun onClickMoreLearn()
    }
}