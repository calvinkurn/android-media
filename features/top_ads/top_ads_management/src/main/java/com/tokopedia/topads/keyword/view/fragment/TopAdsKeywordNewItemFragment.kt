package com.tokopedia.topads.keyword.view.fragment

import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
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
import android.view.animation.LinearInterpolator
import com.tkpd.library.ui.expandablelayout.ExpandableLayoutListener
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.topads.R
import com.tokopedia.topads.dashboard.di.component.TopAdsComponent
import com.tokopedia.topads.keyword.domain.model.keywordadd.AddKeywordDomainModelDatum
import com.tokopedia.topads.keyword.helper.KeywordTypeMapper
import com.tokopedia.topads.keyword.view.activity.TopAdsKeywordNewItemActivity
import com.tokopedia.topads.keyword.view.model.TopAdsKeywordNewStepperModel
import kotlinx.android.synthetic.main.fragment_top_ads_keyword_new_item.*
import java.util.*

class TopAdsKeywordNewItemFragment: BaseDaggerFragment() {
    val localKeywords: MutableList<AddKeywordDomainModelDatum> = mutableListOf()
    var maxCount = 50
    lateinit var stepperModel: TopAdsKeywordNewStepperModel
    private var selectedType = 0
    private var isValid = false
    val userSession: UserSession by lazy {
        getComponent(TopAdsComponent::class.java).userSession()
    }

    companion object {
        val ADDED_KEYWORDS_PARAM = "added_keywords"
        private const val MIN_WORDS = 5

        fun newInstance(localKeywords:List<AddKeywordDomainModelDatum>, maxCount: Int,
                        stepperModel: TopAdsKeywordNewStepperModel): Fragment {
            return TopAdsKeywordNewItemFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(TopAdsKeywordNewItemActivity.LOCAL_KEYWORDS_PARAM, ArrayList(localKeywords))
                    putInt(TopAdsKeywordNewItemActivity.MAX_COUNT_PARAM, maxCount)
                    putParcelable(TopAdsKeywordNewItemActivity.STEPPERMODEL_PARAM, stepperModel)
                }
            }
        }
    }

    override fun initInjector() {

    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null && arguments != null){
            localKeywords.clear()
            localKeywords.addAll(arguments.getParcelableArrayList(TopAdsKeywordNewItemActivity.LOCAL_KEYWORDS_PARAM))
            maxCount = arguments.getInt(TopAdsKeywordNewItemActivity.MAX_COUNT_PARAM, 50)
            stepperModel = arguments.getParcelable(TopAdsKeywordNewItemActivity.STEPPERMODEL_PARAM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_top_ads_keyword_new_item, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalKeyAdded.text = getString(R.string.top_ads_keywords_total_current_and_max, localKeywords.size, maxCount)
        textKeywordExample.text = MethodChecker.fromHtml(getString(R.string.topads_keyword_search_example_value_phrase))
        setupTextArea()
        setupToggleKeywordTypeDesc()
        setupRadioKeywordType()
        checkAddButtonEnabled()

        buttonSave.setOnClickListener {
            val keywordsString = editTextDescription.text.toString().split("\n")
            if (keywordsString.size + localKeywords.size > maxCount){
                textInputLayoutDescription.error = getString(R.string.top_ads_keyword_per_group_reach_limit)
                return@setOnClickListener
            }
            val keywordAdded = keywordsString.filter { !TextUtils.isEmpty(it) }.map {
                convertToAddKeywordDomainModelDatum(it)
            }

            val intent = Intent().apply {
                putParcelableArrayListExtra(ADDED_KEYWORDS_PARAM, ArrayList(keywordAdded))
            }
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }
    }

    private fun convertToAddKeywordDomainModelDatum(keyword: String): AddKeywordDomainModelDatum{
        return AddKeywordDomainModelDatum(keyword, KeywordTypeMapper.mapToDef(stepperModel.isPositive, selectedType),
                stepperModel.groupId, userSession.shopId)
    }

    private fun setupTextArea(){
        editTextDescription.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable?) {
                var text = editable.toString().split("\n")
                        .filter { !TextUtils.isEmpty(it) }

                if (!isHasInvalidChar(text)){
                    text = text.map { StringUtils.omitPunctuationAndDoubleSpace(it).toLowerCase() }
                            .filter { !TextUtils.isEmpty(it) }
                    if (text.size > 0 && isValidateWordAndCheckLocalSuccess(text)){
                        textInputLayoutDescription.setErrorEnabled(false)
                        isValid = true
                    } else {
                        isValid = false
                    }
                } else {
                    isValid = false
                    textInputLayoutDescription.error = getString(R.string.top_ads_keyword_must_only_letter_or_digit)
                }

                val currentCount = text.size + localKeywords.size
                totalKeyAdded.text = getString(R.string.top_ads_keywords_total_current_and_max, currentCount, maxCount)
                checkAddButtonEnabled()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun isValidateWordAndCheckLocalSuccess(keywords: List<String>): Boolean {
        if (keywords.find { hasInValidWordLength(it) }  != null ){
            textInputLayoutDescription.error = getString(R.string.top_ads_keyword_cannot_more_than_5)
            return false
        } else if (isDuplicateExists(keywords)){
            textInputLayoutDescription.error = getString(R.string.top_ads_keyword_has_existed)
            return false
        } else {
            return true
        }
    }

    private fun isDuplicateExists(keywords: List<String>): Boolean {
        val filteredLocal = localKeywords
                .filter { it.keyWordTypeId ==  KeywordTypeMapper.mapToDef(stepperModel.isPositive, selectedType)}
                .map { it.keywordTag }
        val tmpSet = keywords.toSet()

        return (keywords.size != tmpSet.size) || !Collections.disjoint(filteredLocal, tmpSet)
    }

    private fun hasInValidWordLength(keyword: String): Boolean {
        val words = keyword.split("\\s|\\n".toRegex(), MIN_WORDS + 1).toTypedArray()
        if (words.size > MIN_WORDS) {
            return true
        }
        return false
    }

    private fun isHasInvalidChar(keywords: List<String>): Boolean {
        return keywords.find { StringUtils.containNonSpaceAlphaNumeric(it) } != null
    }

    private fun setupToggleKeywordTypeDesc(){
        seeDetailTextView.setOnClickListener {
            seeDetailToggleDesc.toggle()
        }
        seeDetailToggleIcon.rotation = 0f
        seeDetailToggleDesc.apply {
            isExpanded = false
            setListener(object : ExpandableLayoutListener {
                override fun onAnimationEnd() {}

                override fun onOpened() {}

                override fun onAnimationStart() {
                    ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.bottom)
                            .setDuration(400).start()
                }

                override fun onPreOpen() {
                    createRotateAnimator(seeDetailToggleIcon, 0f, 180f).start()
                }

                override fun onClosed() {}

                override fun onPreClose() {
                    createRotateAnimator(seeDetailToggleIcon, 180f, 0f).start()
                }
            })
        }
    }

    private fun createRotateAnimator(view: View, from: Float, to:Float): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, "rotation", from, to).apply {
            duration = 300
            interpolator = LinearInterpolator()
        }
    }

    private fun setupRadioKeywordType(){
        keywordRadioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            if (checkedId == R.id.keywordPhraseRadioButton){
                textKeywordExample.text = MethodChecker.fromHtml(getString(R.string.topads_keyword_search_example_value_phrase))
                keywordTypeDesc.text = getString(R.string.top_ads_see_detail_info_keyword_phrase)
                selectedType = 0
            } else {
                textKeywordExample.text = MethodChecker.fromHtml(getString(R.string.topads_keyword_search_example_value_exact))
                keywordTypeDesc.text = getString(R.string.top_ads_see_detail_info_keyword_match)
                selectedType = 1
            }
        }
    }

    private fun checkAddButtonEnabled(){
        if (!isValid && buttonSave.isEnabled){
            buttonSave.isEnabled = false
        } else if (isValid && !buttonSave.isEnabled){
            buttonSave.isEnabled = true
        }
    }

}