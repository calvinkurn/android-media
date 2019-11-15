package com.tokopedia.topads.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.CreateManualAdsStepperModel
import com.tokopedia.topads.data.response.ResponseKeywordSuggestion
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.view.adapter.keyword.KeywordListAdapterTypeFactoryImpl
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordGroupViewModel
import com.tokopedia.topads.view.adapter.keyword.viewmodel.KeywordItemViewModel
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import kotlinx.android.synthetic.main.topads_create_fragment_keyword_list.*
import kotlinx.android.synthetic.main.topads_create_fragment_keyword_list.btn_next
import javax.inject.Inject

/**
 * Author errysuprayogi on 29,October,2019
 */
class KeywordAdsListFragment: BaseStepperFragment<CreateManualAdsStepperModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var keywordListAdapter: KeywordListAdapter

    companion object {
        fun createInstance(): Fragment {

            val fragment = KeywordAdsListFragment()
            val args = Bundle()
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(KeywordAdsViewModel::class.java)
        keywordListAdapter = KeywordListAdapter(KeywordListAdapterTypeFactoryImpl(this::onKeywordSelected))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getSugestionKeyword("293178758,293182872", 100, this::onSuccessSuggestion, this::onErrorSuggestion, this::onEmptySuggestion)

        //Dummy data
        keywordListAdapter.items = mutableListOf(
                KeywordGroupViewModel("Kata Kunci Pilihan"),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Katun", 2399848)),
                KeywordGroupViewModel("Rekomendasi"),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Katun", 2399848)),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja K", 2399848)),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Kerja", 2399848)),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Katun", 2399848)),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Kantor", 2399848)),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Katun", 2399848)),
                KeywordItemViewModel(ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data(0,"Kemeja Katun", 2399848))
                )
        keywordListAdapter.notifyDataSetChanged()
    }

    private fun onKeywordSelected() {
        selected_info.setText(String.format(getString(R.string.format_selected_keyword), keywordListAdapter.getSelectedItems().size))
    }

    private fun onSuccessSuggestion(keywords: List<ResponseKeywordSuggestion.TopAdsGetKeywordSuggestion.Data>){

    }

    private fun onErrorSuggestion(throwable: Throwable){

    }

    private fun onEmptySuggestion(){

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
        return CreateGroupAdsFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CreateAdsComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.topads_create_fragment_keyword_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_next.setOnClickListener {
            gotoNextPage()
        }
        keyword_list.adapter = keywordListAdapter
        keyword_list.layoutManager = LinearLayoutManager(context)
    }
}