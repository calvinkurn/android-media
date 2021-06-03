package com.tokopedia.analyticsdebugger.cassava.validator.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.di.CassavaComponentInstance
import com.tokopedia.analyticsdebugger.cassava.validator.core.GtmLogUi
import com.tokopedia.analyticsdebugger.cassava.validator.core.Validator
import com.tokopedia.analyticsdebugger.cassava.validator.core.toJson
import timber.log.Timber
import javax.inject.Inject

class MainValidatorFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val testPath: String by lazy {
        arguments?.getString(ARGUMENT_TEST_PATH)
                ?: throw IllegalArgumentException("Path not found!!")
    }

    private val isFromNetwork: Boolean by lazy {
        arguments?.getBoolean(ARGUMENT_IS_NETWORK)
                ?: true
    }

    val viewModel: ValidatorViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory)
                .get(ValidatorViewModel::class.java)
    }

    private val mAdapter: ValidatorResultAdapter by lazy {
        val itemAdapter = ValidatorResultAdapter { goToDetail(it) }
        itemAdapter
    }
    private var callback: Listener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        viewModel.fetchQueryFromAsset(testPath, isFromNetwork)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_validator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.findViewById<RecyclerView>(R.id.rv)) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = mAdapter
        }

        viewModel.testCases.observe(viewLifecycleOwner, Observer<List<Validator>> {
            Timber.d("Validator got ${it.size}")
            mAdapter.setData(it)
        })

        viewModel.cassavaQuery.observe(viewLifecycleOwner, Observer {
            if (it.readme != null) {
                view.findViewById<View>(R.id.cv_readme).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.tv_readme).text = it.readme
            } else {
                view.findViewById<View>(R.id.cv_readme).visibility = View.GONE
            }
            viewModel.run(it.query, it.mode.value)
        })
    }

    fun setCallback(callback: Listener) {
        this.callback = callback
    }

    private fun goToDetail(item: Validator) {
        val exp = item.data.toJson()
        val act = item.matches

        callback?.goDetail(exp, act)
    }

    private fun initInjector() {
        activity?.let {
            CassavaComponentInstance.getInstance(it).inject(this)
        }
    }

    interface Listener {
        fun goDetail(expected: String, actual: List<GtmLogUi>)
    }

    companion object {

        private const val ARGUMENT_TEST_PATH = "ARGUMENT_TEST_PATH"
        private const val ARGUMENT_IS_NETWORK = "ARGUMENT_IS_NETWORK"

        fun newInstance(path: String, isFromNetwork: Boolean): MainValidatorFragment =
                MainValidatorFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARGUMENT_TEST_PATH, path)
                        putBoolean(ARGUMENT_IS_NETWORK, isFromNetwork)
                    }
                }
    }

}