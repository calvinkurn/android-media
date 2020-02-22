package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.model.CreateGroupAdsViewModel
import com.tokopedia.topads.view.sheet.InfoSheetGroupList
import kotlinx.android.synthetic.main.topads_create_fragment_group_list.*
import javax.inject.Inject
import com.tokopedia.topads.data.response.ResponseGroupValidateName.TopAdsGroupValidateName
import com.tokopedia.topads.view.activity.StepperActivity

/**
 * Author errysuprayogi on 29,October,2019
 */
class CreateGroupAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {


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
        stepperModel?.groupName = group_name_input.text.toString()
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView(stepperModel: CreateManualAdsStepperModel) {
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
     //   btn_submit.isEnabled = false
        btn_submit.setOnClickListener {
            if(group_name_input.text.toString().trim()==""){
                SnackbarManager.make(activity,
                        resources.getString(R.string.group_name_empty_error),
                        Snackbar.LENGTH_LONG)
                        .show()
            }
            else {
                validateGroup(group_name_input.text.toString())
                gotoNextPage()
            }
        }
        tip_btn.setOnClickListener {
            InfoSheetGroupList.newInstance(it.context).show()
        }
        group_name_input.setOnEditorActionListener { v, actionId, _ ->
            when(actionId){
                EditorInfo.IME_ACTION_SEARCH -> validateGroup(v?.text.toString())
            }
            Utils.dismissKeyboard(context, v)
            true
        }
    }

    private fun validateGroup(s: String?) {
        s?.let {
            if (s.isNotEmpty()) {
                viewModel.validateGroup(it, this::onSuccess, this::onError)
            }
        }
    }

    private fun onError(t: Throwable) {
//        SnackbarManager.make(activity, t.message,
//                Snackbar.LENGTH_LONG)
//                .show()
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage) {
            btn_submit.isEnabled = false
            validateGroup(group_name_input.text.toString())
        }
    }

    private fun onSuccess(data: TopAdsGroupValidateName.Data) {
        btn_submit.isEnabled = true
    }

    override fun updateToolBar() {
        (activity as StepperActivity).updateToolbarTitle(getString(R.string.group_name_step))
    }

}