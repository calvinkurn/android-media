package com.tokopedia.topads.auto.view.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiModel
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiRowModel
import com.tokopedia.topads.common.view.sheet.TipsListSheet
import com.tokopedia.topads.common.view.widget.AutoAdsWidgetCommon
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

/**
 * Author errysuprayogi on 09,May,2019
 */

private const val EDIT_AUTOADS = 1
class EditAutoAdsBudgetFragment : AutoAdsBaseBudgetFragment(), View.OnClickListener {

    private var autoAdsWidget: AutoAdsWidgetCommon? = null
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null
    private var createAutoBg: ImageUnify? = null
    private var buttonLayout: LinearLayout? = null

    override fun getLayoutId(): Int {
        return R.layout.topads_autoads_edit_daily_budget
    }

    override fun setUpView(view: View) {
        autoAdsWidget = view.findViewById(R.id.autoads_edit_widget)
        createAutoBg = view.findViewById(R.id.create_auto_bg)
        buttonLayout = view.findViewById(R.id.buttonLayout)
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        btnSubmit.isEnabled = false
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        btnSubmit.isEnabled = true
    }

    override fun showButtonLayout() {
        buttonLayout?.visible()
    }

    override fun setListener() {
        btnSubmit.setOnClickListener(this)
        tipBtn.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createAutoBg?.setImageDrawable(context?.getResDrawable(R.drawable.card_auto_ads_create))
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(com.tokopedia.topads.common.R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.tip_title)
            imgTooltipIcon = this.findViewById(com.tokopedia.topads.common.R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }

        tipBtn.addItem(tooltipView)
        autoAdsWidget?.loadData(EDIT_AUTOADS)
    }

    override fun initInjector() {
        getComponent(AutoAdsComponent::class.java).inject(this)
    }

    override fun getScreenName(): String {
        return EditAutoAdsBudgetFragment::class.java.name
    }

    companion object {

        fun newInstance(): EditAutoAdsBudgetFragment {
            val args = Bundle()
            val fragment = EditAutoAdsBudgetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_submit) {
            activatedAds(EDIT_AUTOADS)
        }
        if (v?.id == R.id.tip_btn) {
            val tipsList: ArrayList<TipsUiModel> = ArrayList()
            tipsList.apply {
                add(TipsUiRowModel(R.string.edit_auto_ads_sheet_desc1, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.edit_auto_ads_sheet_desc2, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
                add(TipsUiRowModel(R.string.edit_auto_ads_sheet_desc3, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
            }
            val tipsListSheet = context?.let { it1 -> TipsListSheet.newInstance(it1, tipsList = tipsList) }
            tipsListSheet?.isDragable = false
            tipsListSheet?.showHeader = true
            tipsListSheet?.showKnob = false
            tipsListSheet?.isHideable = false
            tipsListSheet?.setTitle(getString(R.string.tip_title))
            tipsListSheet?.show(childFragmentManager, "")
        }
        if (v?.id == R.id.btn_cancel) {
            moveToInitialState()
        }
    }

    private fun moveToInitialState() {
        buttonLayout?.gone()
        super.moveToIntialState()
    }
}
