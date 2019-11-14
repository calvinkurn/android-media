package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseGroupValidate
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.model.CreateGroupAdsViewModel
import com.tokopedia.topads.view.sheet.InfoSheetGroupList
import kotlinx.android.synthetic.main.topads_create_fragment_group_list.*
import javax.inject.Inject

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
        btn_submit.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            InfoSheetGroupList.newInstance(it.context).show()
        }
        group_name_input.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                validateGroup(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
        })
    }

    private fun validateGroup(s: Editable?) {
        s?.toString()?.let {
            if (s.length > 0) {
                viewModel.validateGroup(it, this::onSuccess, this::onError)
            }
        }
    }

    private fun onError(t: Throwable) {
        t.printStackTrace()
    }

    private fun onSuccess(data: ResponseGroupValidate.Data) {

    }

}