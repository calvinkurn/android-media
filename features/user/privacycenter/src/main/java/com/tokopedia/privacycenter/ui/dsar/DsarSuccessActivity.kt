package com.tokopedia.privacycenter.ui.dsar

import android.os.Bundle
import android.text.Html
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.FragmentDsarSuccessBinding
import com.tokopedia.utils.date.DateUtil

class DsarSuccessActivity : BaseActivity() {

    private var binding: FragmentDsarSuccessBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDsarSuccessBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()
        setupViews()
    }

    private fun setupViews() {
        val email = intent.getStringExtra(EXTRA_EMAIL)
        val date = intent.getStringExtra(EXTRA_DEADLINE) ?: ""
        binding?.imgSuccess?.loadImage(getString(R.string.dsar_request_success_illustration))
        val formattedDate = DateUtil.formatDate(
            DateUtil.YYYY_MM_DD_T_HH_MM_SS_SSS_Z,
            DateUtil.DEFAULT_VIEW_FORMAT,
            date
        )
        binding?.txtSuccessDescription?.text = Html.fromHtml(getString(R.string.dsar_progress_description, email, formattedDate))
        binding?.btnSuccess?.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val EXTRA_EMAIL = "email"
        const val EXTRA_DEADLINE = "deadline"
    }
}
