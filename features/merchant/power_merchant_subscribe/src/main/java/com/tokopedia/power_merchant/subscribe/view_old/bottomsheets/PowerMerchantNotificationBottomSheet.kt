package com.tokopedia.power_merchant.subscribe.view_old.bottomsheets

import android.os.Bundle
import android.view.View
import android.view.View.inflate
import androidx.annotation.DrawableRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_power_merchant_notification.*

class PowerMerchantNotificationBottomSheet: BottomSheetUnify() {

    companion object {
        private const val EXTRA_TITLE = "extra_title"
        private const val EXTRA_DESCRIPTION = "extra_description"
        private const val EXTRA_IMAGE_ID = "extra_image_id"
        private const val EXTRA_CTA_MODE = "extra_cta_mode"
        private val TAG: String = PowerMerchantNotificationBottomSheet::class.java.simpleName

        fun createInstance(
            title: String,
            description: String,
            @DrawableRes imageId: Int,
            ctaMode: CTAMode
        ): PowerMerchantNotificationBottomSheet {
            return PowerMerchantNotificationBottomSheet().apply {
                val bundle = Bundle()
                bundle.putString(EXTRA_TITLE, title)
                bundle.putString(EXTRA_DESCRIPTION, description)
                bundle.putInt(EXTRA_IMAGE_ID, imageId)
                bundle.putString(EXTRA_CTA_MODE, ctaMode.name)
                arguments = bundle
            }
        }
    }

    private var primaryBtnText: String? = null
    private var secondaryBtnText: String? = null
    private var primaryBtnClickListener: (() -> Unit)? = null
    private var secondaryBtnClickListener: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val itemView = inflate(context,
            R.layout.bottom_sheet_power_merchant_notification, null)

        setChild(itemView)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.PmBottomSheet)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString(EXTRA_TITLE).orEmpty()
        val description = arguments?.getString(EXTRA_DESCRIPTION).orEmpty()
        val imageId = arguments?.getInt(EXTRA_IMAGE_ID).orZero()
        val ctaMode = arguments?.getString(EXTRA_CTA_MODE).orEmpty()

        showTitle(title)
        showDescription(description)
        setupCTAButton(ctaMode)
        showImage(imageId)
    }

    private fun showTitle(title: String) {
        textTitle.text = title
    }

    private fun showDescription(description: String) {
        textDescription.text = description
    }

    private fun setupCTAButton(ctaMode: String) {
        when(ctaMode) {
            CTAMode.SINGLE.name -> {
                btnPrimaryCTA.show()
                btnSecondaryCTA.hide()
            }
            CTAMode.DOUBLE.name -> {
                btnPrimaryCTA.show()
                btnSecondaryCTA.show()
            }
        }

        btnPrimaryCTA.text = primaryBtnText
        btnPrimaryCTA.setOnClickListener { primaryBtnClickListener?.invoke() }

        btnSecondaryCTA.text = secondaryBtnText
        btnSecondaryCTA.setOnClickListener { secondaryBtnClickListener?.invoke() }
    }

    private fun showImage(imageId: Int) {
        ImageHandler.loadImageWithId(image, imageId)
    }

    fun setPrimaryButtonClickListener(listener: () -> Unit) {
        primaryBtnClickListener = listener
    }

    fun setSecondaryButtonClickListener(listener: () -> Unit) {
        secondaryBtnClickListener = listener
    }

    fun setPrimaryButtonText(text: String) {
        primaryBtnText = text
    }

    fun setSecondaryButtonText(text: String) {
        secondaryBtnText = text
    }

    fun show(fm: FragmentManager?) {
        fm?.let { show(it, TAG) }
    }

    enum class CTAMode {
        SINGLE,
        DOUBLE
    }
}