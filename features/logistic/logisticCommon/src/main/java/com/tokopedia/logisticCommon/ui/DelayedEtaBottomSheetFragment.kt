package com.tokopedia.logisticCommon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.empty_state.EmptyStateUnify
import com.tokopedia.logisticCommon.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class DelayedEtaBottomSheetFragment : BottomSheetUnify() {

    private var description: String = ""
    private var emptyState: EmptyStateUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            description = it.getString(DELAYED_INFO, "")
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
        if (description.isNotEmpty()) emptyState?.emptyStateDescriptionID?.text = MethodChecker.fromHtml(description)
        setViewListener()
    }

    private fun bindView(view: View) {
        with(view) {
            emptyState = findViewById(R.id.eta_empty_state)
        }
    }

    private fun setViewListener() {
        emptyState?.setPrimaryCTAClickListener {
            dismiss()
        }
    }

    companion object {
        private const val DELAYED_INFO = "eta"
        fun newInstance(): DelayedEtaBottomSheetFragment {
            return DelayedEtaBottomSheetFragment()
        }

        fun newInstance(delayedInfo: String): DelayedEtaBottomSheetFragment {
            return DelayedEtaBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(DELAYED_INFO, delayedInfo)
                }
            }
        }
    }
}