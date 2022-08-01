package com.tokopedia.resolution

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOperational
import com.tokopedia.resolution.databinding.ActivityResolutionSuccessBinding
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.webview.KEY_URL

class ResolutionSuccessActivity : AppCompatActivity() {

    private val binding: ActivityResolutionSuccessBinding? by viewBinding()
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resolution_success)
        initData()

        binding?.btnGoToDetail?.setOnClickListener {
            routeToDetail()
        }

        binding?.btnGoToHome?.setOnClickListener {
            routeToHome()
        }


    }

    private fun initData() {
        url = intent.data?.getQueryParameter(KEY_URL) ?: ""
    }

    private fun routeToDetail() {
        if (url.isNotEmpty()) {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalOperational.DETAIL_COMPLAIN)
            intent.putExtra(KEY_URL, url)
            startActivity(intent)
        }
    }

    private fun routeToHome() {
        this.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
        }
    }


}