package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.topads.common.CustomViewPager
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import com.tokopedia.topads.common.data.response.GroupEditInput
import com.tokopedia.topads.common.data.response.KeySharedModel
import com.tokopedia.topads.common.data.response.TopAdsBidSettingsModel
import com.tokopedia.topads.common.view.TopadsAutoBidSwitchPartialLayout
import com.tokopedia.topads.common.view.adapter.viewpager.KeywordEditPagerAdapter
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.utils.Constants
import com.tokopedia.topads.edit.utils.Constants.BID_TYPE
import com.tokopedia.topads.edit.utils.Constants.GROUP_STRATEGY
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
import com.tokopedia.topads.edit.view.sheet.BidInfoBottomSheet
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

private const val CLICK_KATA_KUNCI_POSITIF = "click - kata kunci positif"
private const val CLICK_KATA_KUNCI_NEGATIF = "click - kata kunci negatif"
private const val AUTO_BID_STATE = "auto"
private const val CLICK_BID_TYPE_SELECT = "click - mode pengaturan"
private const val MANUAL_LAYOUT_EVENT_LABEL = "mode pengaturan atur manual"
private const val OTOMATIS_LAYOUT_EVENT_LABEL = "mode pengaturan atur otomatis"
private const val OTOMATIS_LEARN_MORE_LINK = "https://seller.tokopedia.com/edu/topads-otomatis/"

class BaseEditKeywordFragment : BaseDaggerFragment(), EditKeywordsFragment.ButtonAction {

    private var autoBidSwitch: TopadsAutoBidSwitchPartialLayout? = null
    private var keywordGroup: LinearLayout? = null
    private var autoBidTicker: Ticker? = null
    private var chipKeyword: ChipsUnify? = null
    private var chipNegativeKeyword: ChipsUnify? = null
    private var viewPager: CustomViewPager? = null

    private var buttonStateCallback: SaveButtonStateCallBack? = null
    private var btnState = true
    private var bidStrategy: String = ""
    private var positivekeywordsAll: ArrayList<KeySharedModel>? = arrayListOf()
    private var negativekeywordsAll: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
    private val sharedViewModel by lazy {
        ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    companion object {
        fun newInstance(bundle: Bundle?): BaseEditKeywordFragment {
            val fragment = BaseEditKeywordFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(
            resources.getLayout(R.layout.topads_edit_keyword_base_layout),
            container,
            false
        )
        initView(view)
        return view
    }

    private fun initView(view: View) {
        autoBidSwitch = view.findViewById(R.id.autoBidSwitchLayout)
        keywordGroup = view.findViewById(R.id.keyword_grp)
        autoBidTicker = view.findViewById(R.id.autobid_ticker)
        chipKeyword = view.findViewById(R.id.keyword)
        chipNegativeKeyword = view.findViewById(R.id.neg_keyword)
        viewPager = view.findViewById(R.id.view_pager)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        autoBidTicker?.setHtmlDescription(resources.getString(R.string.topads_edit_auto_bid_ticker_title))

        initListeners()
        renderViewPager()
        chipKeyword?.chipType = ChipsUnify.TYPE_SELECTED

        sharedViewModel.setIsWhiteListedUser(
            arguments?.getBoolean(ParamObject.ISWHITELISTEDUSER) ?: false
        )
        arguments?.getString(GROUP_STRATEGY, "")?.let { handleInitialAutoBidState(it) }
    }

    private fun initListeners() {
        autoBidTicker?.setDescriptionClickEvent(object : TickerCallback {
            override fun onDescriptionViewClick(linkUrl: CharSequence) {
                RouteManager.route(
                    context, ApplinkConstInternalGlobal.WEBVIEW, OTOMATIS_LEARN_MORE_LINK
                )
            }

            override fun onDismiss() {}
        })

        chipKeyword?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(
                CLICK_KATA_KUNCI_POSITIF,
                ""
            )
            chipKeyword?.chipType = ChipsUnify.TYPE_SELECTED
            chipNegativeKeyword?.chipType = ChipsUnify.TYPE_NORMAL
            viewPager?.currentItem = POSITION0
        }

        chipNegativeKeyword?.setOnClickListener {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendEditFormEvent(
                CLICK_KATA_KUNCI_NEGATIF,
                ""
            )
            chipNegativeKeyword?.chipType = ChipsUnify.TYPE_SELECTED
            chipKeyword?.chipType = ChipsUnify.TYPE_NORMAL
            viewPager?.currentItem = POSITION1
        }

        autoBidSwitch?.let {
            it.onCheckBoxStateChanged = { isAutomatic ->
                handleAutoBidState(if (isAutomatic) AUTO_BID_STATE else "")
            }

            it.onInfoClicked = {
                BidInfoBottomSheet().show(childFragmentManager, "")
            }
        }

    }

    private fun handleInitialAutoBidState(it: String) {
        handleAutoBidState(it)
        autoBidSwitch?.switchBidEditKeyword?.isChecked = it.isNotEmpty()
    }

    private fun handleAutoBidState(autoBidState: String) {
        sharedViewModel.setAutoBidStatus(autoBidState)
        bidStrategy = autoBidState
        if (autoBidState.isNotEmpty()) {
            buttonDisable(true)
            keywordGroup?.visibility = View.GONE
            autoBidTicker?.visibility = View.VISIBLE
            viewPager?.visibility = View.GONE
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_BID_TYPE_SELECT, OTOMATIS_LAYOUT_EVENT_LABEL
            )
        } else {
            keywordGroup?.visibility = View.VISIBLE
            autoBidTicker?.visibility = View.GONE
            viewPager?.visibility = View.VISIBLE
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEditEvent(
                CLICK_BID_TYPE_SELECT, MANUAL_LAYOUT_EVENT_LABEL
            )
        }
    }

    private fun renderViewPager() {
        viewPager?.adapter = getViewPagerAdapter()
        viewPager?.disableScroll(true)
    }

    private fun getViewPagerAdapter(): KeywordEditPagerAdapter {
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
        val fragments = (viewPager?.adapter as KeywordEditPagerAdapter?)?.list
        var dataNegativeAdded: ArrayList<KeySharedModel>? = arrayListOf()
        var dataNegativeDeleted: ArrayList<KeySharedModel>? = arrayListOf()
        var deletedKeywordsPos: ArrayList<KeySharedModel>? = arrayListOf()
        var addedKeywordsPos: ArrayList<KeySharedModel>? = arrayListOf()
        var editedKeywordsPos: ArrayList<KeySharedModel>? = arrayListOf()
        val strategies: ArrayList<String> = arrayListOf()
        var bidSettings: ArrayList<TopAdsBidSettingsModel>? = arrayListOf()
        var bidInfoDataItem: List<GroupEditInput.Group.TopadsSuggestionBidSetting>? = null
        var bidGroup = 0

        if (bidStrategy.isEmpty()) {
            if (fragments?.get(0) is EditKeywordsFragment) {
                val bundle: Bundle = (fragments[0] as EditKeywordsFragment).sendData()
                addedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_CREATE)
                deletedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_DELETE)
                editedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_EDIT)
                positivekeywordsAll = bundle.getParcelableArrayList(POSITIVE_KEYWORD_ALL)
                bidGroup = bundle.getInt(Constants.PRICE_BID)
                bidSettings = bundle.getParcelableArrayList(BID_TYPE)
                bidInfoDataItem = (fragments[0] as EditKeywordsFragment).getSuggestedBidSettings()
            }
            if (fragments?.get(1) is EditNegativeKeywordsFragment) {
                val bundle: Bundle = (fragments[1] as EditNegativeKeywordsFragment).sendData()
                dataNegativeAdded = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS_ADDED)
                dataNegativeDeleted = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS_DELETED)
                negativekeywordsAll = bundle.getParcelableArrayList(NEGATIVE_KEYWORD_ALL)

            }
        }
        strategies.clear()
        if (autoBidSwitch?.isBidAutomatic() == true) {
            strategies.add("auto_bid")
        }
        dataMap[POSITIVE_CREATE] = addedKeywordsPos
        dataMap[POSITIVE_DELETE] = deletedKeywordsPos
        dataMap[POSITIVE_EDIT] = editedKeywordsPos
        dataMap[NEGATIVE_KEYWORDS_ADDED] = dataNegativeAdded
        dataMap[NEGATIVE_KEYWORDS_DELETED] = dataNegativeDeleted
        dataMap[STRATEGIES] = strategies
        dataMap[Constants.PRICE_BID] = bidGroup
        dataMap[BID_TYPE] = bidSettings
        dataMap[ParamObject.SUGGESTION_BID_SETTINGS] = bidInfoDataItem
        return dataMap
    }

    fun getKeywordNameItems(): MutableList<Map<String, Any>> {
        val fragments = (viewPager?.adapter as KeywordEditPagerAdapter?)?.list
            ?: return mutableListOf()

        val items: MutableList<Map<String, Any>> = mutableListOf()
        if (fragments.size > 0 && fragments.get(0) is EditKeywordsFragment) {
            positivekeywordsAll?.forEachIndexed { index, it ->
                val map = mapOf(
                    "name" to it.name, "id" to it.id, "type" to "positif"
                )
                items.add(map)
            }
        }
        if (fragments.size > 1 && fragments.get(1) is EditNegativeKeywordsFragment) {
            negativekeywordsAll?.forEach {
                val map = mapOf(
                    "name" to it.tag, "id" to it.keywordId, "type" to "negatif"
                )
                items.add(map)
            }
        }
        return items
    }

}