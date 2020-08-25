package com.tokopedia.topads.edit.utils

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.tokopedia.topads.edit.R
import com.tokopedia.unifycomponents.SearchBarUnify
import java.text.NumberFormat
import java.util.*

object Constants {

    const val FAVOURED_DATA = "favouredData"
    const val SELECTED_DATA = "selectedData"
    const val ORIGINAL_LIST = "originalList"
    const val PRODUCT_ID = "product"
    const val MIN_SUGGESTION = "minSuggestedBid"
    const val GROUP_ID = "groupId"
    const val COUNT = 50
    const val SELECTED_KEYWORD = "selectKeywords"
    const val RESTORED_DATA = "restoreData"
    const val CURRENTLIST = "currentKeywords"
    const val RESULT_PRICE = "resultPrice"
    const val RESULT_PROUCT = "resultProduct"
    const val RESULT_NAME = "resultName"
    const val RESULT_IMAGE = "resultImage"
    const val ALL = "all"
    const val ROW = 50
    const val EXISTING_IDS = "ExistingIds"
    const val POSITION0 = 0
    const val POSITION1 = 1
    const val POSITIVE_CREATE = "createdPositiveKeyword"
    const val POSITIVE_DELETE = "deletedPositiveKeyword"
    const val POSITIVE_EDIT = "editedPositiveKeyword"
    const val NEGATIVE_KEYWORDS_ADDED = "negative_keywords_added"
    const val NEGATIVE_KEYWORDS_DELETED = "negative_keywords_deleted"
    const val GROUP_NAME = "group_name"
    const val PRICE_BID = "price_bid"
    const val DAILY_BUDGET = "daily_budget"
    const val NAME_EDIT = "isNameEdit"
    const val MULTIPLIER = 40
    const val GROUPKEY = "groupName"
    const val PRODUCT = "product"
    const val BUDGET_LIMITED = "isBudgetLimited"
    const val KEYWORD_EXISTS = 1
    const val KEYWORD_TYPE_PHRASE = 11
    const val KEYWORD_TYPE_EXACT = 21
    const val KEYWORD_TYPE_NEGATIVE_PHRASE = 12
    const val KEYWORD_TYPE_NEGATIVE_EXACT = 22
    const val REQUEST_OK = 1
    const val ADDED_PRODUCTS = "addedProducts"
    const val DELETED_PRODUCTS = "deletedProducts"
    const val KALI = " kali"
    const val PRODUK_NAME = " Produk"
    const val KATA_KUNCI = " Kata Kunci"
    const val ATUR_NAME = " Atur"
    const val TITLE_1 = "Pencarian luas"
    const val TITLE_2 = "Pencarian Spesifik"
    const val REGEX = "^[A-Za-z0-9 ]*\$"
    const val EDIT_SOURCE = "dashboard_edit_group"
    const val ACTION_EDIT = "edit"
    const val ACTION_ADD = "add"
    const val ACTION_REMOVE = "remove"
    const val ACTION_CREATE = "create"
    const val ACTION_DELETE = "delete"
    const val PUBLISHED = "published"
    const val POSTIVE_PHRASE = "positive_phrase"
    const val POSITIVE_SPECIFIC = "positive_exact"
    const val NEGATIVE_PHRASE = "negative_phrase"
    const val NEGATIVE_SPECIFIC = "negative_exact"
    const val ACTIVE = "active"
    const val KEYWORD_SOURCE = "es"
    const val INPUT = "input"
    const val DEBOUNCE_CONST: Long = 200
    const val MULTIPLY_CONST = 50
    const val groupId = "groupId"
    const val priceBid = "price_bid"
    const val priceDaily = "price_daily"
    const val groupName = "groupName"
    const val TAB_POSITION = "tab_position"
    const val MAX_BID = "max"
    const val MIN_BID = "min"
    const val SUGGESTION_BID = "suggest"
    const val KEYWORD_NAME = "keywordName"
    const val CURRENT_KEY_TYPE = "currentKeyType"
    const val ITEM_POSITION = "pos"
    const val SPECIFIC_TYPE = "Spesifik"
    const val BROAD_TYPE = "Luas"
    const val EXACT_POSITIVE = 21
    const val BROAD_POSITIVE = 11


    var locale = Locale("in", "ID")


    fun convertToCurrencyString(value: Long): String {
        return (NumberFormat.getNumberInstance(locale).format(value) + KALI)
    }


    fun dismissKeyboard(context: Context?, view: View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager?.isAcceptingText == true)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun setSearchListener(context: Context?, view: View, onSuccess: () -> Unit) {
        val searchbar = view.findViewById<SearchBarUnify>(R.id.searchBar)
        val searchTextField = searchbar?.searchBarTextField
        val searchClearButton = searchbar?.searchBarIcon
        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {

            override fun onEditorAction(textView: TextView?, actionId: Int, even: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSuccess.invoke()
                    dismissKeyboard(context, view)
                    return true
                }
                return false
            }
        })
        searchClearButton?.setOnClickListener {
            searchTextField?.text?.clear()
            onSuccess.invoke()
            dismissKeyboard(context, view)
        }
    }

}
