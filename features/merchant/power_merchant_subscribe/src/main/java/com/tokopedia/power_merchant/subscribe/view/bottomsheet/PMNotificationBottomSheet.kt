package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.bottom_sheet_pm_notification.view.*

class PMNotificationBottomSheet : BaseBottomSheet() {

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_IMAGE_URL = "extra_image_url"
        private const val TAG: String = "PowerMerchantNotificationBottomSheet"

        fun createInstance(
                title: String,
                description: String,
                imgUrl: String
        ): PMNotificationBottomSheet {
            return PMNotificationBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_TITLE, title)
                bundle.putString(EXTRA_DESCRIPTION, description)
                bundle.putString(EXTRA_IMAGE_URL, imgUrl)
                arguments = bundle
            }
        }
    }

    private var primaryCtaText: String? = null
    private var secondaryCtaText: String? = null
    private var primaryCtaClickListener: (() -> Unit)? = null
    private var secondaryCtaClickListener: (() -> Unit)? = null

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_notification

    override fun setupView() = childView?.run {
        setupPrimaryCta()
        setupSecondaryCta()

        val title = arguments?.getString(EXTRA_TITLE).orEmpty()
        val description = arguments?.getString(EXTRA_DESCRIPTION).orEmpty()
        val imageUrl = arguments?.getString(EXTRA_IMAGE_URL).orEmpty()
        textTitle.text = title
        textDescription.text = description.parseAsHtml()
        imagePmNotification.loadImageWithoutPlaceholder(imageUrl)
    }

    private fun setupPrimaryCta() = childView?.run {
        btnPrimaryCta.text = primaryCtaText
        btnPrimaryCta.setOnClickListener { primaryCtaClickListener?.invoke() }
    }

    private fun setupSecondaryCta() = childView?.run {
        if (secondaryCtaText.isNullOrBlank()) {
            btnSecondaryCta.gone()
            return@run
        } else {
            btnSecondaryCta.visible()
        }

        btnSecondaryCta.text = secondaryCtaText
        btnSecondaryCta.setOnClickListener { secondaryCtaClickListener?.invoke() }
    }

    fun setPrimaryButtonClickListener(ctaText: String, listener: () -> Unit) {
        this.primaryCtaText = ctaText
        primaryCtaClickListener = listener
    }

    fun setSecondaryCtaClickListener(ctaText: String?, listener: () -> Unit) {
        this.secondaryCtaText = ctaText
        this.secondaryCtaClickListener = listener
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }
}