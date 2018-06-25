package com.tokopedia.topads.keyword.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.R
import com.tokopedia.topads.common.view.fragment.TopAdsNewBaseStepperFragment
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel
import kotlinx.android.synthetic.main.fragment_top_ads_keyword_new_add.*

class TopAdsKeywordNewAddFragment : TopAdsNewBaseStepperFragment<TopAdsKeywordNewStepperModel>(){
    private var maxKeyword = MAX_KEYWORD
    private var serverCount = 0

    companion object {
        val TAG = TopAdsKeywordNewAddFragment::class.java.simpleName
        private val MAX_KEYWORD = 50

        fun newInstance(): Fragment {
            return TopAdsKeywordNewAddFragment()
        }
    }


    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: TopAdsKeywordNewStepperModel()
    }

    override fun saveStepperModel(stepperModel: TopAdsKeywordNewStepperModel) {
    }

    override fun gotoNextPage() {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun populateView(stepperModel: TopAdsKeywordNewStepperModel) {
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun initInjector() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initiateStepperModel()
        stepperModel?.run {
            maxKeyword = maxWords
            this@TopAdsKeywordNewAddFragment.serverCount = serverCount
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_top_ads_keyword_new_add, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSave.setOnClickListener { onButtonSaveClicked() }
        buttonSave.isEnabled = false
        setCurrentGroup()
        setCurrentMaxKeyword()
        setServerKeyword()
    }

    private fun onButtonSaveClicked(){

    }

    private fun setCurrentGroup(){
        keywordCurrentLabelView.run {
            title = getString(R.string.keyword_group_label_view,stepperModel?.groupName ?: "")
            setSubTitle(getString(R.string.top_ads_keyword_total_keyword_in_group_2, serverCount))
        }
    }

    private fun setCurrentMaxKeyword(){
        keywordGroupLabelView.title = getString(R.string.top_ads_keywords_total_current_and_max,
                getLocalKeyWordSize(), maxKeyword)
    }

    private fun getLocalKeyWordSize(): Int {
        return keywordRecyclerView.keywordList.size
    }

    private fun setServerKeyword(){

    }

}