package com.tokopedia.gm.common.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.gm.common.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By @ilhamsuaib on 08/04/21
 */

class SimpleInterruptBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "GmcSimpleInterruptBottomSheet"

        fun createInstance(showKnob: Boolean): SimpleInterruptBottomSheet {
            return SimpleInterruptBottomSheet().apply {
                clearContentPadding = true
                showHeader = !showKnob
                showCloseIcon = !showKnob
                isHideable = showKnob
                isDragable = showKnob
                this@apply.showKnob = showKnob
                isSkipCollapseState = showKnob
            }
        }
    }

    private var mTitle: String = ""
    private var description: String = ""
    private var imgIllustration: String? = null
    private var ctaText: String = ""
    private var ctaAction: (() -> Unit)? = null

    override fun getResLayout(): Int = R.layout.bottom_sheet_gmc_pm_simple_interrupt

    override fun setupView() = childView?.run {
        findViewById<Typography>(R.id.tvGmcSimpleInterruptTitle).text = mTitle
        findViewById<Typography>(R.id.tvGmcSimpleInterruptDescription).text = description
        findViewById<UnifyButton>(R.id.btnGmcSimpleInterrupt).let {
            it.text = ctaText
            it.setOnClickListener {
                ctaAction?.invoke()
                dismiss()
            }
        }
        findViewById<ImageUnify>(R.id.imgGmcSimpleInterrupt).let {
            val imgUrl = imgIllustration
            if (imgUrl.isNullOrBlank()) {
                setMargin(0, 0, 0, 0)
                requestLayout()
                it.gone()
            } else {
                it.visible()
                it.loadImage(imgUrl)
            }
        }
    }

    fun setContent(title: String, description: String, img: String? = null): SimpleInterruptBottomSheet {
        this.mTitle = title
        this.description = description
        this.imgIllustration = img
        return this
    }

    fun setOnCtaClickListener(ctaText: String, action: () -> Unit): SimpleInterruptBottomSheet {
        this.ctaText = ctaText
        this.ctaAction = action
        return this
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}