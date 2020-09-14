package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseGroupValidateName.TopAdsGroupValidateName
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.model.CreateGroupAdsViewModel
import com.tokopedia.topads.view.sheet.InfoSheetGroupList
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.*
import kotlinx.android.synthetic.main.topads_create_fragment_group_list.*
import kotlinx.android.synthetic.main.topads_create_fragment_group_list.tip_btn
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_TIPS_GRUP_IKLAN = "click-tips grup iklan"
private const val CLICK_BUAT_GRUP_IKLAN = "click-buat grup iklan"
class CreateGroupAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: CreateGroupAdsViewModel

    companion object {
        fun createInstance(): Fragment {

            val fragment = CreateGroupAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateGroupAdsViewModel::class.java)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
        stepperModel = CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperModel = CreateManualAdsStepperModel()
        stepperModel?.groupName = group_name_input.textFieldInput.text.toString()
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.group_name_step))
    }

    override fun getScreenName(): String {
        return CreateGroupAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_group_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit?.isEnabled = false
        btn_submit?.setOnClickListener {
            validateGroup(group_name_input.textFieldInput.text.toString())
        }
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(R.string.apa_itu_group_iklan)

            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(view.context.getResDrawable(R.drawable.topads_ic_tips))
        }

        tip_btn?.addItem(tooltipView)
        tip_btn?.setOnClickListener {
            val sheet = InfoSheetGroupList.newInstance()
            sheet.show(fragmentManager!!, "")
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_GRUP_IKLAN, "")
        }
        group_name_input?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error_text?.visibility = View.GONE
                btn_submit?.isEnabled = !s.toString().trim().isEmpty()
            }

        })
        group_name_input?.textFieldInput?.setOnEditorActionListener { v, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> validateGroup(v?.text.toString())
            }
            Utils.dismissKeyboard(context, v)
            true
        }
    }

    private fun validateGroup(s: String?) {
        s?.let {
            viewModel.validateGroup(it, this::onSuccess, this::onError)
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUAT_GRUP_IKLAN, it)
        }
    }

    private fun onError(t: Throwable) {
        errorTextVisibility(true)
        if (t.localizedMessage == resources.getString(R.string.duplicate_group_name_error_wrong))
            error_text?.text = resources.getString(R.string.duplicate_group_name_error)
        else
            error_text?.text = t.message
    }

    private fun onSuccess(data: TopAdsGroupValidateName.Data) {
        errorTextVisibility(false)
        gotoNextPage()
    }

    private fun errorTextVisibility(visible: Boolean) {
        if (visible) {
            error_text?.visibility = View.VISIBLE
            btn_submit?.isEnabled = false
        } else {
            error_text?.visibility = View.GONE
            btn_submit?.isEnabled = true
        }
    }
}