package com.tokopedia.top_ads_headline.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent
import com.tokopedia.top_ads_headline.view.viewmodel.HeadlineEditKeywordViewModel
import com.tokopedia.topads.common.constant.Constants
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.util.SpaceItemDecoration
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.adapter.keyword.KeywordListAdapter
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewholder.HeadlineEditEmptyAdKeywordViewHolder
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditAdKeywordModel
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.HeadlineEditEmptyAdKeywordModel
import com.tokopedia.topads.common.view.adapter.keyword.viewmodel.KeywordUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_headline_edit_keyword.*
import javax.inject.Inject


const val KEYWORD_POSITIVE = "keywordPositive"
const val KEYWORD_NEGATIVE = "keywordNegative"
const val KEYWORD_TYPE = "keywordType"

class HeadlineEditKeywordFragment : BaseDaggerFragment(), HeadlineEditAdKeywordViewHolder.OnHeadlineAdEditItemClick, HeadlineEditEmptyAdKeywordViewHolder.OnHeadlineEmptyKeywordButtonClick {
    private var keywordType: String = ""
    private var keywordList: ArrayList<String> = ArrayList()
    private lateinit var recyclerviewScrollListener: EndlessRecyclerViewScrollListener
    private lateinit var layoutManager: LinearLayoutManager
    private var cursor = ""

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HeadlineEditKeywordViewModel
    private lateinit var adapter: KeywordListAdapter
    private var groupId = 0

    companion object {
        fun getInstance(keywordType: String, groupId: Int): HeadlineEditKeywordFragment {
            return HeadlineEditKeywordFragment().apply {
                arguments = Bundle().apply {
                    putString(KEYWORD_TYPE, keywordType)
                    putInt(GROUP_ID, groupId)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return HeadlineEditKeywordFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(HeadlineAdsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            keywordType = getString(KEYWORD_TYPE) ?: ""
            groupId = getInt(GROUP_ID)
        }
        viewModel = ViewModelProvider(this, viewModelFactory).get(HeadlineEditKeywordViewModel::class.java)
        adapter = KeywordListAdapter(this, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.fragment_headline_edit_keyword), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchNextPage()
        setAdapter()
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerviewScrollListener = onRecyclerViewListener()
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
        recyclerView.addOnScrollListener(recyclerviewScrollListener)
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.parent.parent.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun onRecyclerViewListener(): EndlessRecyclerViewScrollListener {
        return object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (cursor != "") {
                    fetchNextPage()
                }
            }
        }
    }

    private fun fetchNextPage() {
        viewModel.getAdKeyword(userSession.shopId, groupId, cursor, this::onSuccessKeyword)
    }

    private fun onSuccessKeyword(data: List<GetKeywordResponse.KeywordsItem>, cursor: String) {
        this.cursor = cursor
        val keywordCounter = if (keywordType == KEYWORD_POSITIVE) {
            "${data.size} ${getString(R.string.topads_headline_edit_kata_kunci)}"
        } else {
            "${data.size} ${getString(R.string.topads_headline_edit_kata_kunci_neg)}"
        }
        val keywordUiModels = ArrayList<KeywordUiModel>()
        data.forEach { result ->
            if (result.status != -1) {
                val keywordSubType:String = getKeywordSubType(result.type)
                if (keywordType == KEYWORD_POSITIVE) {
                    keywordUiModels.add(HeadlineEditAdKeywordModel(result.tag, keywordSubType,
                            advertisingCost = Utils.convertToCurrency(result.priceBid.toLong()), priceBid = result.priceBid))
                } else if (keywordType == KEYWORD_NEGATIVE) {
                    keywordUiModels.add(HeadlineEditAdKeywordModel(result.tag, keywordSubType,
                            advertisingCost = Utils.convertToCurrency(result.priceBid.toLong()), priceBid = result.priceBid, isNegativeKeyword = true))
                }
            }
        }
        if (keywordUiModels.isEmpty()) {
            if (keywordType == KEYWORD_POSITIVE) {
                keywordUiModels.add(HeadlineEditEmptyAdKeywordModel(R.string.topads_headline_edit_keyword_empty_kata_kunci_header,
                        R.string.topads_headline_edit_keyword_empty_kata_kunci_subheader, R.string.topads_headline_edit_keyword_empty_kata_kunci_cta))
            } else {
                keywordUiModels.add(HeadlineEditEmptyAdKeywordModel(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_header,
                        R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_subheader, R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_cta))
            }
        }
        showEmptyView(keywordUiModels.isEmpty())
        adapter.setKeywordItems(keywordUiModels)
        keyword_counter.text = keywordCounter
    }

    private fun getKeywordSubType(type: Int): String {
        return if(type == Constants.KEYWORD_TYPE_EXACT || type == Constants.KEYWORD_TYPE_PHRASE){
            Constants.TITLE_1
        }else{
            Constants.TITLE_2
        }
    }

    private fun showEmptyView(isEmpty: Boolean) {
        if (isEmpty) {
            keyword_counter.hide()
            add_keyword.hide()
        } else {
            keyword_counter.show()
            add_keyword.show()
            if (keywordType == KEYWORD_POSITIVE) {
                add_keyword.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_cta)
            } else {
                add_keyword.text = getString(R.string.topads_headline_edit_keyword_empty_kata_kunci_negatif_cta)
            }
        }
    }

    override fun onDeleteItemClick(keywordModel: HeadlineEditAdKeywordModel) {

    }

    override fun onSearchTypeClick(keywordModel: HeadlineEditAdKeywordModel) {

    }

    override fun onCtaBtnClick() {

    }
}