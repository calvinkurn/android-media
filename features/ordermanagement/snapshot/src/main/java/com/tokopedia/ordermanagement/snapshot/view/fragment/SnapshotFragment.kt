package com.tokopedia.ordermanagement.snapshot.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.data.model.SnapshotTypeData
import com.tokopedia.ordermanagement.snapshot.ui.main.SnapshotViewModel
import com.tokopedia.ordermanagement.snapshot.util.SnapshotConsts
import com.tokopedia.ordermanagement.snapshot.view.adapter.SnapshotAdapter

class SnapshotFragment : Fragment() {

    private lateinit var snapshotAdapter: SnapshotAdapter
    private var rv: RecyclerView? = null

    companion object {
        fun newInstance() = SnapshotFragment()
    }

    private lateinit var viewModel: SnapshotViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val contentView = inflater.inflate(R.layout.snapshot_fragment, container, false)
        rv = contentView.findViewById(R.id.rv_snapshot)
        return contentView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SnapshotViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareLayout()
        renderPage()
    }

    private fun prepareLayout() {
        snapshotAdapter = SnapshotAdapter()
        rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = snapshotAdapter
        }
    }

    private fun renderPage() {
        val listPage = arrayListOf<SnapshotTypeData>()
        listPage.add(SnapshotTypeData(1, SnapshotConsts.TYPE_HEADER))
        listPage.add(SnapshotTypeData("a", SnapshotConsts.TYPE_INFO))
        listPage.add(SnapshotTypeData("b", SnapshotConsts.TYPE_SHOP))
        listPage.add(SnapshotTypeData("c", SnapshotConsts.TYPE_DETAILS))
        snapshotAdapter.addList(listPage)
    }
}