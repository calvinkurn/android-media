package com.tokopedia.tokofood.example

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.tokofood.databinding.FragmentLivedataInputAndTextbBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class FragmentB : BaseMultiFragment() {

    private lateinit var binding: FragmentLivedataInputAndTextbBinding

    private var parent: HasViewModel<MultipleFragmentsViewModel>? = null

    val activityViewModel: MultipleFragmentsViewModel?
        get() = parent?.viewModel()

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
            val input = arguments?.getString("input") ?: ""
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

    override fun onAttachActivity(context: Context?) {
        super.onAttachActivity(context)
        parent = activity as? HasViewModel<MultipleFragmentsViewModel>
    }
}