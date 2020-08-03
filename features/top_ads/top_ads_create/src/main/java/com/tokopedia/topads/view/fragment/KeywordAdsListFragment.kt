package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.topads.Utils
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.activity.StepperActivity
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.topads.view.sheet.TipSheetKeywordList
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_PILIH_KEYWORD = "click-pilih keyword"
private const val CLICK_TAMBAH_KEYWORD = "click-tambah keyword"
private const val CLICK_TIPS_KEYWORD = "click-tips memilih kata kunci"
class KeywordAdsListFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private val keywordList = HashSet<String>()

    companion object {
        private const val COACH_MARK_TAG = "keyword"
        private const val NOT_KNOWN = "Tidak diketahui"
        fun createInstance(): Fragment {

            val fragment = KeywordAdsListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(KeywordAdsViewModel::class.java)
            it.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        keywordListAdapter = KeywordListAdapter(KeywordListAdapterTypeFactoryImpl(this::onKeywordSelected))

    }

    private fun startShowCase() {
        val coachMark = CoachMarkBuilder().build()
        if (!coachMark.hasShown(activity, COACH_MARK_TAG)) {
            coachitem_title.visibility = View.VISIBLE
            var coachItems = ArrayList<CoachMarkItem>()
            coachItems.add(CoachMarkItem(search_bar, getString(R.string.coach_mark_title_1), getString(R.string.coach_mark_desc_1)))
            coachItems.add(CoachMarkItem(coachitem_title, getString(R.string.coach_mark_title_2), getString(R.string.coach_mark_desc_2)))
            coachMark.show(activity, COACH_MARK_TAG, coachItems)
        } else {
            coachitem_title.visibility = View.GONE
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val list = stepperModel?.selectedProductIds!!
        val productId = list.joinToString(",")
        viewModel.getSugestionKeyword(productId, 0, this::onSuccessSuggestion, this::onErrorSuggestion, this::onEmptySuggestion)
    }

    private fun onKeywordSelected(pos: Int) {
        coachitem_title.visibility = View.GONE
        showSelectMessage()
        if (pos != -1 && keywordListAdapter.items[pos] is KeywordItemViewModel) {
            if ((keywordListAdapter.items[pos] as KeywordItemViewModel).data.totalSearch != "Tidak diketahui") {
                keywordListAdapter.setSelectedKeywords(getFavouredData())
            }
        }
    }

    private fun showSelectMessage() {
        var count = keywordListAdapter.getSelectedItems().size
        selected_info.text = String.format(getString(R.string.format_selected_keyword), count)
        if (count >= 50) {
            btn_next.isEnabled = false
            error_text.visibility = View.VISIBLE
            error_text.text = getString(R.string.error_max_selected_keyword)
        } else {
            btn_next.isEnabled = true
            error_text.visibility = View.INVISIBLE
        }
    }

    private fun onSuccessSuggestion(keywords: List<ResponseKeywordSuggestion.KeywordData>) {
        startShowCase()
        keywordListAdapter.items.add(KeywordGroupViewModel("Rekomendasi"))
        coachitem_title.visibility = View.GONE
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()
        keywords.forEach { key ->
            key.keywordData.forEach {index->
                keywordListAdapter.items.add(KeywordItemViewModel(index))
                keywordList.add(KeywordItemViewModel(index).data.keyword)
            }
        }
        tip_btn.visibility = View.VISIBLE
        addManualKeywords()
        keywordListAdapter.notifyDataSetChanged()
        showSelectMessage()
    }

    private fun addManualKeywords() {
        stepperModel?.manualKeywords!!.forEach {
            keywordListAdapter.addNewKeyword(viewModel.addNewKeyword(it))
        }
        keywordListAdapter.setSelectedList(stepperModel?.selectedKeywords!!)

        if (stepperModel?.selectedKeywords?.size != 0)
            keywordListAdapter.setSelectedKeywords(getFavouredData())

    }

    private fun onErrorSuggestion(throwable: Throwable) {
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()

    }

    private fun onEmptySuggestion() {
        coachitem_title.visibility = View.GONE
        keywordListAdapter.items = mutableListOf(KeywordEmptyViewModel())
        tip_btn.visibility = View.INVISIBLE
        addManualKeywordsEmpty()
        showSelectMessage()
    }

    private fun addManualKeywordsEmpty() {
        val list = mutableListOf<KeywordItemViewModel>()
        stepperModel?.manualKeywords!!.forEach {
            list.add(viewModel.addNewKeyword(it))
        }
        if (stepperModel?.manualKeywords?.size != 0) {
            tip_btn.visibility = View.VISIBLE
            keywordListAdapter.addManual(list)
        }
        keywordListAdapter.setSelectedList(stepperModel?.selectedKeywords!!)
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperModel?.selectedKeywords = getSelectedKeyword()
        stepperModel?.selectedSuggestBid = getSelectedBid()
        stepperModel?.manualKeywords = getManualKeywords()
        stepperListener?.goToNextPage(stepperModel)
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_PILIH_KEYWORD, getSelectedKeyword().joinToString("::"))
    }

    private fun getSelectedKeyword(): MutableList<String> {
        var list = mutableListOf<String>()
        keywordListAdapter.getSelectedItems().forEach {
            list.add(it.data.keyword)
        }
        return list
    }

    private fun getSelectedBid(): MutableList<Int> {
        var list = mutableListOf<Int>()
        keywordListAdapter.getSelectedItems().forEach {
            list.add(it.data.bidSuggest)
        }
        return list
    }

    private fun getManualKeywords(): MutableList<String> {
        var list = mutableListOf<String>()
        keywordListAdapter.manualKeywords.forEach {
            list.add(it.data.keyword)
        }
        return list
    }

    private fun getFavouredData(): List<KeywordItemViewModel> {
        val manual = mutableListOf<KeywordItemViewModel>()
        val selected = mutableListOf<KeywordItemViewModel>()
        selected.clear()
        selected.addAll(keywordListAdapter.getSelectedItems())
        manual.addAll(keywordListAdapter.manualKeywords)
        manual.addAll(selected)
        selected.forEach { index ->
            if (index is KeywordItemViewModel) {
                if (index.data.totalSearch == NOT_KNOWN) {
                    manual.remove(index)

                }
            }
        }
        return manual
    }

    override fun populateView() {
        if (activity is StepperActivity)
            (activity as StepperActivity).updateToolbarTitle(getString(R.string.keyword_list_step))
    }

    override fun getScreenName(): String {
        return KeywordAdsListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_layout_keyword_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn.isEnabled = false
        add_btn.setOnClickListener {
            coachitem_title.visibility = View.GONE
            keywordValidation(editText.text.toString())
        }
        btn_next.setOnClickListener { gotoNextPage() }
        tip_btn.setOnClickListener {
            TipSheetKeywordList.newInstance(view.context).show()
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TIPS_KEYWORD, "")
        }
        keyword_list.adapter = keywordListAdapter
        keyword_list.layoutManager = LinearLayoutManager(context)
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var text = validateKeyword(s)

                if (s.toString().trim().isEmpty()) {
                    add_btn.isEnabled = false
                } else if (!text.isNullOrBlank()) {
                    setValues(false)
                    error_text.text = text
                } else {
                    setValues(true)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //  add_btn.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                keywordValidation(editText.text.toString().trim())
                Utils.dismissKeyboard(context, v)

            }
            true
        }
    }

     private fun keywordValidation(key: String) {
         tip_btn.visibility = View.VISIBLE
         if(key.isNotEmpty()) {
            val alreadyExists: Boolean = keywordListAdapter.addNewKeyword(viewModel.addNewKeyword(key))
            showSelectMessage()
            if (alreadyExists) {
                makeToast(getString(R.string.keyword_already_exists))
            }
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_TAMBAH_KEYWORD, key)
        }
    }

    private fun setValues(flag: Boolean) {
        if (flag) {
            add_btn.isEnabled = true
            editText.imeOptions = EditorInfo.IME_ACTION_NEXT
            error_text.visibility = View.INVISIBLE

        } else {
            add_btn.isEnabled = false
            editText.imeOptions = EditorInfo.IME_ACTION_NONE
            error_text.visibility = View.VISIBLE
        }
    }

    private fun makeToast(s: String) {
        SnackbarManager.make(activity, s,
                Snackbar.LENGTH_LONG)
                .show()
    }

    private fun validateKeyword(text: CharSequence?): CharSequence? {
        return if (!text.isNullOrBlank() && text.split(" ").size > 5) {
            getString(R.string.error_max_length_keyword)
        } else if (!text.isNullOrBlank() && !text.matches("^[A-Za-z0-9 ]*$".toRegex())) {
            getString(R.string.error_keyword)
        } else if (text!!.length > 50) {
            getString(R.string.error_max_length)
        } else {
            null
        }
    }
}