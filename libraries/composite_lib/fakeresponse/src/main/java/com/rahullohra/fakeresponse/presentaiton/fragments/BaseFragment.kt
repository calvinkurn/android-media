package com.rahullohra.fakeresponse.presentaiton.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open abstract class BaseFragment : Fragment() {

    open fun getLayout(): Int {
        return 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(getLayout(), container, false)
        initVars()
        initViews(v)
        setupFragment()
        return v
    }

    open fun initVars(){}
    open fun initViews(view: View){}
    open fun setupFragment(){}
}