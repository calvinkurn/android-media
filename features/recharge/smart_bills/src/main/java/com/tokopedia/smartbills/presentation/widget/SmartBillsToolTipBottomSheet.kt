package com.tokopedia.smartbills.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.smartbills.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class SmartBillsToolTipBottomSheet : BottomSheetUnify() {
    lateinit var tooltipListener: Listener

    var buttonClickToolTip: UnifyButton? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    private fun initView(view: View?) {
        view?.let {
         buttonClickToolTip = view.findViewById(R.id.btn_tooltip_sbm)
         buttonClickToolTip?.setOnClickListener {
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