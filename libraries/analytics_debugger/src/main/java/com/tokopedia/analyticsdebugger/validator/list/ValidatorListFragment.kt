package com.tokopedia.analyticsdebugger.validator.list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.validator.Utils
import timber.log.Timber

class ValidatorListFragment : Fragment() {

    private var listener: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_validator_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listTests = Utils.listAssetFiles(context!!, "tracker")
        Timber.d("List files: %s", listTests)

        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val listingAdapter = FileListingAdapter()

        with(rv) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = listingAdapter
        }

        val searchBarTextField = view.findViewById<EditText>(R.id.searchbar_textfield)
        val searchBarClearButton = view.findViewById<ImageButton>(R.id.searchbar_clear)
        with(searchBarTextField) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearSearchBarFocus(this)
                    true
                } else false
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    s?.run {
                        searchBarClearButton.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
                        // Filter test cases based on search query
                        val filteredListTests = listTests.filter { it.contains(s.toString()) }
                        listingAdapter.setItems(filteredListTests)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    /* do nothing */
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    /* do nothing */
                }

            })
        }
        searchBarClearButton.setOnClickListener {
            searchBarTextField.text.clear()
            searchBarClearButton.visibility = View.GONE
            clearSearchBarFocus(searchBarTextField)
            listingAdapter.setItems(listTests)
        }

        listingAdapter.setOnItemClickListener { listener?.runTest(it) }
        listingAdapter.setItems(listTests)
    }

    private fun clearSearchBarFocus(editText: EditText) {
        editText.clearFocus()
        val input = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        input?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun runTest(filepath: String)
    }

    companion object {
        fun newInstance(): ValidatorListFragment = ValidatorListFragment()
    }
}