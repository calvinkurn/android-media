package com.tokopedia.logisticorder.view.tipping

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.tokopedia.logisticorder.databinding.FragmentTippingDriverBinding
import com.tokopedia.logisticorder.utils.TippingConstant.PARAM_ORDER_ID
import com.tokopedia.logisticorder.utils.TippingConstant.PARAM_REF_NUM
import com.tokopedia.logisticorder.view.bottomsheet.DriverTippingBottomSheet
import com.tokopedia.utils.lifecycle.autoClearedNullable

class TippingDriverFragment : Fragment() {

    private var binding by autoClearedNullable<FragmentTippingDriverBinding>()
    private var bottomSheet: DriverTippingBottomSheet? = null

    private var orderId: String = ""
    private var refNum: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orderId = it.getString(PARAM_ORDER_ID, "")
            refNum = it.getString(PARAM_REF_NUM, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTippingDriverBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openBottomSheet()
        setOnBackPressed()
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    bottomSheet?.dismiss()
                    activity?.finish()
                }
            }
        )
    }

    private fun openBottomSheet() {
        bottomSheet = DriverTippingBottomSheet()
        bottomSheet?.setOnDismissListener {
            activity?.finish()
        }
        bottomSheet?.show(parentFragmentManager, orderId, refNum)
    }

    private fun doFinishActivity(result: Int = RESULT_CANCELED) {
        activity?.apply {
            setResult(result, Intent())
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val window = activity?.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {

        fun newInstance(orderId: String, refNum: String): TippingDriverFragment {
            return TippingDriverFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, orderId)
                    putString(PARAM_REF_NUM, refNum)
                }
            }
        }
    }
}
