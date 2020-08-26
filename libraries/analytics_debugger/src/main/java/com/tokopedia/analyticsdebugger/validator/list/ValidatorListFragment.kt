package com.tokopedia.analyticsdebugger.validator.list

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.kotlin.extensions.view.hideLoading
import com.tokopedia.unifycomponents.SearchBarUnify
import kotlinx.coroutines.*
import timber.log.Timber

class ValidatorListFragment : Fragment() {

    private var listener: Listener? = null

    private var searchInputJob: Job? = null
    private var searchTextChangeDelay: Long = DEFAULT_TEXT_CHANGE_DELAY_MILLIS

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

        val searchBar = view.findViewById<SearchBarUnify>(R.id.search_bar)
//        searchBar.searchBarTextField.setOnEditorActionListener { v, _, _ ->
//            searchBar.clearFocus()
//            val filteredListTests = listTests.filter { it.contains(v.text) }
//            listingAdapter.setItems(filteredListTests)
//            true
//        }
        searchBar.searchBarTextField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchInputJob?.cancel()
                searchInputJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(searchTextChangeDelay)
                    // Filter test cases based on search query
                    val filteredListTests = listTests.filter { it.contains(s.toString()) }
                    listingAdapter.setItems(filteredListTests)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        searchBar.clearListener = {
            listingAdapter.setItems(listTests)
        }

        listingAdapter.setOnItemClickListener { listener?.runTest(it) }
        listingAdapter.setItems(listTests)
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    interface Listener {
        fun runTest(filepath: String)
    }

    companion object {
        fun newInstance(): ValidatorListFragment = ValidatorListFragment()

        const val DEFAULT_TEXT_CHANGE_DELAY_MILLIS: Long = 500
    }
}