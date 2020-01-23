package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewmodel.DiscoveryViewModel
import com.tokopedia.usecase.coroutines.Success

class DiscoveryFragment : Fragment(), RecyclerView.OnChildAttachStateChangeListener {
    private lateinit var mDiscoveryViewModel: DiscoveryViewModel
    private lateinit var mDiscoveryRecycleAdapter: DiscoveryRecycleAdapter
    private lateinit var mRecyclerView: RecyclerView


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
        mDiscoveryViewModel = (activity as DiscoveryActivity).getViewModel()
        mDiscoveryViewModel.pageIdentifier = arguments?.getString(END_POINT, "") ?: ""
        return view
    }

    private fun initView(view: View) {
        mRecyclerView = view.findViewById(R.id.discovery_recyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(this)
        mRecyclerView.adapter = mDiscoveryRecycleAdapter
        mRecyclerView.addOnChildAttachStateChangeListener(this)
    }

    override fun onDetach() {
        mRecyclerView.removeOnChildAttachStateChangeListener(this)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDiscoveryViewModel.getDiscoveryData()
        setUpObserver()
    }

    private fun setUpObserver() {
        mDiscoveryViewModel.getDiscoveryResponse().observe(this, Observer {
            when (it) {
                is Success -> {
                    val list = ArrayList<ComponentsItem>()

                    //RouteManager.route(this, "tokopedia://discovery/160")
//                    it.data.components?.get(0)?.let { it1 -> list.add(it1) }
//                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }


//        RouteManager.route(this, "tokopedia://discovery/test-disco")
                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(10)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(10)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(5)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(3)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(10)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(2)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(4)?.let { it1 -> list.add(it1) }
                    it.data.components?.get(10)?.let { it1 -> list.add(it1) }

                    mDiscoveryRecycleAdapter.setDataList(list)
                }
            }
        })
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        (mRecyclerView.getChildViewHolder(view) as? AbstractViewHolder)?.onViewDetachedToWindow()
    }

    override fun onChildViewAttachedToWindow(view: View) {
        (mRecyclerView.getChildViewHolder(view) as? AbstractViewHolder)?.onViewAttachedToWindow()
    }
}