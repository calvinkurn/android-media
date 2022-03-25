package com.tokopedia.tokofood.example

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokofood.common.presentation.view.BaseTokofoodFragment
import com.tokopedia.tokofood.common.util.Result
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentLivedataInputAndTextaBinding
import com.tokopedia.tokofood.example.FragmentB.Companion.ARGUMENT_INPUT_KEY
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class FragmentA : BaseTokofoodFragment() {

    private lateinit var binding: FragmentLivedataInputAndTextaBinding

    private var doubleTapExit = false

    override fun getFragmentToolbar(): Toolbar {
        return binding.toolbar
    }

    override fun getFragmentTitle(): String {
        return "A"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLivedataInputAndTextaBinding.inflate(inflater)
        binding.et1.isSaveEnabled = false
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonGoToB.setOnClickListener {
            navigateToNewFragment(FragmentB())
        }
        binding.buttonGoToBDeeplink.setOnClickListener {
            RouteManager.route(requireContext(),
                "tokopedia-android-internal://tokofood/b?$ARGUMENT_INPUT_KEY=abc"
            )
        }
        binding.buttonGoToBDeeplinkInternal.setOnClickListener {
            TokofoodRouteManager.routePrioritizeInternal(requireContext(),
                "tokopedia-android-internal://tokofood/b?$ARGUMENT_INPUT_KEY=abc"
            )
        }
        binding.et1.addTextChangedListener(object : TextWatcher {
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

    override fun onFragmentBackPressed(): Boolean {
        return if (doubleTapExit) {
            activity?.finish()
            true
        } else {
            doubleTapExit = true
            try {
                Toast.makeText(activity, "Yakin Keluar?", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch {
                    delay(2000)
                    doubleTapExit = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            true
        }
    }
}