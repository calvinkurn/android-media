package com.tokopedia.tokofood.example

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.tokofood.base.ExampleTokofoodActivity
import com.tokopedia.tokofood.databinding.FragmentLivedataInputAndTextaBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class FragmentA : BaseMultiFragment() {

    private lateinit var binding: FragmentLivedataInputAndTextaBinding

    private var doubleTapExit = false

    val activityViewModel: MultipleFragmentsViewModel?
        get() = (activity as? ExampleTokofoodActivity)?.viewModel

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
            val f = FragmentB()
            (activity as? ExampleTokofoodActivity)?.navigateToNewFragment(f)
            // OR CAN ALSO BE LIKE BELOW
            // TokofoodRouteManager.routePrioritizeInternal(requireContext(), "tokopedia://tokofood/b")
        }
        binding.buttonGoToBDeeplink.setOnClickListener {
            RouteManager.route(requireContext(), "tokopedia://tokofood/b?input=abc")
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
        //lifecycleScope.launchWhenStarted can also be used, but wasteful.
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel?.outputFlow?.collect {
                    when (it) {
                        is Result.Success -> {
                            binding.tvRes.text = it.data
                        }
                        is Result.Failure -> {
                            binding.tvRes.text = it.error.toString()
                        }
                        is Result.Loading -> {
                            binding.tvRes.text = "Lagi Loading nih. Sabar.."
                        }
                    }
                    binding.swipeRefresh.isRefreshing = false
                    println("HENDRY 1 $it")
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel?.outputFlow?.collect {
                    println("HENDRY 2 $it")
                }
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