package com.tokopedia.contactus.resolution.view

import com.tokopedia.imageassets.TokopediaImageUrl

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.contactus.R
import com.tokopedia.contactus.databinding.FragmentResolutionSuccessBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding
import java.util.*

class ResolutionSuccessFragment : TkpdBaseV4Fragment() {

    private val binding: FragmentResolutionSuccessBinding? by viewBinding()
    private var url: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resolution_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        setToolbar()
    }

    private fun setToolbar() {
        activity?.let {
            (it as ResolutionSuccessActivity).supportActionBar?.title = getString(R.string.title_toolbar_resolution)
        }
    }

    private fun initView() {
        binding?.ivSuccessReso?.loadImage(URL_IMAGE)

        binding?.btnGoToDetail?.setOnClickListener {
            routeToDetail()
        }

        binding?.btnGoToHome?.setOnClickListener {
            routeToHome()
        }
    }

    private fun initData() {
        url = arguments?.getString(KEY_URL) ?: ""
    }

    private fun routeToDetail() {
        if (url.isNotEmpty()) {
            activity?.let {
                RouteManager.route(
                    it,
                    String.format(Locale.getDefault(), "%s?url=%s", ApplinkConst.WEBVIEW, url)
                )
                it.finish()
            }
        }
    }

    private fun routeToHome() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intent)
        }
    }

    override fun getScreenName(): String {
        return getString(R.string.screen_name_resolution) ?: ""
    }

    companion object {
        private const val KEY_URL = "url"
        private const val URL_IMAGE = TokopediaImageUrl.RESOLUTION_URL_IMAGE

        fun createNewInstance(url: String): Fragment {
            return ResolutionSuccessFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_URL, url)
                }
            }
        }
    }
}
