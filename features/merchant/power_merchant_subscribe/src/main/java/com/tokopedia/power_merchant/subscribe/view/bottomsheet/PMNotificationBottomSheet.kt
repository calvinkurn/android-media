package com.tokopedia.power_merchant.subscribe.view.bottomsheet

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.parseAsHtml
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

    private var primaryBtnText: String? = null
    private var primaryBtnClickListener: (() -> Unit)? = null

    override fun getChildResLayout(): Int = R.layout.bottom_sheet_pm_notification

    override fun setupView() = childView?.run {
        val title = arguments?.getString(EXTRA_TITLE).orEmpty()
        val description = arguments?.getString(EXTRA_DESCRIPTION).orEmpty()
        val imageUrl = arguments?.getString(EXTRA_IMAGE_URL).orEmpty()

        setupCTAButton()

        textTitle.text = title
        textDescription.text = description.parseAsHtml()
        imagePmNotification.loadImageWithoutPlaceholder(imageUrl)
    }

    private fun setupCTAButton() = childView?.run {
        btnPrimaryCTA.text = primaryBtnText
        btnPrimaryCTA.setOnClickListener { primaryBtnClickListener?.invoke() }
    }

    fun setPrimaryButtonClickListener(ctaText: String, listener: () -> Unit) {
        this.primaryBtnText = ctaText
        primaryBtnClickListener = listener
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }
}