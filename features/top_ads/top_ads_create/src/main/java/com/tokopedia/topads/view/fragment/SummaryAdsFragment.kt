package com.tokopedia.topads.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.view.activity.SuccessActivity
import kotlinx.android.synthetic.main.topads_create_fragment_summary.*

/**
 * Author errysuprayogi on 29,October,2019
 */
class SummaryAdsFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    companion object {
        fun createInstance(): Fragment {

            val fragment = SummaryAdsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            if (true) {
                var intent: Intent = Intent(context, SuccessActivity::class.java)
                startActivity(intent)

            }

            //TODO check credits and redirect

//            startActivity(RouteManager.getIntent(context, ApplinkConstInternalTopAds.TOPADS_BUY_CREDIT))
//            activity!!.finish()
//
//            RouteManager.route(context, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL)

        }
        toggle.setOnClickListener {
            if (toggle.isChecked) {
                daily_budget.visibility = View.VISIBLE
            } else
                daily_budget.visibility = View.GONE
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        product_count.text = stepperModel!!.selectedProductIds!!.count().toString()
        keyword_count.text = stepperModel!!.selectedKeywords!!.count().toString()
        group_name.text = stepperModel!!.groupName

    }
}