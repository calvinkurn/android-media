package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.sheet.InfoSheetBudgetList
import com.tokopedia.topads.view.sheet.TipSheetBudgetList
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.*
import kotlinx.android.synthetic.main.topads_create_fragment_budget_list.tip_btn

/**
 * Author errysuprayogi on 29,October,2019
 */
class BudgetingAdsFragment: BaseStepperFragment<CreateManualAdsStepperModel>() {

    companion object {
        val TAG = BudgetingAdsFragment::class.simpleName
        fun createInstance(): Fragment {

            val fragment = BudgetingAdsFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView(stepperModel: CreateManualAdsStepperModel) {
        Log.d(TAG, stepperModel.toString())
    }

    override fun getScreenName(): String {
        return CreateGroupAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_budget_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        tip_btn.setOnClickListener {
            TipSheetBudgetList.newInstance(it.context).show()
        }
        btn_info.setOnClickListener {
            InfoSheetBudgetList.newInstance(it.context).show()
        }
    }
}