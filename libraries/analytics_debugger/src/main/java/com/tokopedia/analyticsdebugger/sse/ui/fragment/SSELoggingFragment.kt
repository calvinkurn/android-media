package com.tokopedia.analyticsdebugger.sse.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.sse.di.DaggerSSELoggingComponent
import com.tokopedia.analyticsdebugger.sse.ui.viewmodel.SSELoggingViewModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 09, 2021
 */
class SSELoggingFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: SSELoggingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjection()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, viewModelFactory).get(SSELoggingViewModel::class.java)
        return inflater.inflate(R.layout.fragment_sse_logging, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        viewModel.getLog()
    }

    private fun initInjection() {
        val component = DaggerSSELoggingComponent.builder().baseAppComponent(
            (requireActivity().application as BaseMainApplication).baseAppComponent
        ).build()
        component.inject(this)
    }

    private fun initObserver() {
        viewModel.observableSSELog.observe(viewLifecycleOwner) {
            it.forEach {
                Log.d("<LOG>", it.toString())
            }
        }
    }

    companion object {

        @JvmStatic
        val TAG = SSELoggingFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): Fragment {
            return SSELoggingFragment()
        }
    }
}