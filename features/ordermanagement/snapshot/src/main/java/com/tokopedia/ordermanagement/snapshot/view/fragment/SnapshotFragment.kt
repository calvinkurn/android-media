package com.tokopedia.ordermanagement.snapshot.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.ordermanagement.snapshot.R
import com.tokopedia.ordermanagement.snapshot.ui.main.SnapshotViewModel

class SnapshotFragment : Fragment() {

    companion object {
        fun newInstance() = SnapshotFragment()
    }

    private lateinit var viewModel: SnapshotViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.snapshot_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SnapshotViewModel::class.java)
        // TODO: Use the ViewModel
    }

}