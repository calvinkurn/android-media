package com.tokopedia.analyticsdebugger.validator.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tokopedia.analyticsdebugger.R

class ValidatorDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_validator_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exp = arguments?.getString(EXTRA_EXPECTED)
        val act = arguments?.getString(EXTRA_ACTUAL)

        view.findViewById<TextView>(R.id.tv_expected).text = exp
        view.findViewById<TextView>(R.id.tv_actual).text = act
    }

    companion object {
        private const val EXTRA_EXPECTED = "EXTRA_EXPECTED"
        private const val EXTRA_ACTUAL = "EXTRA_ACTUAL"

        fun newIntent(expected: String, actual: String) =
                ValidatorDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_EXPECTED, expected)
                        putString(EXTRA_ACTUAL, actual)
                    }
                }
    }
}
