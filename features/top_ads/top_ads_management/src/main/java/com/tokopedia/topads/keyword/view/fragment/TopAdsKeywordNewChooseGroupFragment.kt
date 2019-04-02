package com.tokopedia.topads.keyword.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tokopedia.core.util.MethodChecker
import com.tokopedia.topads.R
import com.tokopedia.topads.common.view.fragment.TopAdsNewBaseStepperFragment
import com.tokopedia.topads.dashboard.constant.TopAdsExtraConstant
import com.tokopedia.topads.dashboard.data.model.data.GroupAd
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.dashboard.view.adapter.TopAdsAutoCompleteAdapter
import com.tokopedia.topads.dashboard.utils.ViewUtils
import com.tokopedia.topads.keyword.di.component.DaggerTopAdsKeywordNewChooseGroupComponent
import com.tokopedia.topads.keyword.di.module.TopAdsKeywordNewChooseGroupModule
import com.tokopedia.topads.keyword.view.listener.TopAdsKeywordNewChooseGroupView
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordNewChooseGroupPresenter
import kotlinx.android.synthetic.main.fragment_top_ads_keyword_new_choose_group.*
import kotlinx.android.synthetic.main.partial_top_ads_search_group.*
import javax.inject.Inject

class TopAdsKeywordNewChooseGroupFragment : TopAdsNewBaseStepperFragment<TopAdsKeywordNewStepperModel>(),
    TopAdsKeywordNewChooseGroupView {

    companion object {
        val TAG = TopAdsKeywordNewChooseGroupFragment::class.java.simpleName
        val ADD_REQUEST_CODE = 100
        private val SAVED_GROUP_ID = "grp_id"
        private val SAVED_GROUP_NAME = "grp_nm"
        private val SAVED_KEYWORD_COUNT = "key_count"

        fun newInstance(): Fragment = TopAdsKeywordNewChooseGroupFragment()
    }

    private var chosenId = "";
    private var keywordCount = 0;
    private var isPositive = false
    private var groupId: String? = null
    private lateinit var adapterChooseGroup: TopAdsAutoCompleteAdapter
    private var groupAds: MutableList<GroupAd> = mutableListOf()
    private var groupNames: ArrayList<String> = arrayListOf()
    private var groupAd: GroupAd? = null

    @Inject
    lateinit var topAdsKeywordNewChooseGroupPresenter: TopAdsKeywordNewChooseGroupPresenter

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: TopAdsKeywordNewStepperModel()
    }

    override fun saveStepperModel(stepperModel: TopAdsKeywordNewStepperModel) {
        stepperModel.apply {
            groupId = chosenId
            choosenId = chosenId
            groupName = choose_group_auto_text.text?.toString()
            serverCount = keywordCount
            maxWords = getResources().getInteger(R.integer.top_ads_keyword_max_in_group) - keywordCount
            localWords = mutableListOf()
        }
    }

    override fun gotoNextPage() {
        if (keywordCount >= resources.getInteger(R.integer.top_ads_keyword_max_in_group)){
            Toast.makeText(activity, MethodChecker.fromHtml(getString(R.string.top_ads_keyword_per_group_reach_limit)), Toast.LENGTH_LONG).show()
            return
        }

        stepperListener?.goToNextPage(stepperModel)
    }

    override fun populateView(stepperModel: TopAdsKeywordNewStepperModel) {}

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerTopAdsKeywordNewChooseGroupComponent.builder()
                .topAdsKeywordNewChooseGroupModule(TopAdsKeywordNewChooseGroupModule())
                .topAdsComponent(getComponent(TopAdsComponent::class.java))
                .build()
                .inject(this)
        topAdsKeywordNewChooseGroupPresenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initiateStepperModel()
        super.onCreate(savedInstanceState)
        stepperModel?.let {
            this.isPositive = it.isPositive
            this.groupId = it.groupId
        }
        adapterChooseGroup = TopAdsAutoCompleteAdapter(activity, R.layout.item_autocomplete_text)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_ads_keyword_new_choose_group, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        choose_group_auto_text.setAdapter(adapterChooseGroup)

        var groupName: String? = null
        savedInstanceState?.run {
            chosenId = getString(SAVED_GROUP_ID)
            groupName = getString(SAVED_GROUP_NAME)
            keywordCount = getInt(SAVED_KEYWORD_COUNT)
        }

        if (TextUtils.isEmpty(chosenId)){
            input_layout_choose_group.setErrorSuccessEnabled(false)
        } else {
            choose_group_auto_text.run {
                setText(groupName)
                lockView()
                input_layout_choose_group.setSuccess(getString(R.string.top_ads_keywords_in_groups, keywordCount))
            }
        }

        run {
            if (isPositive) keywordPositiveRadioButton else keywordNegativeRadioButton
        }.setChecked(true)

        view?.requestFocus()

        setChooseGroupListener()
        setRadioGroupListener()
        setButtonNextListener()
        checkButtonNextEnabled()
    }

    private fun setChooseGroupListener(){
        choose_group_auto_text.setOnFocusChangeListener { view, isFocus ->
            if (isFocus){
                topAdsKeywordNewChooseGroupPresenter.searchGroupName("")
            }
        }

        choose_group_auto_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (!choose_group_auto_text.isEnabled || choose_group_auto_text.isPerformingCompletion){
                    return
                }
                input_layout_choose_group.hideErrorSuccess()
                buttonNext.isEnabled = false
                chosenId = ""
                topAdsKeywordNewChooseGroupPresenter.searchGroupName(editable?.toString() ?: "")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        choose_group_auto_text.setOnItemClickListener { _, _, i, _ ->
            choose_group_auto_text.lockView()
            groupAd = groupAds[i]
            groupAd?.run {
                chosenId = id
                findKeywordCount(this)
            }
            checkButtonNextEnabled()
        }

        groupId?.run {
            choose_group_auto_text.setText(this)
            choose_group_auto_text.lockView()
            topAdsKeywordNewChooseGroupPresenter.searchGroupName(this)
        }

        adapterChooseGroup.setListenerGetData { groupNames }
    }

    private fun findKeywordCount(groupAd: GroupAd){
        keywordCount = if (isPositive) groupAd.positiveCount else groupAd.negativeCount
        input_layout_choose_group.setSuccess(getString(R.string.top_ads_keywords_in_groups, keywordCount))
    }

    private fun setButtonNextListener(){
        buttonNext.setOnClickListener { onNextClicked() }
    }

    private fun checkButtonNextEnabled(){
        buttonNext.isEnabled = !TextUtils.isEmpty(chosenId)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            ADD_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK){
                    val intent = Intent().apply { putExtra(TopAdsExtraConstant.EXTRA_AD_CHANGED, true) }
                    activity?.run {
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                }
            }
            else -> return
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putString(SAVED_GROUP_ID, chosenId)
            putString(SAVED_GROUP_NAME, choose_group_auto_text.text?.toString() ?: "")
            putInt(SAVED_KEYWORD_COUNT, keywordCount)
        }
    }

    private fun setRadioGroupListener(){
        keywordRadioGroup.setOnCheckedChangeListener { radioGroup, isChecked, checkedId ->
            isPositive = checkedId == R.id.keywordPositiveRadioButton
            stepperModel?.apply {  isPositive = this@TopAdsKeywordNewChooseGroupFragment.isPositive }
            groupAd?.let { findKeywordCount(it) }
        }
    }

    override fun onGetGroupAdList(groupAds: List<GroupAd>) {
        this.groupAds.clear()
        this.groupAds.addAll(groupAds)
        groupNames.clear()
        groupAds.forEach {
            groupId?.run {
                if (it.name == this){
                    chosenId = it.id
                    findKeywordCount(it)
                    checkButtonNextEnabled()
                }
                groupId = null
            }
            groupNames.add(it.name)
        }

        choose_group_auto_text.showDropDownFilter()
    }

    override fun onGetGroupAdListError(e: Throwable?) {
        context?.let {
            input_layout_choose_group.error = ViewUtils.getGeneralErrorMessage(it, e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        topAdsKeywordNewChooseGroupPresenter.detachView()
    }
}