package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
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
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordViewModel
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
    val keywordList = HashSet<String>()

    companion object {
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var list = stepperModel?.selectedProductIds!!
        var _list = list.toString()
        var product_id =_list.substring(1, _list.length- 1)
        viewModel.getSugestionKeyword(product_id,0,  this::onSuccessSuggestion, this::onErrorSuggestion, this::onEmptySuggestion)
    }



    private fun onKeywordSelected() {
        var count = keywordListAdapter.getSelectedItems().size
        selected_info.setText(String.format(getString(R.string.format_selected_keyword), count))
        if (count >= 50) {
            btn_next.isEnabled = false
            error_text.visibility = View.VISIBLE
            error_text.text = getString(R.string.error_max_selected_keyword)
        } else{
            btn_next.isEnabled = true
            error_text.visibility = View.INVISIBLE
        }
        keywordListAdapter.addNewKeywords(getSelectedData())

    }

    private fun onSuccessSuggestion(keywords: List<ResponseKeywordSuggestion.Result.TopAdsGetKeywordSuggestion.Data>) {
        keywordListAdapter.items.add (KeywordGroupViewModel("Rekomendasi"))
        keywordListAdapter.favoured.clear()
        keywordListAdapter.remains.clear()
        keywords.forEach {
            index-> keywordListAdapter.items.add(KeywordItemViewModel(index))
            keywordList.add(KeywordItemViewModel(index).data.keyword)
        }
        tip_btn.visibility = View.VISIBLE
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun onErrorSuggestion(throwable: Throwable) {

    }

    private fun onEmptySuggestion() {
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
        stepperListener?.goToNextPage(stepperModel)
    }

    private fun getSelectedKeyword(): MutableList<String> {
        var list = mutableListOf<String>()
        keywordListAdapter.getSelectedItems().forEach {
            list.add((it as KeywordItemViewModel).data.keyword)
        }
        return list
    }

    private fun getSelectedData():List<KeywordItemViewModel>{
        return keywordListAdapter.getSelectedItems()
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
            var bool : Boolean
           bool= keywordListAdapter.addNewKeyword( viewModel.addNewKeyword(editText.text.toString()))
           if(bool){
               SnackbarManager.make(activity,
                       "already added",
                       Snackbar.LENGTH_LONG)
                       .show()
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
                    error_text.visibility = View.VISIBLE
                    error_text.text = text
                } else {
                    error_text.visibility = View.INVISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                add_btn.isEnabled = !s.isNullOrBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        editText.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when(actionId){
                    EditorInfo.IME_ACTION_DONE -> viewModel.addNewKeyword(editText.text.toString())
                }
                Utils.dismissKeyboard(context, v)
                return true
            }
        })
    }

    private fun validateKeyword(text: CharSequence?): CharSequence? {
        if (!text.isNullOrBlank() && text.split(" ").size > 5) {
            return getString(R.string.error_max_length_keyword)
        } else if (!text.isNullOrBlank() && !text.matches("^[A-Za-z0-9 ]*$".toRegex())) {
            return getString(R.string.error_keyword)
        } else {
            return null
        }
    }

    override fun onDestroy() {
   //     viewModel?.selectedKeywordList?.removeObservers(viewLifecycleOwner)
        super.onDestroy()
    }


}