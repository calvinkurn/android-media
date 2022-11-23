package com.tokopedia.privacycenter.dsar.ui

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.common.utils.formatDateLocalTimezone
import com.tokopedia.privacycenter.databinding.FragmentDsarSuccessBinding

class DsarSuccessActivity: BaseActivity() {

    private lateinit var binding: FragmentDsarSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDsarSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupViews()
    }

    private fun setupViews() {
        val email = intent.getStringExtra(EXTRA_EMAIL)
        val date = intent.getStringExtra(EXTRA_DEADLINE) ?: ""
        binding.imgSuccess.loadImage(SUCCESS_IMG)
        binding.txtSuccessDescription.text = "Kami bakal kabari lewat e-mail $email maks. ${date.formatDateLocalTimezone()}.\nSilahkan cek secara berkala, ya!"
        binding.btnSuccess.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val SUCCESS_IMG = "https://images.tokopedia.net/img/android/accounts/privacycenter/request_success.png"
        const val EXTRA_EMAIL = "email"
        const val EXTRA_DEADLINE = "deadline"
    }
}
