package com.tokopedia.report.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.report.R
import kotlinx.android.synthetic.main.fragment_input_report_detail.*

class ReportInputDetailFragment : BaseDaggerFragment() {
    private var minChar = -1
    private var maxChar = -1
    private var value = ""

    override fun getScreenName(): String? = null

    override fun initInjector() {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_input_report_detail, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            minChar = it.getInt(ARG_MIN_CHAR)
            maxChar = it.getInt(ARG_MAX_CHAR)
            value = it.getString(ARG_VALUE, "")
        }

        input.hint = getString(R.string.product_hint_product_report, minChar.toString())
        input.setText(value)

        if (minChar == -1 && maxChar == -1){
            btn_cont.isEnabled = true
        } else {
            input.filters = arrayOf(InputFilter.LengthFilter(maxChar))
            btn_cont.isEnabled = !value.isNotBlank()
            input.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    btn_cont.isEnabled = (!p0.isNullOrEmpty() && p0.length in minChar..maxChar)
                }
            })
        }

        btn_cont.setOnClickListener {
            val intent = Intent().putExtra(INPUT_VALUE, input.text.toString())
            activity?.run {
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }

    fun sendInputResult() {
        if (input.text.toString().length >= maxChar){
            val intent = Intent().putExtra(INPUT_VALUE, input.text.toString())
            activity?.run {
                setResult(Activity.RESULT_OK, intent)
            }
        }
    }

    companion object{
        private const val ARG_MIN_CHAR = "arg_min_char"
        private const val ARG_MAX_CHAR = "arg_max_char"
        private const val ARG_VALUE = "arg_value"

        const val INPUT_VALUE = "input_value"

        fun createInstance(minChar: Int, maxChar: Int, value: String) = ReportInputDetailFragment().also {
            fragment -> fragment.arguments = Bundle().apply {
                putInt(ARG_MAX_CHAR, maxChar)
                putInt(ARG_MIN_CHAR, minChar)
                putString(ARG_VALUE, value)
        } }
    }
}