package com.tokopedia.report.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.report.R
import com.tokopedia.report.view.fragment.ReportInputDetailFragment

class ReportInputDetailActivity : BaseSimpleActivity() {
    private var fragment = ReportInputDetailFragment()

    override fun getNewFragment(): Fragment {
        fragment = ReportInputDetailFragment.createInstance(intent.getIntExtra(ARG_MIN_CHAR, -1),
                intent.getIntExtra(ARG_MAX_CHAR, -1), intent.getStringExtra(ARG_VALUE) ?: "")
        return fragment
    }

    override fun isShowCloseButton(): Boolean = true

    companion object{
        private const val ARG_MIN_CHAR = "arg_min_char"
        private const val ARG_MAX_CHAR = "arg_max_char"
        private const val ARG_VALUE = "arg_value"

        fun createIntent(context: Context, value: String, minChar: Int, maxChar: Int): Intent =
                Intent(context, ReportInputDetailActivity::class.java)
                        .putExtra(ARG_MIN_CHAR, minChar).putExtra(ARG_MAX_CHAR, maxChar).putExtra(ARG_VALUE, value)
    }

    override fun onBackPressed() {
        fragment.sendInputResult()
        super.onBackPressed()

    }
}