package com.tokopedia.tokofood.example

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodFragment
import com.tokopedia.tokofood.common.util.Constant
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.databinding.FragmentLivedataInputAndTextbBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect

@FlowPreview
@ExperimentalCoroutinesApi
class FragmentB : BaseTokofoodFragment() {

    companion object {
        const val ARGUMENT_INPUT_KEY = "input"
    }

    private lateinit var binding: FragmentLivedataInputAndTextbBinding

    override fun onFragmentBackPressed(): Boolean {
        return false
    }

    override fun getFragmentToolbar(): Toolbar {
        return binding.toolbar
    }

    override fun getFragmentTitle(): String {
        return "B"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val data = arguments?.getString(Constant.DATA_KEY) ?: ""
            val uriData = Uri.parse(data)
            val input = uriData.getQueryParameter(ARGUMENT_INPUT_KEY)?: ""
            activityViewModel?.setInput(input)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLivedataInputAndTextbBinding.inflate(inflater)
        binding.et1.isSaveEnabled = false
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.et1.addTextChangedListener ( object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                activityViewModel?.setInput(s.toString())
            }
        })
        binding.et1.setText(activityViewModel?.getLatestInput())
        init()
    }

    fun init() {
        //lifecycleScope.launch{lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {} use after upgrade to lifecycle 2.4.0
        lifecycleScope.launchWhenResumed {
            activityViewModel?.outputFlow?.collect {
                when (it) {
                    is Result.Success -> {
                        binding.tvRes.text = it.data
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is Result.Failure -> {
                        binding.tvRes.text = it.error.toString()
                        binding.swipeRefresh.isRefreshing = false
                    }
                    is Result.Loading -> {
                        binding.tvRes.text = "Lagi Loading nih. Sabar.."
                    }
                }
                println("HENDRY 1 $it")
            }
        }

        lifecycleScope.launchWhenResumed {
            activityViewModel?.outputFlow?.collect {
                println("HENDRY 2 $it")
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            activityViewModel?.refresh()
        }
    }
}