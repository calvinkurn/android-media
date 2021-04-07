package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.text.Html
import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher
import kotlinx.android.synthetic.main.topads_common_edit_key_bid_sheet.*

/**
 * Created by Pika on 21/7/20.
 */


private const val CLICK_BUDGET_CREATE = "click - biaya kata kunci box"
private const val EVENT_CLICK_BUDGET_CREATE = "biaya yang diinput"
private const val CLICK_KEYWORD_TYPE = "click - tipe pencarian"
private const val BROAD = "pencarian luas"
private const val SPECIFIC = "pencarian specific"
private const val CLICK_KEYWORD_DELETE = "click - delete keyword selected"
private const val CLICK_SUBMIT_BUTTON = "click - simpan keyword setup"

class TopAdsEditKeywordBidSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var minBid = "0"
    private var maxBid = "0"
    private var suggestedBid = "0"
    private var position = 0
    private var type = 11
    private var name = ""
    private var fromEdit = 99
    private var userID: String = ""
    private var groupId: String = ""
    var onSaved: ((bid: String, type: Int, pos: Int) -> Unit)? = null
    var onDelete: ((pos: Int) -> Unit)? = null
    private var shopID = ""
    private var typeSheet = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userID = UserSession(view.context).userId
        shopID = UserSession(view.context).shopId
        initView()
        sendAnalyticsData()
        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                when {
                    result < minBid.toDouble() -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                    }
                    result > maxBid.toDouble() -> {
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }

                    result % 50 != 0 -> {
                        setMessageErrorField(getString(R.string.topads_common_error_multiple_50), "50", true)
                    }

                    else -> {
                        budget.setError(false)
                        budget.setMessage("")
                        btnSave.isEnabled = true
                    }
                }
            }
        })
    }

    private fun sendAnalyticsData() {
        budget.textFieldInput.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                if (fromEdit != 1) {
                    val eventLabel = "$shopID - $name - $EVENT_CLICK_BUDGET_CREATE"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUDGET_CREATE, eventLabel, userID)
                } else {
                    val eventLabel = "$groupId - $name - $EVENT_CLICK_BUDGET_CREATE"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_BUDGET_CREATE, eventLabel, userID)
                }
            }
        }


        title_1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (fromEdit != 1) {
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_KEYWORD_TYPE, BROAD, userID)
                } else
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_KEYWORD_TYPE, BROAD, userID)
            }
        }
        title_2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (fromEdit != 1)
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUDGET_CREATE, SPECIFIC, userID)
                else
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_BUDGET_CREATE, SPECIFIC, userID)
            }
        }

    }

    fun getSelectedSortId(): Int {
        return when (dialog?.radio_group?.checkedRadioButtonId) {
            R.id.title_1 -> BROAD_POSITIVE
            R.id.title_2 -> EXACT_POSITIVE
            else -> BROAD_POSITIVE
        }
    }

    private fun getDatafromArguments() {
        minBid = arguments?.getString(MIN_BID) ?: "0"
        maxBid = arguments?.getString(MAX_BID) ?: "0"
        position = arguments?.getInt(ITEM_POSITION) ?: 0
        suggestedBid = arguments?.getString(SUGGESTION_BID) ?: "0"
        type = arguments?.getInt(CURRENT_KEY_TYPE) ?: 11
        name = arguments?.getString(KEYWORD_NAME) ?: ""
        fromEdit = arguments?.getInt(FROM_EDIT) ?: 99
        groupId = arguments?.getString(GROUP_ID) ?: "0"
        typeSheet = arguments?.getString("type") ?: ""
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(Html.fromHtml(String.format(error, bid)))
        if (bool)
            btnSave.isEnabled = false
    }

    private fun initView() {
        prepareViewForCreate()

        setCloseClickListener {
            dismiss()
        }
        btnDeleteKeyword?.setOnClickListener {
            if (fromEdit != 1)
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_KEYWORD_DELETE, "$shopID - $name", userID)
            else
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_KEYWORD_DELETE, "$groupId - $name", userID)

            dismiss()
            onDelete?.invoke(position)
        }

        btnSave.setOnClickListener {
            if (fromEdit != 1) {
                val eventLabel = "$shopID - $name - ${budget.textFieldInput.text} - ${getSelectedSortId()}"

                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_SUBMIT_BUTTON, eventLabel, userID)
            } else {
                val eventLabel = "$groupId - $name - ${budget.textFieldInput.text} - ${getSelectedSortId()}"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(CLICK_SUBMIT_BUTTON, eventLabel, userID)
            }
            onSaved?.invoke(budget.textFieldInput.text.toString().removeCommaRawString(), getSelectedSortId(), position)
            dismiss()
        }

        btn_close.setOnClickListener {
            dismiss()
        }
        if (type == EXACT_POSITIVE) {
            title_2.isChecked = true
        } else
            title_1.isChecked = true

        budget.textFieldInput.setText(suggestedBid)
        title?.text = name

        if (fromEdit == 1) {
            desc_1?.text = resources.getString(R.string.topads_common_keyword_edit_info_sheet_desc_1_foredit)
            desc_2?.text = resources.getString(R.string.topads_common_keyword_edit_info_sheet_desc_2_foredit)
        }

        desc_1?.setOnClickListener {
            title_1.isChecked = true
        }
        desc_2?.setOnClickListener {
            title_2.isChecked = true
        }
    }

    private fun prepareViewForCreate() {
        if (typeSheet.isNotEmpty()) {
            divider?.gone()
            txtTypeKey?.gone()
            radio_group?.gone()
            divider1?.gone()
            btnDeleteKeyword?.gone()
            btnSave?.text = getString(R.string.lanjutkan)
            btnSave?.setMargin(16, 24, 16, 16)
        }

    }

    companion object {

        private const val MAX_BID = "max"
        private const val MIN_BID = "min"
        private const val SUGGESTION_BID = "suggest"
        private const val KEYWORD_NAME = "keywordName"
        private const val CURRENT_KEY_TYPE = "currentKeyType"
        private const val ITEM_POSITION = "pos"
        private const val EXACT_POSITIVE = 21
        private const val BROAD_POSITIVE = 11
        private const val FROM_EDIT = "fromEdit"
        private const val GROUP_ID = "group_id"

        fun createInstance(data: Bundle): TopAdsEditKeywordBidSheet {
            return TopAdsEditKeywordBidSheet().apply {
                arguments = data
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDatafromArguments()
        initChildLayout()
        super.onCreate(savedInstanceState)

    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_common_edit_key_bid_sheet, null)
        showHeader = false
        showCloseIcon = false
        if (typeSheet.isNotEmpty())
            isKeyboardOverlap = false
        setChild(contentView)
    }
}