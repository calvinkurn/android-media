package com.tokopedia.analyticsdebugger.validator.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    }
}