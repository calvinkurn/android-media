package com.tokopedia.topads.keyword.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.TopAdsComponentInstance
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewItemFragment
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel

class TopAdsKeywordNewItemActivity : BaseSimpleActivity(), HasComponent<TopAdsComponent> {
    private var currentCount = 0
    private var maxCount = 50
    private var stepperModel: TopAdsKeywordNewStepperModel? = null

    companion object {
        val CURRENT_COUNT_PARAM = "current_count"
        val MAX_COUNT_PARAM = "max_count"
        val STEPPERMODEL_PARAM = "steppermodel"

        fun startForResult(activity: Activity, requestCode: Int, currentCount: Int,
                           maxCount: Int, stepperModel: TopAdsKeywordNewStepperModel?, fragment: Fragment? = null){
            val intent = createIntent(activity, currentCount, maxCount, stepperModel)
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode)
            } else {
                activity.startActivityForResult(intent, requestCode)
            }
        }

        fun createIntent(activity: Activity, currentCount: Int, maxCount: Int,
                         stepperModel: TopAdsKeywordNewStepperModel?): Intent {
            val intent = Intent(activity, TopAdsKeywordNewItemActivity::class.java)
            return intent.apply {
                putExtra(CURRENT_COUNT_PARAM, currentCount)
                putExtra(MAX_COUNT_PARAM, maxCount)
                putExtra(STEPPERMODEL_PARAM, stepperModel)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        iniateStepperModel()
        intent?.extras?.run {
            currentCount = getInt(CURRENT_COUNT_PARAM, 0)
            maxCount = getInt(MAX_COUNT_PARAM, 50)
            stepperModel = getParcelable(STEPPERMODEL_PARAM)
        }
        super.onCreate(savedInstanceState)
    }

    private fun iniateStepperModel(){
        stepperModel = stepperModel ?: TopAdsKeywordNewStepperModel()
    }

    override fun getNewFragment(): Fragment {
        return TopAdsKeywordNewItemFragment.newInstance(currentCount, maxCount, stepperModel!!)
    }

    override fun getComponent(): TopAdsComponent {
        return TopAdsComponentInstance.getComponent(application)
    }

}