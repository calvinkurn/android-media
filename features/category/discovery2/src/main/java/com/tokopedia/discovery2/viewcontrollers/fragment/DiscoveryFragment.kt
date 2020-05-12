package com.tokopedia.discovery2.viewcontrollers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private lateinit var mPageComponentRecyclerView: RecyclerView
    var pageEndPoint = ""
    var last = false


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
        pageEndPoint = mDiscoveryViewModel.pageIdentifier
        return view
    }

    private fun initView(view: View) {
        mPageComponentRecyclerView = view.findViewById(R.id.discovery_recyclerView)
        mPageComponentRecyclerView.layoutManager = LinearLayoutManager(activity)
        mDiscoveryRecycleAdapter = DiscoveryRecycleAdapter(this)
        mPageComponentRecyclerView.adapter = mDiscoveryRecycleAdapter
        mPageComponentRecyclerView.addOnChildAttachStateChangeListener(this)
    }

    override fun onDetach() {
        mPageComponentRecyclerView.removeOnChildAttachStateChangeListener(this)
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mDiscoveryViewModel.getDiscoveryData()
        setUpObserver()
        mPageComponentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
                    last = true

                }
            }
        })
    }

    private fun setUpObserver() {
        mDiscoveryViewModel.getDiscoveryResponseList().observe(this, Observer {
            when (it) {
                is Success -> {
                    val list = arrayListOf<ComponentsItem>()
                    list.add(it.data[2])
                    mDiscoveryRecycleAdapter.setDataList(it.data)
                }
            }
        })
    }

    override fun onChildViewDetachedFromWindow(view: View) {
        (mPageComponentRecyclerView.getChildViewHolder(view) as? AbstractViewHolder)?.onViewDetachedToWindow()
    }

    override fun onChildViewAttachedToWindow(view: View) {
        (mPageComponentRecyclerView.getChildViewHolder(view) as? AbstractViewHolder)?.onViewAttachedToWindow()
    }
}