package com.tokopedia.topads.auto.base

import android.arch.lifecycle.ViewModel
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Author errysuprayogi on 09,May,2019
 */
abstract class BaseBindingFragment<V : ViewModel, D : ViewDataBinding> : BaseDaggerFragment() {


    lateinit var viewModel: V

    lateinit var dataBinding: D

    abstract fun getViewModel(): Class<V>

    @get:LayoutRes
    protected abstract val layoutRes: Int


    abstract fun setupView(view: View?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        setupView(dataBinding.root)
        return dataBinding.root
    }
}
