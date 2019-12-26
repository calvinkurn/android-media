package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.BaseDataModel
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_discovery.view.*

class DiscoveryFragment : Fragment() {

    private lateinit var dataList: ArrayList<BaseDataModel>
    private lateinit var discoveryViewModel: DiscoveryViewModel

    companion object {
        fun getInstance(endPoint: String?): Fragment {
            val bundle = Bundle()
            val fragment = DiscoveryFragment()
            if (!endPoint.isNullOrEmpty()) {
                bundle.putString(END_POINT, endPoint)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_discovery, container, false)
        initView(view)
        discoveryViewModel = (activity as DiscoveryActivity).getViewModel()
        discoveryViewModel.pageIdentifier = arguments?.getString(END_POINT, "") ?: ""
        return view
    }

    private fun initView(view: View) {
        dataList = ArrayList()
        view.discovery_recyclerView.layoutManager = LinearLayoutManager(activity)
        val discoveryRecycleAdapter = DiscoveryRecycleAdapter()
        view.discovery_recyclerView.adapter = discoveryRecycleAdapter
        discoveryRecycleAdapter.setDataList(dataList)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discoveryViewModel.viewCreated()
        setUpObserver()
    }

    private fun setUpObserver() {
        (activity as DiscoveryActivity).getViewModel().getDiscoveryResponse().observe(this, Observer {
            when (it) {
                is Success -> {
//                    discovery_data.text = it.toString()
                }
            }
        })
    }


}