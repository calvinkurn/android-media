package com.tokopedia.topads.keyword.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.TopAdsComponentInstance
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.view.fragment.TopAdsKeywordNewItemFragment
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel

class TopAdsKeywordNewItemActivity : BaseSimpleActivity(), HasComponent<TopAdsComponent> {
    private val localKeywords: MutableList<AddKeywordDomainModelDatum> = mutableListOf()
    private var maxCount = 50
    private var stepperModel: TopAdsKeywordNewStepperModel? = null

    companion object {
        const val MAX_COUNT_PARAM = "max_count"
        const val STEPPERMODEL_PARAM = "steppermodel"
        const val LOCAL_KEYWORDS_PARAM = "local_keywords"

        fun startForResult(activity: Activity, requestCode: Int, localKeywords: List<AddKeywordDomainModelDatum>,
                           maxCount: Int, stepperModel: TopAdsKeywordNewStepperModel?, fragment: Fragment? = null){
            val intent = createIntent(activity, localKeywords, maxCount, stepperModel)
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode)
            } else {
                activity.startActivityForResult(intent, requestCode)
            }
        }

        fun createIntent(activity: Activity, localKeywords: List<AddKeywordDomainModelDatum>, maxCount: Int,
                         stepperModel: TopAdsKeywordNewStepperModel?): Intent {
            val intent = Intent(activity, TopAdsKeywordNewItemActivity::class.java)
            return intent.apply {
                putParcelableArrayListExtra(LOCAL_KEYWORDS_PARAM, ArrayList(localKeywords))
                putExtra(MAX_COUNT_PARAM, maxCount)
                putExtra(STEPPERMODEL_PARAM, stepperModel)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        iniateStepperModel()
        intent?.extras?.run {
            localKeywords.clear()
            localKeywords.addAll(getParcelableArrayList(LOCAL_KEYWORDS_PARAM))
            maxCount = getInt(MAX_COUNT_PARAM, 50)
            stepperModel = getParcelable(STEPPERMODEL_PARAM)
        }
        super.onCreate(savedInstanceState)
    }

    private fun iniateStepperModel(){
        stepperModel = stepperModel ?: TopAdsKeywordNewStepperModel()
    }

    override fun getNewFragment(): Fragment {
        return TopAdsKeywordNewItemFragment.newInstance(localKeywords, maxCount, stepperModel?.isPositive ?: true, stepperModel?.groupId ?: "")
    }

    override fun getComponent(): TopAdsComponent {
        return TopAdsComponentInstance.getComponent(application)
    }

}