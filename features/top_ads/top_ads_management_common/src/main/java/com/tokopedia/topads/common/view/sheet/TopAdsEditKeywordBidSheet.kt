package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.toIntOrZero
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

class TopAdsEditKeywordBidSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var minBid = "0"
    private var maxBid = "0"
    private var suggestedBid = "0"
    private var position = 0
    private var name = ""
    private var fromEdit = 99
    private var userID: String = ""
    private var groupId: String = ""
    var onSaved: ((bid: String, pos: Int) -> Unit)? = null
    private var shopID = ""

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
                        setMessageErrorField(getString(R.string.min_bid_error_new), minBid, true)
                    }
                    result > maxBid.toDouble() -> {
                        setMessageErrorField(getString(R.string.max_bid_error_new), maxBid, true)
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
    }

    private fun getDatafromArguments() {
        minBid = arguments?.getString(MIN_BID) ?: "0"
        maxBid = arguments?.getString(MAX_BID) ?: "0"
        position = arguments?.getInt(ITEM_POSITION) ?: 0
        suggestedBid = arguments?.getString(SUGGESTION_BID) ?: "0"
        name = arguments?.getString(KEYWORD_NAME) ?: ""
        fromEdit = arguments?.getInt(FROM_EDIT) ?: 99
        groupId = arguments?.getString(GROUP_ID) ?: "0"
    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
        if (bool)
            btnSave.isEnabled = false
    }

    private fun initView() {
        setCloseClickListener {
            dismiss()
        }
        btnSave.setOnClickListener {
            onSaved?.invoke(budget.textFieldInput.text.toString().removeCommaRawString(), position)
            dismiss()
        }
        if(suggestedBid.toIntOrZero() < minBid.toIntOrZero()) {
            budget.textFieldInput.setText(minBid)
        } else {
            budget.textFieldInput.setText(suggestedBid)
        }
        setTitle(name)
    }

    companion object {

        private const val MAX_BID = "max"
        private const val MIN_BID = "min"
        private const val SUGGESTION_BID = "suggest"
        private const val KEYWORD_NAME = "keywordName"
        private const val ITEM_POSITION = "pos"
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
        isKeyboardOverlap = false
        setChild(contentView)
    }
}