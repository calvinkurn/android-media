package com.tokopedia.logisticorder.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.databinding.FragmentDelayedEtaBottomSheetBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.android.synthetic.main.fragment_delayed_eta_bottom_sheet.view.*

class DelayedEtaBottomSheetFragment : BottomSheetUnify() {

    private var binding by autoCleared<FragmentDelayedEtaBottomSheetBinding>()
    private var eta: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            eta = it.getString(ETA, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView() {
        binding = FragmentDelayedEtaBottomSheetBinding.inflate(LayoutInflater.from(context), null, false)
        setChild(binding.root)
        if (eta.isNotEmpty()) binding.description.text = getString(R.string.delayed_eta_bottomsheet_description, eta)
        setViewListener()
    }

    private fun setViewListener() {
        binding.root.btn.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val ETA = "eta"
        fun newInstance() : DelayedEtaBottomSheetFragment {
            return DelayedEtaBottomSheetFragment()
        }

        fun newInstance(eta: String) : DelayedEtaBottomSheetFragment {
            return DelayedEtaBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ETA, eta)
                }
            }
        }
    }
}