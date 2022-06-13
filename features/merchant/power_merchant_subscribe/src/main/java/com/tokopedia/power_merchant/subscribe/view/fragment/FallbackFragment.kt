package com.tokopedia.power_merchant.subscribe.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.media.loader.loadImage
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by @ilhamsuaib on 09/06/22.
 */

class FallbackFragment : Fragment() {

    companion object {
        private const val IMG_FALLBACK_PAGE_URL =
            "https://images.tokopedia.net/img/android/power_merchant_subscribe/pm_fallback_page_update_app.png"
        private const val SELLER_APP_PACKAGE = "com.tokopedia.sellerapp"
        private const val SELLER_APP_PLAY_STORE =
            "https://play.google.com/store/apps/details?id=$SELLER_APP_PACKAGE"

        fun newInstance(): FallbackFragment {
            return FallbackFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fallback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    private fun setupView() {
        view?.run {
            findViewById<ImageUnify>(R.id.imgPmFallbackPage)
                .loadImage(IMG_FALLBACK_PAGE_URL)

            findViewById<UnifyButton>(R.id.btnPmUpdateApp)
                .setOnClickListener {
                    openPlayStore()
                }
        }
    }

    private fun openPlayStore() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(SELLER_APP_PLAY_STORE)))
    }
}