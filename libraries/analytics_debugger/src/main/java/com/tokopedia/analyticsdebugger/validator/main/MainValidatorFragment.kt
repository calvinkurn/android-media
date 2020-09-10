package com.tokopedia.analyticsdebugger.validator.main

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
import com.tokopedia.analyticsdebugger.validator.Utils
import com.tokopedia.analyticsdebugger.validator.core.GtmLogUi
import com.tokopedia.analyticsdebugger.validator.core.Validator
import com.tokopedia.analyticsdebugger.validator.core.toJson
import com.tokopedia.analyticsdebugger.validator.core.toJsonMap
import timber.log.Timber

class MainValidatorFragment : Fragment() {

    val viewModel: ValidatorViewModel by lazy {
        activity?.application?.let {
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(it))
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
        var jsonTest: String = ""
        val testPath = arguments?.getString(ARGUMENT_TEST_PATH)
        context?.let {
            jsonTest = Utils.getJsonDataFromAsset(it, testPath!!) ?: ""
        }
        Timber.d("Validator Json Query \n %s", jsonTest)

        val testQuery = jsonTest.toJsonMap()
        Timber.d("Validator Test Query Map \n %s", testQuery)

        val readme = testQuery["readme"]
        if (readme != null && readme is String && !readme.isEmpty()) {
            view.findViewById<View>(R.id.cv_readme).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.tv_readme).text = readme
        } else {
            view.findViewById<View>(R.id.cv_readme).visibility = View.GONE
        }

        viewModel.testCases.observe(this, Observer<List<Validator>> {
            Timber.d("Validator got ${it.size}")
            mAdapter.setData(it)
        })
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        val mode = testQuery["mode"] as? String

        val value = testQuery["query"]
        viewModel.run(value as List<Map<String, Any>>, mode ?: "exact")
        with(rv) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = mAdapter
        }
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