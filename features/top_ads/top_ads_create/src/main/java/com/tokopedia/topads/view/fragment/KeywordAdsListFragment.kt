package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.coachmark.CoachMarkBuilder
import com.tokopedia.coachmark.CoachMarkItem
import com.tokopedia.topads.Utils
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordEmptyViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.topads.view.sheet.TipSheetKeywordList
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list.*
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list.btn_next
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */
class KeywordAdsListFragment : BaseStepperFragment<CreateManualAdsStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter
    private val keywordList = HashSet<String>()

    companion object {
        private const val COACH_MARK_TAG = "keyword"
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
        var list = stepperModel?.selectedProductIds!!
        var s = list.toString()
        var productId = s.substring(1, s.length - 1)
        viewModel.getSugestionKeyword(productId, 0, this::onSuccessSuggestion, this::onErrorSuggestion, this::onEmptySuggestion)
    }


    private fun onKeywordSelected(pos: Int) {
        coachitem_title.visibility = View.GONE
        showErrorMessage()
        if (pos != -1 && keywordListAdapter.items[pos] is KeywordItemViewModel) {
            if ((keywordListAdapter.items[pos] as KeywordItemViewModel).data.totalSearch == "Tidak diketahui") {

            } else
                keywordListAdapter.setSelectedKeywords(getFavouredData())

        }

    }

    private fun showErrorMessage() {
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

    private fun onSuccessSuggestion(keywords: List<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>) {
        startShowCase()
        keywordListAdapter.items.add(KeywordGroupViewModel("Rekomendasi"))
        coachitem_title.visibility = View.GONE
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()
        keywords.forEach { index ->
            keywordListAdapter.items.add(KeywordItemViewModel(index))
            keywordList.add(KeywordItemViewModel(index).data.keyword)
        }
        tip_btn.visibility = View.VISIBLE
        keywordListAdapter.notifyDataSetChanged()

    }

    private fun onErrorSuggestion(throwable: Throwable) {
        keywordListAdapter.favoured.clear()
        keywordListAdapter.manualKeywords.clear()

    }

    override fun onResume() {
        super.onResume()
    }

    private fun onEmptySuggestion() {
        coachitem_title.visibility = View.GONE
        keywordListAdapter.items = mutableListOf(KeywordEmptyViewModel())
        keywordListAdapter.notifyDataSetChanged()
        tip_btn.visibility = View.INVISIBLE
    }

    override fun initiateStepperModel() {
        stepperModel = stepperModel ?: CreateManualAdsStepperModel()
    }

    override fun saveStepperModel(stepperModel: CreateManualAdsStepperModel) {}

    override fun gotoNextPage() {
        stepperModel?.selectedKeywords = getSelectedKeyword()
        stepperModel?.selectedSuggestBid = getSelectedBid()
        stepperListener?.goToNextPage(stepperModel)
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

    private fun getSelectedData(): List<KeywordItemViewModel> {
        return keywordListAdapter.getSelectedItems()
    }

    private fun getFavouredData(): List<KeywordItemViewModel> {
        val list = mutableListOf<KeywordItemViewModel>()
        val list1 = mutableListOf<KeywordItemViewModel>()
        list1.clear()
        list1.addAll(keywordListAdapter.getSelectedItems())
        list.addAll(keywordListAdapter.manualKeywords)
        list.addAll(list1)
        list1.forEach { index ->
            if (index is KeywordItemViewModel) {
                if (index.data.totalSearch == "Tidak diketahui") {
                    list.remove(index)

                }
            }
        }
        return list
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
        return inflater.inflate(R.layout.topads_create_layout_keyword_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_btn.isEnabled = false
        add_btn.setOnClickListener {
            coachitem_title.visibility = View.GONE
            var alreadyExists: Boolean = keywordListAdapter.addNewKeyword(viewModel.addNewKeyword(editText.text.toString()))
            showErrorMessage()
            if (alreadyExists) {
                makeToast()
            }
        }
        btn_next.setOnClickListener { gotoNextPage() }
        tip_btn.setOnClickListener { TipSheetKeywordList.newInstance(view.context).show() }
        keyword_list.adapter = keywordListAdapter
        keyword_list.layoutManager = LinearLayoutManager(context)
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var text = validateKeyword(s)
                if (!text.isNullOrBlank()) {
                    add_btn.isEnabled = false
                    error_text.visibility = View.VISIBLE
                    error_text.text = text
                } else {
                    add_btn.isEnabled = true
                    error_text.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
              //  add_btn.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        editText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    var alreadyExists: Boolean = keywordListAdapter.addNewKeyword(viewModel.addNewKeyword(editText.text.toString()))
                    showErrorMessage()
                    if (alreadyExists) {
                        makeToast()
                    }
                }
                Utils.dismissKeyboard(context, v)
                return true
            }
        })
    }

    private fun makeToast() {
        SnackbarManager.make(activity,
                getString(R.string.keyword_already_exists),
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