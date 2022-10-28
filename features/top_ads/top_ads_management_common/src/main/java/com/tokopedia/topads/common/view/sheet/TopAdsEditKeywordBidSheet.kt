package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.common.constant.TopAdsCommonConstant.BUDGET_MULTIPLE_FACTOR
import com.tokopedia.topads.common.data.util.Utils.removeCommaRawString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.text.currency.NumberTextWatcher

/**
 * Created by Pika on 21/7/20.
 */

private const val CLICK_BUDGET_CREATE = "click - biaya kata kunci box"
private const val EVENT_CLICK_BUDGET_CREATE = "biaya yang diinput"
private const val INTENT_FROM_EDIT = 99

class TopAdsEditKeywordBidSheet : BottomSheetUnify() {

    private var dailybudget_description: Typography? = null
    private var budget: TextFieldUnify? = null
    private var min_suggested_bid: Typography? = null
    private var btnSave: UnifyButton? = null

    private var contentView: View? = null
    private var minBid = "0"
    private var maxBid = "0"
    private var suggestedBid = "0"
    private var position = 0
    private var name = ""
    private var fromEdit = INTENT_FROM_EDIT
    private var fromDetail = false
    private var fromRekomendasi = false
    private var fromCreate = false
    private var dailyBudget = ""
    private var keywordBudget = ""
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
        budget?.let {
            it.textFieldInput.addTextChangedListener(object :
                NumberTextWatcher(it.textFieldInput, "0") {
                override fun onNumberChanged(number: Double) {
                    super.onNumberChanged(number)
                    val result = number.toInt()
                    when {
                        result < minBid.toDouble() -> {
                            min_suggested_bid?.visibility = View.GONE
                            setMessageErrorField(getString(R.string.min_bid_error_new), minBid, true)
                        }
                        (result >= minBid.toDouble() && result < suggestedBid.toDouble()) -> {
                            budget?.setError(false)
                            budget?.setMessage("")
                            btnSave?.isEnabled = true
                            if (fromDetail)
                                min_suggested_bid?.visibility = View.VISIBLE
                            checkResultIsMultipleOfFifty(result)
                        }
                        result > maxBid.toDouble() -> {
                            min_suggested_bid?.visibility = View.GONE
                            setMessageErrorField(getString(R.string.max_bid_error_new), maxBid, true)
                        }

                        else -> {
                            min_suggested_bid?.visibility = View.GONE
                            budget?.setError(false)
                            budget?.setMessage("")
                            btnSave?.isEnabled = true
                            checkResultIsMultipleOfFifty(result)
                        }
                    }
                }
            })
        }
    }

    private fun checkResultIsMultipleOfFifty(result: Int) {
        if (result % BUDGET_MULTIPLE_FACTOR != 0) {
            min_suggested_bid?.visibility = View.GONE
            setMessageErrorField(getString(R.string.topads_common_error_multiple_50), "50", true)
        }
    }

    private fun sendAnalyticsData() {
        budget?.textFieldInput?.setOnFocusChangeListener { v, hasFocus ->

            if (hasFocus) {
                if (fromEdit != 1) {
                    val eventLabel = "$shopID - $name - $EVENT_CLICK_BUDGET_CREATE"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BUDGET_CREATE,
                        eventLabel,
                        userID)
                } else {
                    val eventLabel = "$groupId - $name - $EVENT_CLICK_BUDGET_CREATE"
                    TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEventEdit(
                        CLICK_BUDGET_CREATE,
                        eventLabel,
                        userID)
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
        fromEdit = arguments?.getInt(FROM_EDIT) ?: INTENT_FROM_EDIT
        groupId = arguments?.getString(GROUP_ID) ?: "0"
        fromDetail = arguments?.getBoolean(FROM_DETAIL) ?: false
        fromCreate = arguments?.getBoolean(FROM_CREATE) ?: false
        fromRekomendasi = arguments?.getBoolean(FROM_REKOMENDASI) ?: false
        dailyBudget = arguments?.getString(DAILY_BUDGET) ?: suggestedBid
        keywordBudget = arguments?.getString(KEYWORD_BUDGET) ?: "0"

    }

    private fun setMessageErrorField(error: String, bid: String, bool: Boolean) {
        budget?.setError(bool)
        budget?.setMessage(MethodChecker.fromHtml(String.format(error, bid)))
        if (bool)
            btnSave?.isEnabled = false
    }

    private fun initView() {
        if (fromDetail) {
            dailybudget_description?.show()
            if (fromRekomendasi) {
                dailybudget_description?.text = getString(R.string.topads_edit_bid_rekomendasi)
            } else {
                dailybudget_description?.text = getString(R.string.topads_edit_bid_pencerian)
            }
            if (dailyBudget.toIntOrZero() < suggestedBid.toIntOrZero()) {
                min_suggested_bid?.visibility = View.VISIBLE
            } else {
                budget?.setMessage("Rekomendasi Rp$suggestedBid")
            }
        } else {
            budget?.textFiedlLabelText?.text = "Biaya Kata kunci"
            dailybudget_description?.hide()
        }
        setCloseClickListener {
            dismiss()
        }
        btnSave?.setOnClickListener {
            onSaved?.invoke(budget?.textFieldInput?.text.toString().removeCommaRawString(), position)
            dismiss()
        }
        if (fromDetail) {
            budget?.textFieldInput?.setText(dailyBudget)
        } else if (fromCreate) {
            budget?.textFieldInput?.setText(keywordBudget)
        } else if (suggestedBid.toIntOrZero() < minBid.toIntOrZero()) {
            budget?.textFieldInput?.setText(minBid)
        } else {
            budget?.textFieldInput?.setText(suggestedBid)
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
        private const val FROM_DETAIL = "fromDetail"
        private const val FROM_CREATE = "fromCreate"
        private const val FROM_REKOMENDASI = "fromRekomendasi"
        private const val DAILY_BUDGET = "daily_budget"
        private const val KEYWORD_BUDGET = "keywordBudget"

        fun createInstance(data: Bundle): TopAdsEditKeywordBidSheet {
            return TopAdsEditKeywordBidSheet().apply {
                arguments = data
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        getDatafromArguments()
        if (!fromDetail) {
            setDefaultValues()
        } else {
            showHeader = true
            showCloseIcon = true
            isKeyboardOverlap = false
        }
        initChildLayout()
        super.onCreate(savedInstanceState)

    }

    private fun setDefaultValues() {
        showHeader = false
        showCloseIcon = false
        isKeyboardOverlap = false
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_common_edit_key_bid_sheet, null)
        setChild(contentView)
        dailybudget_description = contentView?.findViewById(R.id.dailybudget_description)
        budget = contentView?.findViewById(R.id.budget)
        min_suggested_bid = contentView?.findViewById(R.id.min_suggested_bid)
        btnSave = contentView?.findViewById(R.id.btnSave)
    }
}