package com.tokopedia.thankyou_native.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IdRes
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.R

abstract class BaseThankYouPageFragment : BaseDaggerFragment() {

    lateinit var rootView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.thank_fragment_base, container, false)
        val mainContentView = onCreateView(inflater)
        mainContentView?.let {
            rootView.findViewById<FrameLayout>(R.id.mainContainer).addView(mainContentView)
        }
        return rootView
    }

    abstract fun onCreateView(inflater: LayoutInflater): View?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProductBottomSheet()
    }

    fun showProductBottomSheet() {

    }
}