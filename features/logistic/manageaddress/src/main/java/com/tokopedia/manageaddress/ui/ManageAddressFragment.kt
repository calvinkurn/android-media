package com.tokopedia.manageaddress.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.manageaddress.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.SearchInputView

class ManageAddressFragment : BaseDaggerFragment(), SearchInputView.Listener{

    private lateinit var searchAddress: SearchInputView
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manage_address, container, false)
        searchAddress = view.findViewById(R.id.search_input_view)
        return view
    }

    override fun onSearchSubmitted(text: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    private fun openSoftKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                searchAddress.searchTextView, InputMethodManager.SHOW_IMPLICIT)
    }

}