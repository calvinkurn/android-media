package com.tokopedia.topads.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.topads.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.model.CreateGroupAdsViewModel
import com.tokopedia.topads.view.sheet.InfoSheetGroupList
import kotlinx.android.synthetic.main.topads_create_fragment_group_list.*
import javax.inject.Inject
import com.tokopedia.topads.data.response.ResponseGroupValidateName.TopAdsGroupValidateName

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
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateGroupAdsViewModel::class.java)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
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
        btn_submit.isEnabled = false
        btn_submit.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            InfoSheetGroupList.newInstance(it.context).show()
        }
        group_name_input.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when(actionId){
                    EditorInfo.IME_ACTION_SEARCH -> validateGroup(v?.text.toString())
                }
                Utils.dismissKeyboard(context, v)
                return true
            }
        })
    }

    private fun validateGroup(s: String?) {
        s?.let {
            if (s.length > 0) {
                viewModel.validateGroup(it, this::onSuccess, this::onError)
            }
        }
    }

    private fun onError(t: Throwable) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, t.localizedMessage, object : NetworkErrorHelper.RetryClickedListener{
            override fun onRetryClicked() {
                btn_submit.isEnabled = false
                validateGroup(group_name_input.text.toString())
            }
        })
    }

    private fun onSuccess(data: TopAdsGroupValidateName.Data) {
        btn_submit.isEnabled = true
    }

}