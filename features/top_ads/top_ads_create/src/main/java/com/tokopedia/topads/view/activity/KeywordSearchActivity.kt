package com.tokopedia.topads.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.adapter.keyword.KeywordSearchAdapter
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment.Companion.PRODUCT_IDS_SELECTED
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment.Companion.SEARCH_QUERY
import com.tokopedia.topads.view.fragment.KeywordAdsListFragment.Companion.SELECTED_KEYWORDS
import com.tokopedia.topads.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_create_keyword_search_layout.*
import kotlinx.android.synthetic.main.topads_create_layout_keyword_list_empty_tip.view.*
import javax.inject.Inject

private const val CLICK_MANUAL_SEARCH = "click - ceklist rekomendasi kata kunci manual search"
private const val EVENT_CLICK_MANUAL_SEARCH = "kata kunci terpilih yang di ceklist"
private const val CLICK_SUBMIT_BUTT = "'click - pilih kata kunci"
private const val EVENT_CLICK_SUBMIT_BUTT = "kata kunci terpilih yang di ceklist"

class KeywordSearchActivity : BaseActivity(), HasComponent<CreateAdsComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var adapter: KeywordSearchAdapter
    private lateinit var search: SearchBarUnify
    private var userID: String = ""
    private var shopID = ""
    private var manualKeywords: MutableList<SearchData> = mutableListOf()
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    private fun initInjector() {
        DaggerCreateAdsComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent).build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID = UserSession(this).userId
        shopID = UserSession(this).shopId
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initInjector()
        viewModel = ViewModelProvider(this, viewModelFactory).get(KeywordAdsViewModel::class.java)
        adapter = KeywordSearchAdapter(::onCheckedItem)
        setContentView(R.layout.topads_create_keyword_search_layout)
        setSearchBar()
        fetchData()
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(this)
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(com.tokopedia.topads.common.R.string.topads_common_tip_memilih_kata_kunci)

            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(this.context.getResDrawable(com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }

        tip_btn?.addItem(tooltipView)
        tip_btn.setOnClickListener {
            TipSheetKeywordList().show(supportFragmentManager, KeywordAdsListFragment::class.java.name)
        }

        emptyLayout.ic_tip.setImageDrawable(AppCompatResources.getDrawable(this, com.tokopedia.topads.common.R.drawable.ic_bulp_fill))
        emptyLayout.imageView2.setImageDrawable(AppCompatResources.getDrawable(this, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        emptyLayout.imageView3.setImageDrawable(AppCompatResources.getDrawable(this, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        emptyLayout.imageView4.setImageDrawable(AppCompatResources.getDrawable(this, com.tokopedia.topads.common.R.drawable.topads_create_ic_checklist))
        btn_next.setOnClickListener {
            val eventLabel = "$shopID - $EVENT_CLICK_SUBMIT_BUTT"
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SUBMIT_BUTT, eventLabel, userID)
            val returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra(SELECTED_KEYWORDS, ArrayList(adapter.getSelectedItem()))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun setSearchBar() {
        setSearchParam()
        setHeader()
    }

    private fun setSearchParam() {
        search = SearchBarUnify(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        search.iconDrawable = null
        search.showIcon = false
        search.searchBarTextField.setText(intent.getStringExtra(SEARCH_QUERY))
        search.layoutParams = param
        val searchTextField = search.searchBarTextField
        searchTextField.hint = getString(R.string.topads_common_search_hint_activity)
        Utils.setSearchListener(search, this, rootView, ::fetchData)
    }

    private fun setHeader() {
        val wrapper = LinearLayout(this)
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        wrapper.layoutParams = param
        wrapper.addView(search)
        header_toolbar?.customView(wrapper)
        header_toolbar?.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun onSelectedItem() {
        selected_info.text = String.format(getString(R.string.format_selected_keyword), adapter.getSelectedItem().size)
    }

    private fun fetchData() {
        manualAd?.gone()
        manualAdTxt?.gone()
        if (search.searchBarTextField.text.toString().isNotEmpty()) {
            adapter.items.clear()
            txtError.text = Utils.validateKeywordCountAndChars(this, search.searchBarTextField.text.toString().trim())
            if (txtError.text.isNotEmpty()) {
                setEmpty(true)
                txtError.visibility = View.VISIBLE
                onSelectedItem()
            } else {
                txtError.visibility = View.GONE
                viewModel.searchKeyword(search.searchBarTextField.text.toString(), intent?.getStringExtra(PRODUCT_IDS_SELECTED)
                        ?: "", ::onSuccessSearch)
            }
        }
    }

    private fun makeToast() {
        SnackbarManager.make(this, getString(R.string.keyword_already_exists),
                Snackbar.LENGTH_LONG)
                .show()
    }

    private fun onSuccessSearch(data: List<SearchData>) {
        loader?.gone()
        val listKeywords: MutableList<String> = mutableListOf()
        if (manualKeywords.isNotEmpty())
            adapter.items.addAll(manualKeywords)
        data.forEach {
            adapter.items.add(it)
            listKeywords.add(it.keyword ?: "")
        }
        checkIfNeedsManualAddition(listKeywords)
        if (manualKeywords.isEmpty())
            setEmpty(data.isEmpty())
        onSelectedItem()
        adapter.notifyDataSetChanged()
    }

    private fun checkIfNeedsManualAddition(listKeywords: MutableList<String>) {
        if (listKeywords.find { key -> search.searchBarTextField.text.toString() == key } == null) {
            manualAd?.visible()
            manualAdTxt?.visible()
            dividerManual?.visible()
            manualAdTxt.text = MethodChecker.fromHtml(String.format(getString(R.string.topads_common_new_manual_key), search.searchBarTextField.text.toString()))
            manualAd?.setOnClickListener {
                addManualKeyword()
                search.searchBarTextField.text.clear()
            }
        }
    }

    private fun addManualKeyword() {
        if (adapter.getSelectedItem().find { it.keyword == search.searchBarTextField.text.toString() } != null) {
            makeToast()
        } else {
            loader?.gone()
            headlineList.visible()
            manualAd.gone()
            manualAdTxt?.gone()
            dividerManual?.gone()
            val item = SearchData()
            item.keyword = search.searchBarTextField.text.toString()
            item.onChecked = true
            item.totalSearch = -1
            manualKeywords.add(item)
            adapter.items.add(0, item)
            adapter.notifyItemInserted(0)
            onSelectedItem()
        }
        setEmpty(false)
    }

    private fun setEmpty(setEmpty: Boolean) {
        if (setEmpty) {
            emptyImage?.visible()
            title_empty?.visible()
            desc_empty?.visible()
            headlineList.visibility = View.GONE
        } else {
            emptyImage?.gone()
            title_empty?.gone()
            desc_empty?.gone()
            tip_btn.visibility = View.VISIBLE
            headlineList.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    private fun onCheckedItem() {
        val eventLabel = "$shopID - $EVENT_CLICK_MANUAL_SEARCH"
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_MANUAL_SEARCH, eventLabel, userID)
        onSelectedItem()
    }
}


