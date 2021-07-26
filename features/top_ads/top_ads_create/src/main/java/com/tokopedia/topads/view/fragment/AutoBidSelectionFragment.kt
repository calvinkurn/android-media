package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.UrlConstant
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.floatingbutton.FloatingButtonUnify
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

private const val MANUAL_BID = 10
private const val AUTO_BID = 20
class AutoBidSelectionFragment: BaseStepperFragment<CreateManualAdsStepperModel>() {

    private lateinit var nextBtn: UnifyButton
    private lateinit var otomatisLayout: ConstraintLayout
    private lateinit var manualLayout: ConstraintLayout
    private lateinit var otomatisRadioBtn: RadioButtonUnify
    private lateinit var manualRadioBtn: RadioButtonUnify
    private lateinit var tipBtn: FloatingButtonUnify
    var selected: Int = 0

    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    companion object {

        fun createInstance(): Fragment {
            val fragment = AutoBidSelectionFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.tokopedia.topads.common.R.layout.auto_bid_selection_layout, container, false)
        nextBtn = view.findViewById(com.tokopedia.topads.common.R.id.save_autobid)
        otomatisRadioBtn = view.findViewById(com.tokopedia.topads.common.R.id.radiobtn_otomatis)
        manualRadioBtn = view.findViewById(com.tokopedia.topads.common.R.id.radiobtn_manual)
        otomatisLayout = view.findViewById(com.tokopedia.topads.common.R.id.atur_otomatis)
        manualLayout = view.findViewById(com.tokopedia.topads.common.R.id.atur_manual)
        tipBtn = view.findViewById(com.tokopedia.topads.common.R.id.tip_btn)

        manualRadioBtn.setOnCheckedChangeListener(null)
        otomatisRadioBtn.setOnCheckedChangeListener(null)
        return view
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        if (stepperModel?.autoBidState?.isEmpty() == true)
            stepperListener?.goToNextPage(stepperModel)
        else {
            stepperModel?.redirectionToSummary = true
            stepperListener?.getToFragment(UrlConstant.FRAGMENT_NUMBER_4, stepperModel)
        }
    }

    private fun prepareView() {
        if (stepperModel?.redirectionToSummary == true) {
            nextBtn?.text = getString(R.string.topads_common_save_butt)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tipBtn.visibility = View.VISIBLE
        nextBtn.visibility = View.VISIBLE
        prepareView()

        if (selected == MANUAL_BID)
            manualRadioBtn?.isChecked = true
        else
            otomatisRadioBtn?.isChecked = true
        handleClick()

        nextBtn.setOnClickListener {
            gotoNextPage()
        }

        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(com.tokopedia.topads.common.R.string.tip_autobid_selection)

            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }

        tipBtn.addItem(tooltipView)
        tipBtn.setOnClickListener {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(com.tokopedia.topads.common.R.string.autobid_selection_tip_1, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(com.tokopedia.topads.common.R.string.autobid_selection_tip_2, R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(com.tokopedia.topads.common.R.string.autobid_selection_tip_3, R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.showHeader = true
            tipsListSheet?.showKnob = false
            tipsListSheet?.setTitle(getString(com.tokopedia.topads.common.R.string.tip_autobid_selection))
            tipsListSheet?.show(childFragmentManager, "")
        }
    }

    private fun handleClick() {
        manualLayout?.setOnClickListener {
            manualRadioBtn?.isChecked = true
            otomatisRadioBtn?.isChecked = false
            stepperModel?.autoBidState= ""
        }

        otomatisLayout?.setOnClickListener {
            otomatisRadioBtn?.isChecked = true
            manualRadioBtn?.isChecked = false
            stepperModel?.autoBidState= "auto_bid"
        }

        manualRadioBtn?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                otomatisRadioBtn.isChecked = false
                stepperModel?.autoBidState= ""
            }
        }
        otomatisRadioBtn?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                manualRadioBtn.isChecked = false
                stepperModel?.autoBidState= "auto_bid"

            }
        }
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(com.tokopedia.topads.common.R.string.autobid_list_step))
    }

    override fun getScreenName(): String {
        return AutoBidSelectionFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

}