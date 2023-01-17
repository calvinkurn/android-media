package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

abstract class BaseFragment<T: ViewBinding> : Fragment() {

    protected var binding: T? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = bind(layoutInflater, container)
        this.binding = viewBinding
        return viewBinding.root
    }

    abstract fun bind(layoutInflater: LayoutInflater, container: ViewGroup?): T
}