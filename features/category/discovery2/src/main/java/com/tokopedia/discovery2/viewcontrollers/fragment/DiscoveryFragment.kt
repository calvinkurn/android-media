package com.tokopedia.discovery2.viewcontrollers.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity.Companion.END_POINT
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_discovery.*

class DiscoveryFragment : Fragment() {


    companion object{
        fun getInstance(intent: Intent):Fragment{
            val bundle = Bundle()
            val fragment = DiscoveryFragment()
            val endPoint = intent.data.lastPathSegment
            if (!endPoint.isNullOrEmpty()){
                bundle.putString(END_POINT,endPoint)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_discovery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as DiscoveryActivity).getViewModel().getDiscoveryData(arguments?.getString(END_POINT)?:"")
        setUpObserver()
    }

    private fun setUpObserver() {
        (activity as DiscoveryActivity).getViewModel().getDiscoveryResponse().observe(this, Observer {
            when (it) {
                is Success -> {
                    discovery_data.text = it.toString()
                }
            }
        })
    }


}