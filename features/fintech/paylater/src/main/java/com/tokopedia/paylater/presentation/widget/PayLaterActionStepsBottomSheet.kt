package com.tokopedia.paylater.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterPartnerStepDetails
import com.tokopedia.paylater.presentation.adapter.PayLaterActionStepsAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_action_steps_bottomsheet_widget.*

class PayLaterActionStepsBottomSheet : BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.paylater_action_steps_bottomsheet_widget
    private var partnerUsageData: PayLaterPartnerStepDetails? = null
    private var sheetTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
        getArgumentData()
    }

    private fun getArgumentData() {
        arguments?.let {
            partnerUsageData = it.getParcelable(STEPS_DATA)
            sheetTitle = it.getString(ACTION_TITLE) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val notesData = partnerUsageData?.partnerNotes?.getOrNull(0)
        if (!notesData.isNullOrEmpty())
            tickerPaylaterRegister.setTextDescription(MethodChecker.fromHtml(notesData))
        else tickerPaylaterRegister.gone()
        initAdapter()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
        setTitle(sheetTitle)
    }

    private fun initAdapter() {
        rvPayLaterRegisterSteps.adapter = PayLaterActionStepsAdapter(partnerUsageData?.partnerSteps ?: ArrayList())
        rvPayLaterRegisterSteps.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {

        private const val TAG = "FT_TAG"
        const val STEPS_DATA = "stepsData"
        const val ACTION_TITLE = "actionTitle"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val payLaterRegisterBottomSheet = PayLaterActionStepsBottomSheet().apply {
                arguments = bundle
            }
            payLaterRegisterBottomSheet.show(childFragmentManager, TAG)
        }
    }
}