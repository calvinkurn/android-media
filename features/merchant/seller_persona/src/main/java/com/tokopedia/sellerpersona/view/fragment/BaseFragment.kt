package com.tokopedia.sellerpersona.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tokopedia.sellerpersona.di.SellerPersonaComponent
import com.tokopedia.sellerpersona.view.activity.SellerPersonaActivity

/**
 * Created by @ilhamsuaib on 17/01/23.
 */

abstract class BaseFragment<T : ViewBinding> : Fragment() {

    protected var binding: T? = null
    protected val daggerComponent: SellerPersonaComponent? by lazy {
        return@lazy (activity as? SellerPersonaActivity)?.component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewBinding = bind(layoutInflater, container)
        this.binding = viewBinding
        return viewBinding.root
    }

    abstract fun inject()

    abstract fun bind(layoutInflater: LayoutInflater, container: ViewGroup?): T
}