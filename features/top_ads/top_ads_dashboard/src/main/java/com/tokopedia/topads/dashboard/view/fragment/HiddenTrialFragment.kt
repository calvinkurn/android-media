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
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.topads.dashboard.R
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.EXPIRE
import com.tokopedia.unifycomponents.ImageUnify
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class HiddenTrialFragment : TkpdBaseV4Fragment() {

    private var desc: TextView? = null
    private val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val outputFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())

    companion object {
        const val RANGE_LIMIT = 13
        fun newInstance(extras: Bundle?): HiddenTrialFragment {
            val fragment = HiddenTrialFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun getScreenName(): String {
        return HiddenTrialFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(resources.getLayout(R.layout.topads_dashboard_hidden_trial_layout),
            container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desc = view.findViewById(R.id.desc_1)
        view.findViewById<ImageUnify>(R.id.ic_ilustration)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.ill_iklan_otomatis))
        view.findViewById<ImageUnify>(R.id.icon1)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.ic_money))
        view.findViewById<ImageUnify>(R.id.icon2)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.ic_people))
        view.findViewById<ImageUnify>(R.id.icon3)
            ?.setImageDrawable(context?.getResDrawable(R.drawable.ic_product))

        val date: String = outputFormat.format(inputFormat.parse(arguments?.getString(EXPIRE)))
        val text1 = getString(R.string.hidden_trial_desc1)
        val text2 = text1.removeRange(text1.length - RANGE_LIMIT, text1.length)
        val text = "$text2 [$date]."
        val spannableString = SpannableString(text)
        spannableString.setSpan(ForegroundColorSpan(Color.BLACK),
            text2.length,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        desc?.text = spannableString
    }

}