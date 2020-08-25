package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.text.watcher.NumberTextWatcher
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.topads_common_edit_key_bid_sheet.*
import kotlin.math.max

/**
 * Created by Pika on 21/7/20.
 */

class TopAdsEditKeywordBidSheet : BottomSheetUnify() {

    private var contentView: View? = null
    private var minBid = 0
    private var maxBid = 0
    private var suggestedBid = 0
    private var position = 0
    private var type = 11
    private var name = ""
    var onSaved: ((bid: String, type: Int, pos: Int) -> Unit)? = null
    var onDelete: ((pos: Int) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDatafromArguments()
        initView()
        budget.textFieldInput.addTextChangedListener(object : NumberTextWatcher(budget.textFieldInput, "0") {
            override fun onNumberChanged(number: Double) {
                super.onNumberChanged(number)
                val result = number.toInt()
                when {
                    result < minBid -> {
                        setMessageErrorField(getString(R.string.min_bid_error), minBid, true)
                    }
                    result > maxBid -> {
                        setMessageErrorField(getString(R.string.max_bid_error), maxBid, true)
                    }

                    result % 50 != 0 -> {
                        setMessageErrorField(getString(R.string.topads_common_error_multiple_50), 50, true)
                    }

                    else -> {
                        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestedBid, false)
                        btnSave.isEnabled = true
                    }
                }
            }
        })
    }

    fun getSelectedSortId(): Int {
        return when (dialog?.radio_group?.checkedRadioButtonId) {
            R.id.title_1 -> BROAD_POSITIVE
            R.id.title_2 -> EXACT_POSITIVE
            else -> BROAD_POSITIVE
        }
    }

    private fun getDatafromArguments() {
        minBid = arguments?.getInt(MIN_BID) ?: 0
        maxBid = arguments?.getInt(MAX_BID) ?: 0
        position = arguments?.getInt(ITEM_POSITION) ?: 0
        suggestedBid = arguments?.getInt(SUGGESTION_BID) ?: 0
        type = arguments?.getInt(CURRENT_KEY_TYPE) ?: 11
        name = arguments?.getString(KEYWORD_NAME)?:""

    }

    private fun setMessageErrorField(error: String, bid: Int, bool: Boolean) {
        budget.setError(bool)
        budget.setMessage(String.format(error, bid))
        if(bool)
            btnSave.isEnabled = false
    }

    private fun initView() {

        setCloseClickListener {
            dismiss()
        }
        btnDeleteKeyword?.setOnClickListener {
            dismiss()
            onDelete?.invoke(position)
        }

        btnSave.setOnClickListener {
            onSaved?.invoke(budget.textFieldInput.text.toString(), getSelectedSortId(), position)
            dismiss()
        }

        btn_close.setOnClickListener {
            dismiss()
        }
        if (type == EXACT_POSITIVE) {
            title_2.isChecked = true
        } else
            title_1.isChecked = true

        budget.textFieldInput.setText(suggestedBid.toString())
        setMessageErrorField(getString(R.string.recommendated_bid_message), suggestedBid, false)
        title?.text = name
    }

    companion object {

        private const val MAX_BID = "max"
        private const val MIN_BID = "min"
        private const val SUGGESTION_BID = "suggest"
        private const val KEYWORD_NAME = "keywordName"
        private const val CURRENT_KEY_TYPE = "currentKeyType"
        private const val ITEM_POSITION = "pos"
        private const val SPECIFIC_TYPE = "Spesifik"
        private const val BROAD_TYPE = "Luas"
        private const val EXACT_POSITIVE = 21
        private const val BROAD_POSITIVE = 11

        fun createInstance(data: Bundle): TopAdsEditKeywordBidSheet {
            return TopAdsEditKeywordBidSheet().apply {
                arguments = data
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        contentView = View.inflate(context, R.layout.topads_common_edit_key_bid_sheet, null)
        showHeader = false
        showCloseIcon = false
        setChild(contentView)
    }
}