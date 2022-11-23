package com.tokopedia.campaign.components.bottomsheet.rbac

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.campaign.databinding.CampaignCommonBottomsheetIneligibleAccessWarningBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable

class IneligibleAccessWarningBottomSheet : BottomSheetUnify() {

    companion object {
        private const val DEFAULT_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/fs-tkpd/ic_ineligible_access.png"

        @JvmStatic
        fun newInstance() = IneligibleAccessWarningBottomSheet()
    }

    data class Widget(
        val imageView: ImageUnify,
        val titleTypography: Typography,
        val descriptionTypography: Typography,
        val button: UnifyButton
    )

    private var binding by autoClearedNullable<CampaignCommonBottomsheetIneligibleAccessWarningBinding>()
    private var onButtonClicked : () -> Unit = {}
    private var customAppearance: Widget.() -> Unit = {}

    init {
        clearContentPadding = true
        isSkipCollapseState = true
        isKeyboardOverlap = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBottomSheet(inflater, container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setupBottomSheet(inflater: LayoutInflater, container: ViewGroup?) {
        binding = CampaignCommonBottomsheetIneligibleAccessWarningBinding.inflate(inflater, container, false)
        setChild(binding?.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding?.run {
            imageViewIneligibleAccess.loadImage(DEFAULT_IMAGE_URL)
            btnReadEducationArticle.setOnClickListener {
                onButtonClicked()
                dismiss()
            }

            val widget = Widget(imageViewIneligibleAccess, tpgTitle, tpgDescription, btnReadEducationArticle)
            customAppearance.invoke(widget)
        }
    }

    fun setOnButtonClicked(onButtonClicked : () -> Unit = {}) {
        this.onButtonClicked = onButtonClicked
    }

    fun setCustomAppearance(customAppearance: Widget.() -> Unit) {
        this.customAppearance = customAppearance
    }

}
