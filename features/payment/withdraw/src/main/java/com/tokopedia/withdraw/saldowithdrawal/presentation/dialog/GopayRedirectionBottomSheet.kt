package com.tokopedia.withdraw.saldowithdrawal.presentation.dialog

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.appaidl.data.CUSTOMER_APP
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.isAppInstalled
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.withdraw.R

class GopayRedirectionBottomSheet: BottomSheetUnify() {

    private lateinit var image: String
    private lateinit var title: String
    private lateinit var description: String
    private lateinit var applink: String
    private lateinit var childView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            childView = LayoutInflater.from(context).inflate(R.layout.swd_dialog_gopay_redirection_layout,
                null, false)

            setChild(childView)
            setUpView()

            image = it.getString(ARG_IMAGE, "")
            title = it.getString(ARG_TITLE, "")
            description = it.getString(ARG_DESCRIPTION, "")
            applink = it.getString(ARG_APPLINK, "")
        }
    }

    private fun setUpView() {
        val imageView = childView.findViewById<ImageUnify>(R.id.ivGopayRedirection)
        val titleView = childView.findViewById<Typography>(R.id.tvGopayRedirectionTitle)
        val descriptionView = childView.findViewById<Typography>(R.id.tvGopayRedirectionDescription)
        val ctaView = childView.findViewById<UnifyButton>(R.id.btnGopayRedirectionCta)

        imageView.setImageUrl(image)
        titleView.shouldShowWithAction(title.isNotEmpty()) {
            titleView.text = title
        }
        descriptionView.shouldShowWithAction(description.isNotEmpty()) {
            descriptionView.text = description
        }
        ctaView.setOnClickListener {
            context?.let {
                if (it.isAppInstalled(CUSTOMER_APP)) {
                    RouteManager.route(it, applink)
                } else {
                    goToTkpdPlayStore()
                }
            }
        }
    }

    private fun goToTkpdPlayStore() {
        activity?.let {
            val uri = Uri.parse(TKPD_MARKET_APPLINK)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                it.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                it.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(PLAYSTORE_URL)
                    )
                )
            }
        }
    }

    companion object {
        const val TAG = "gopay_redirection_tag"
        private const val TKPD_MARKET_APPLINK = "market://details?id=$CUSTOMER_APP"
        private const val PLAYSTORE_URL = "https://play.google.com/store/apps/details?id=$CUSTOMER_APP"
        private const val ARG_IMAGE = "arg_image"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESCRIPTION = "arg_description"
        private const val ARG_APPLINK = "arg_applink"

        fun getInstance(
            image: String,
            title: String,
            description: String,
            applink: String,
        ): GopayRedirectionBottomSheet {
            return GopayRedirectionBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(ARG_IMAGE, image)
                bundle.putString(ARG_TITLE, title)
                bundle.putString(ARG_DESCRIPTION, description)
                bundle.putString(ARG_APPLINK, applink)
                arguments = bundle
            }
        }
    }
}
