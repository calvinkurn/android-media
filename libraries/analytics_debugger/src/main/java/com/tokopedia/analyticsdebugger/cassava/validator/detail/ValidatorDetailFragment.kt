package com.tokopedia.analyticsdebugger.cassava.validator.detail

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analyticsdebugger.R
import com.tokopedia.analyticsdebugger.cassava.validator.core.GtmLogUi
import java.util.ArrayList

class ValidatorDetailFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_validator_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val exp = arguments?.getString(EXTRA_EXPECTED)
        val actuals: List<GtmLogUi> = arguments?.getParcelableArrayList<GtmLogUi>(EXTRA_ACTUAL)?.toList() ?: emptyList()

        view.findViewById<TextView>(R.id.tv_expected).text = exp
        view.findViewById<TextView>(R.id.tv_found_number).text = "Found ${actuals.size}"

        with(view.findViewById<RecyclerView>(R.id.rv)) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = ValidatorDetailAdapter(actuals)
        }
    }

    companion object {
        private const val EXTRA_EXPECTED = "EXTRA_EXPECTED"
        private const val EXTRA_ACTUAL = "EXTRA_ACTUAL"

        fun newIntent(expected: String, actual: List<GtmLogUi>) =
                ValidatorDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(EXTRA_EXPECTED, expected)
                        putParcelableArrayList(EXTRA_ACTUAL, actual as? ArrayList<out Parcelable>)
                    }
                }
    }
}
