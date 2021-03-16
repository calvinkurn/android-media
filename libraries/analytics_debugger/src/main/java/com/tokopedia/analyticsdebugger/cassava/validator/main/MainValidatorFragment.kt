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
import com.tokopedia.analyticsdebugger.cassava.validator.Utils
import com.tokopedia.analyticsdebugger.cassava.validator.core.*
import timber.log.Timber

class MainValidatorFragment : Fragment() {

    private val testPath: String by lazy {
        arguments?.getString(ARGUMENT_TEST_PATH)
                ?: throw IllegalArgumentException("Path not found!!")
    }
    private val query by lazy {
        Utils.getJsonDataFromAsset(requireContext(), testPath)?.toCassavaQuery()
                ?: throw QueryTestParseException()
    }

    val viewModel: ValidatorViewModel by lazy {
        activity?.application?.let {
            ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(it))
                    .get(ValidatorViewModel::class.java)
        } ?: throw IllegalArgumentException("Requires activity, fragment should be attached")
    }

    private val mAdapter: ValidatorResultAdapter by lazy {
        val itemAdapter = ValidatorResultAdapter { goToDetail(it) }
        itemAdapter
    }
    private var callback: Listener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main_validator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (query.readme != null) {
            view.findViewById<View>(R.id.cv_readme).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.tv_readme).text = query.readme
        } else {
            view.findViewById<View>(R.id.cv_readme).visibility = View.GONE
        }
        viewModel.run(query.query, query.mode.value)
        with(view.findViewById<RecyclerView>(R.id.rv)) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = mAdapter
        }

        viewModel.testCases.observe(viewLifecycleOwner, Observer<List<Validator>> {
            Timber.d("Validator got ${it.size}")
            mAdapter.setData(it)
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

    interface Listener {
        fun goDetail(expected: String, actual: List<GtmLogUi>)
    }

    companion object {

        private const val ARGUMENT_TEST_PATH = "ARGUMENT_TEST_PATH"

        fun newInstance(path: String): MainValidatorFragment = MainValidatorFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_TEST_PATH, path)
            }
        }
    }

}