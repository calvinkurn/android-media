package com.tokopedia.catalog.model.util

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog.R
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.unifycomponents.SearchBarUnify

object CatalogUtil {

    fun shareData(context: Context?, shareTxt: String?, productUri: String?) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, shareTxt + "\n" + productUri)
        context?.startActivity(Intent.createChooser(share, shareTxt))
    }

    fun getSortFilterAnalytics(searchFilterMap : HashMap<String,String>?) : String{
        var label = ""
        searchFilterMap?.forEach { map ->
            label = "$label&${map.key}=${map.value}"
        }
        return label.removePrefix("&")
    }

    fun getShareURI(catalogUrl: String): String {
        return  if (!catalogUrl.contains(CatalogConstant.WWW_DOT_TEXT))
            catalogUrl.replace(CatalogConstant.HTTPS_TEXT, CatalogConstant.HTTPS_WWW_DOT_TEXT)
        else
            catalogUrl
    }

    fun getShareUrl(catalogId : String) : String{
        return "${CatalogConstant.CATALOG_DEEPLINK_PREFIX}$catalogId"
    }

    fun getImagesFromCatalogImages(catalogImages: ArrayList<CatalogImage>?): ArrayList<String>? {
        if(catalogImages == null || catalogImages.isEmpty())
            return null
        val imagesArray = arrayListOf<String>()
        catalogImages.forEach {
            if(!it.imageURL.isNullOrBlank())
                imagesArray.add(it.imageURL)
        }
        return imagesArray
    }

    fun getRatingString(rating : String?) : String {
        if(rating.isNullOrBlank()){
            return ""
        }
        return if(rating.length >= 3){
            rating.replace(".",",").substring(0,3)
        }else if(rating.length <= 2 && rating.isNotBlank()) {
            rating.substring(0,1)
        }else {
            ""
        }
    }

    fun setSearchListener(context: Context?, view: View, onSearchKeywordEntered: () -> Unit,
                                  onClearSearch : () -> Unit, onTapSearchBar : () -> Unit) {
        val searchbar = view.findViewById<SearchBarUnify>(R.id.catalog_product_search)
        searchbar.searchBarContainer.background = MethodChecker.getDrawable(context,
            com.tokopedia.catalog.R.drawable.catalog_search_bar_background)
        val searchTextField = searchbar?.searchBarTextField
        val searchClearButton = searchbar?.searchBarIcon
        searchTextField?.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchTextField?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(textView: TextView?, actionId: Int, even: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchKeywordEntered.invoke()
                    dismissKeyboard(context, view)
                    return true
                }
                return false
            }
        })

        searchTextField?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onSearchKeywordEntered.invoke()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchTextField?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus)
                onTapSearchBar.invoke()
        }

        searchClearButton?.setOnClickListener {
            searchTextField?.text?.clear()
            onClearSearch.invoke()
            dismissKeyboard(context, view)
        }
    }

    private fun dismissKeyboard(context: Context?, view: View?) {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        if (inputMethodManager?.isAcceptingText == true)
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}