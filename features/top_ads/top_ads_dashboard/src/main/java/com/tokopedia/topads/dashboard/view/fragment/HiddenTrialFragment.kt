package com.tokopedia.topads.dashboard.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EXPIRE
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HiddenTrialFragment : TkpdBaseV4Fragment() {

    private var date: MutableLiveData<String> = MutableLiveData()
    private var desc: TextView? = null
    val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    companion object {
        fun newInstance(extras: Bundle?): HiddenTrialFragment {
            val fragment = HiddenTrialFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun getScreenName(): String {
        return HiddenTrialFragment::class.java.name
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_dashboard_hidden_trial_layout), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desc = view.findViewById(R.id.desc_1)
        val date: String = outputFormat.format(inputFormat.parse(arguments?.getString(EXPIRE)))
        val text1 = getString(R.string.hidden_trial_desc1)
        val text2 = text1.removeRange(text1.length - 13, text1.length)
        val text = "$text2 [$date]."
        val spannableString = SpannableString(text)
        spannableString.setSpan(ForegroundColorSpan(Color.BLACK), text2.length, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        desc?.text = spannableString
    }

}