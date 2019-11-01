package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import kotlinx.android.synthetic.main.topads_create_fragment_keyword_list.*

/**
 * Author errysuprayogi on 29,October,2019
 */
class CreateManualAdsKeywordAdsListFragment: CreateManualAdsBaseStepperFragment<CreateManualAdsStepperModel>() {

    companion object {
        fun createInstance(): Fragment {

            val fragment = CreateManualAdsKeywordAdsListFragment()
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
    }

    override fun getScreenName(): String {
        return CreateManualAdsCreateGroupAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_keyword_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener {
            gotoNextPage()
        }
    }
}