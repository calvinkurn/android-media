package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.view.adapter.viewpager.KeywordEditPagerAdapter
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.response.KeywordDataModel
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_ADDED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORDS_DELETED
import com.tokopedia.topads.edit.utils.Constants.NEGATIVE_KEYWORD_ALL
import com.tokopedia.topads.edit.utils.Constants.POSITION0
import com.tokopedia.topads.edit.utils.Constants.POSITION1
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_CREATE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_DELETE
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_EDIT
import com.tokopedia.topads.edit.utils.Constants.POSITIVE_KEYWORD_ALL
import com.tokopedia.topads.edit.utils.Constants.STRATEGIES
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.sheet.AutoBidSelectionSheet
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.topads_edit_keyword_base_layout.*

private const val CLICK_KATA_KUNCI_POSITIF = "click - kata kunci positif"
private const val CLICK_KATA_KUNCI_NEGATIF = "click - kata kunci negatif"

class BaseEditKeywordFragment : BaseDaggerFragment(), EditKeywordsFragment.ButtonAction {

    private var buttonStateCallback: SaveButtonStateCallBack? = null
    private var btnState = true
    private var autoBidSelectionSheet: AutoBidSelectionSheet? = null
    var positivekeywordsAll: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    var negativekeywordsAll: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()

    companion object {
        fun newInstance(bundle: Bundle?): BaseEditKeywordFragment {
            val fragment = BaseEditKeywordFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_keyword_base_layout), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderViewPager()
        keyword.chipType = ChipsUnify.TYPE_SELECTED
        keyword.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_KATA_KUNCI_POSITIF, "")
            keyword.chipType = ChipsUnify.TYPE_SELECTED
            neg_keyword.chipType = ChipsUnify.TYPE_NORMAL
            view_pager.currentItem = POSITION0
        }
        neg_keyword.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(CLICK_KATA_KUNCI_NEGATIF, "")
            neg_keyword.chipType = ChipsUnify.TYPE_SELECTED
            keyword.chipType = ChipsUnify.TYPE_NORMAL
            view_pager.currentItem = POSITION1
        }
        autobid_layout.setOnClickListener {
            autoBidSelectionSheet = AutoBidSelectionSheet.newInstance()
            autoBidSelectionSheet?.setChecked(autobid_selection.text.toString())
            autoBidSelectionSheet?.show(childFragmentManager, "")
            autoBidSelectionSheet?.onItemClick = { autoBidState ->
                handleAutoBidState(autoBidState)
            }
        }
    }

    private fun handleAutoBidState(autoBidState: String) {
        if (autoBidState.isNotEmpty()) {
            autobid_selection.text = "Otomatis"
            keyword_grp.visibility = View.GONE
            autobid_ticker.visibility = View.VISIBLE
            view_pager.visibility = View.GONE
        } else {
            keyword_grp.visibility = View.VISIBLE
            autobid_selection.text = "Manual"
            autobid_ticker.visibility = View.GONE
            view_pager.visibility = View.VISIBLE
        }
    }

    private fun renderViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    private fun getViewPagerAdapter(): KeywordEditPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(EditKeywordsFragment())
        list.add(EditNegativeKeywordsFragment())
        val adapter = KeywordEditPagerAdapter(childFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    fun getButtonState(): Boolean {
        return btnState
    }

    override fun onAttach(context: Context) {
        if (context is SaveButtonStateCallBack) {
            buttonStateCallback = context
        }
        super.onAttach(context)
    }

    override fun onDetach() {
        buttonStateCallback = null
        super.onDetach()
    }

    override fun buttonDisable(enable: Boolean) {
        btnState = enable
        buttonStateCallback?.setButtonState()
    }

    override fun getScreenName(): String {
        return BaseEditKeywordFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    fun sendData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        val fragments = (view_pager?.adapter as KeywordEditPagerAdapter?)?.list
        var dataNegativeAdded: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var dataNegativeDeleted: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var deletedKeywordsPos: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var addedKeywordsPos: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var editedKeywordsPos: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var strategies: ArrayList<String>? = arrayListOf()

        if (fragments?.get(0) is EditKeywordsFragment) {
            val bundle: Bundle = (fragments[0] as EditKeywordsFragment).sendData()
            addedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_CREATE)
            deletedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_DELETE)
            editedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_EDIT)
            positivekeywordsAll = bundle.getParcelableArrayList(POSITIVE_KEYWORD_ALL)
        }
        if (fragments?.get(1) is EditNegativeKeywordsFragment) {
            val bundle: Bundle = (fragments[1] as EditNegativeKeywordsFragment).sendData()
            dataNegativeAdded = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS_ADDED)
            dataNegativeDeleted = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS_DELETED)
            negativekeywordsAll = bundle.getParcelableArrayList(NEGATIVE_KEYWORD_ALL)

        }
        strategies?.clear()
        if(autobid_selection.text == "Otomatis") {
            strategies?.add("auto_bid")
        } else {
            strategies?.add("")
        }
        dataMap[POSITIVE_CREATE] = addedKeywordsPos
        dataMap[POSITIVE_DELETE] = deletedKeywordsPos
        dataMap[POSITIVE_EDIT] = editedKeywordsPos
        dataMap[NEGATIVE_KEYWORDS_ADDED] = dataNegativeAdded
        dataMap[NEGATIVE_KEYWORDS_DELETED] = dataNegativeDeleted
        dataMap[STRATEGIES] = strategies
        return dataMap
    }

    fun getKeywordNameItems(): ArrayList<KeywordDataModel>? {
        var items: ArrayList<KeywordDataModel>? = arrayListOf()
        val fragments = (view_pager?.adapter as KeywordEditPagerAdapter?)?.list
        if (fragments?.get(0) is EditKeywordsFragment) {
            positivekeywordsAll?.forEach {
                var model = KeywordDataModel()
                model.keywordName = it.tag
                model.keywordId = it.keywordId
                model.keywordType = "positif"
                items?.add(model)
            }
        }
        if (fragments?.get(1) is EditNegativeKeywordsFragment) {
            negativekeywordsAll?.forEach {
                var model = KeywordDataModel()
                model.keywordName = it.tag
                model.keywordId = it.keywordId
                model.keywordType = "negatif"
                items?.add(model)
            }
        }
        return items
    }

}