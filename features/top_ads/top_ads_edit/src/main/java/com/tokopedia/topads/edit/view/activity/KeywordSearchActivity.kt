package com.tokopedia.topads.edit.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.response.SearchData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.data.util.Utils.validateKeywordCountAndChars
import com.tokopedia.topads.common.view.sheet.TipSheetKeywordList
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants.GROUP_ID
import com.tokopedia.topads.edit.view.adapter.edit_keyword.KeywordSearchAdapter
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment.Companion.PRODUCT_IDS_SELECTED
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment.Companion.SEARCH_QUERY
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment.Companion.SELECTED_KEYWORDS
import com.tokopedia.topads.edit.view.model.KeywordAdsViewModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.topads_edit_keyword_search_layout.*
import javax.inject.Inject

private const val CLICK_MANUAL_SEARCH = "click - ceklist rekomendasi kata kunci manual search"
private const val EVENT_CLICK_MANUAL_SEARCH = "kata kunci terpilih yang di ceklist"
private const val CLICK_SUBMIT_BUTT = "'click - pilih kata kunci"
private const val EVENT_CLICK_SUBMIT_BUTT = "kata kunci terpilih yang di ceklist"

class KeywordSearchActivity : BaseActivity(), HasComponent<TopAdsEditComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: KeywordAdsViewModel
    private lateinit var adapter: KeywordSearchAdapter
    private lateinit var search: SearchBarUnify
    private var groupId: String = ""
    private var userID: String = ""
    private var manualKeywords: MutableList<SearchData> = mutableListOf()
    private var tvToolTipText: Typography? = null
    private var imgTooltipIcon: ImageUnify? = null


    override fun getComponent(): TopAdsEditComponent {
        return DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        userID = UserSession(this).userId
        groupId = intent?.getStringExtra(GROUP_ID) ?: ""
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(KeywordAdsViewModel::class.java)
        adapter = KeywordSearchAdapter(::onCheckItem)
        setContentView(R.layout.topads_edit_keyword_search_layout)
        setSearchBar()
        fetchData()
        keyword_list.adapter = adapter
        keyword_list.layoutManager = LinearLayoutManager(this)
        val tooltipView = layoutInflater.inflate(com.tokopedia.topads.common.R.layout.tooltip_custom_view, null).apply {
            tvToolTipText = this.findViewById(R.id.tooltip_text)
            tvToolTipText?.text = getString(com.tokopedia.topads.common.R.string.topads_common_tip_memilih_kata_kunci)
            imgTooltipIcon = this.findViewById(R.id.tooltip_icon)
            imgTooltipIcon?.setImageDrawable(AppCompatResources.getDrawable(this.context, com.tokopedia.topads.common.R.drawable.topads_ic_tips))
        }

        tip_btn?.addItem(tooltipView)
        tip_btn.setOnClickListener {
            TipSheetKeywordList().show(supportFragmentManager, KeywordSearchActivity::class.java.name)
        }
        btn_next.setOnClickListener {
            val eventLabel = "$groupId - $EVENT_CLICK_SUBMIT_BUTT"
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_SUBMIT_BUTT, eventLabel, userID)
            val returnIntent = Intent()
            returnIntent.putParcelableArrayListExtra(SELECTED_KEYWORDS, ArrayList(adapter.getSelectedItem()))
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun initInjector() {
        val toAdsEditComponent = DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
        toAdsEditComponent.inject(this)
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
        Utils.setSearchListener(search, this, rootView, ::fetchData)
        val searchTextField = search.searchBarTextField
        searchTextField.hint = getString(R.string.topads_common_search_hint_activity)

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
        manualAd.visibility = View.GONE
        if (search.searchBarTextField.text.toString().isNotEmpty()) {
            adapter.items.clear()
            txtError.text = validateKeywordCountAndChars(this, search.searchBarTextField.text.toString().trim())
            if (txtError.text.isNotEmpty()) {
                setEmpty(true)
                txtError.visibility = View.VISIBLE
                onSelectedItem()
            } else {
                txtError.visibility = View.GONE
                if (!intent?.getStringExtra(PRODUCT_IDS_SELECTED).isNullOrEmpty()) {
                    viewModel.searchKeyword(search.searchBarTextField.text.toString(), intent?.getStringExtra(PRODUCT_IDS_SELECTED)
                            ?: "", ::onSuccessSearch)
                } else {
                    setEmpty(true)
                    if (manualKeywords.isNotEmpty())
                        adapter.items.addAll(manualKeywords)
                    checkIfNeedsManualAddition(null)
                }
            }
        }
    }

    private fun onSuccessSearch(data: List<SearchData>) {
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

    private fun checkIfNeedsManualAddition(listKeywords: MutableList<String>?) {
        if (listKeywords?.find { key -> search.searchBarTextField.text.toString() == key } == null) {
            manualAd.visibility = View.VISIBLE
            dividerManual.visibility = View.VISIBLE
            manualAd.text = Html.fromHtml(String.format(getString(R.string.topads_common_new_manual_key), search.searchBarTextField.text.toString()))
            setSpannable(getString(R.string.topads_common_tambah_button), rootView)
        }
    }

    private fun setSpannable(moreInfo: String, view: View) {
        val desc = view.findViewById<TextView>(R.id.manualAd)
        val spannableText = SpannableString(moreInfo)
        val startIndex = 0
        val endIndex = spannableText.length
        spannableText.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Green_G500)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addManualKeyword()
                search.searchBarTextField.text.clear()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = ContextCompat.getColor(baseContext, com.tokopedia.unifyprinciples.R.color.Green_G500)
            }
        }
        spannableText.setSpan(clickableSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        desc.movementMethod = LinkMovementMethod.getInstance()
        desc.append(spannableText)
    }

    private fun addManualKeyword() {
        if (adapter.getSelectedItem().find { it.keyword == search.searchBarTextField.text.toString() } != null) {
            makeToast()
        } else {
            headlineList.visibility = View.VISIBLE
            manualAd.visibility = View.GONE
            dividerManual.visibility = View.GONE
            val item = SearchData()
            item.keyword = search.searchBarTextField.text.toString()
            item.onChecked = true
            item.totalSearch = -1
            manualKeywords.add(item)
            adapter.items.add(0, item)
            adapter.notifyItemInserted(0)
            onSelectedItem()
        }
    }

    private fun makeToast() {
        SnackbarManager.make(this, getString(R.string.keyword_already_exists),
                Snackbar.LENGTH_LONG)
                .show()
    }

    private fun setEmpty(setEmpty: Boolean) {
        if (setEmpty) {
            headlineList.visibility = View.GONE
        } else {
            headlineList.visibility = View.VISIBLE
            emptyLayout.visibility = View.GONE
        }
    }

    private fun onCheckItem() {
        val eventLabel = "$groupId - $EVENT_CLICK_MANUAL_SEARCH"
        TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_MANUAL_SEARCH, eventLabel, userID)
        onSelectedItem()
    }
}


