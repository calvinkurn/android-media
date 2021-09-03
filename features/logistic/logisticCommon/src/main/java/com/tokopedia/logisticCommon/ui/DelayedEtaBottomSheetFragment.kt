package com.tokopedia.logisticCommon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.logisticCommon.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class DelayedEtaBottomSheetFragment : BottomSheetUnify() {

    private var eta: String = ""
    private var tvDescription: Typography? = null
    private var btnDismiss: UnifyButton? = null

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
        View.inflate(context, R.layout.fragment_delayed_eta_bottom_sheet, null).apply {
            setChild(this)
            bindView(this)
        }
        if (eta.isNotEmpty()) tvDescription?.text =
            getString(R.string.delayed_eta_bottomsheet_description, eta)
        setViewListener()
    }

    private fun bindView(view: View) {
        with(view) {
            tvDescription = findViewById(R.id.description)
            btnDismiss = findViewById(R.id.btn)
        }
    }

    private fun setViewListener() {
        btnDismiss?.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        private const val ETA = "eta"
        fun newInstance(): DelayedEtaBottomSheetFragment {
            return DelayedEtaBottomSheetFragment()
        }

        fun newInstance(eta: String): DelayedEtaBottomSheetFragment {
            return DelayedEtaBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(ETA, eta)
                }
            }
        }
    }
}