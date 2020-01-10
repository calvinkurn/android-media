package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.interfaces.BannerListener
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_discovery.view.*

class DiscoveryFragment : Fragment() {
    private lateinit var discoveryViewModel: DiscoveryViewModel
    private lateinit var discoveryRecycleAdapter: DiscoveryRecycleAdapter
   // var bannerLister : BannerListener = this

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
        view.discovery_recyclerView.layoutManager = LinearLayoutManager(activity)
        discoveryRecycleAdapter = DiscoveryRecycleAdapter(this)
        view.discovery_recyclerView.adapter = discoveryRecycleAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discoveryViewModel.getDiscoveryData()
        setUpObserver()
    }

    private fun setUpObserver() {
        discoveryViewModel.getDiscoveryResponse().observe(this, Observer {
            when (it) {
                is Success -> {
                    val list = ArrayList<ComponentsItem>()

                    //RouteManager.route(this, "tokopedia://discovery/160")
                    it.data.components?.get(0)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }


//        RouteManager.route(this, "tokopedia://discovery/test-disco")
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
                    discoveryRecycleAdapter.setDataList(list)
                }
            }
        })
    }
}