package com.tokopedia.product.addedit.detail.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.IconUnify.Companion.INFORMATION
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TITLE_VALIDATION_ERROR
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TITLE_VALIDATION_SUCCESS
import com.tokopedia.product.addedit.detail.presentation.customview.TitleCorrectionView
import com.tokopedia.product.addedit.detail.presentation.model.TitleValidationModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.Typography.Companion.BOLD

class TitleValidationBottomSheet(
        private var titleValidationModel: TitleValidationModel = TitleValidationModel()
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Title Validation Bottom Sheet"
    }

    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(getString(R.string.title_product_title_validation_bottomsheet))
        titleValidationModel.apply {
            val displayError = isBlacklistKeyword || isNegativeKeyword || isTypoDetected
            setupTitleCorrection(displayError)
            setupHeaderViews(displayError)
            setupPoints(isBlacklistKeyword, isNegativeKeyword, isTypoDetected)
        }
        setupButton()
    }

    private fun setupTitleCorrection(displayError: Boolean) {
        val tvTitleCorrection: TitleCorrectionView? = contentView?.findViewById(R.id.tvTitleCorrection)
        val titleValidationSuccess: Typography? = contentView?.findViewById(R.id.titleValidationSuccess)

        titleValidationModel.apply {
            tvTitleCorrection?.setTitleAndKeywords(title, errorKeywords, warningKeywords)
        }
        tvTitleCorrection?.isVisible = displayError
        titleValidationSuccess?.isVisible = !displayError
    }

    private fun setupButton() {
        contentView?.findViewById<UnifyButton>(R.id.btnDismiss)?.setOnClickListener {
            dismiss()
        }
    }

    private fun setupHeaderViews(displayError: Boolean) {
        val ivValidationSuccess: ImageView? = contentView?.findViewById(R.id.ivValidationSuccess)
        val ivValidationError: ImageView? = contentView?.findViewById(R.id.ivValidationError)
        val tvErrorDescription: Typography? = contentView?.findViewById(R.id.tvErrorDescription)

        if (displayError) {
            ivValidationError?.loadImage(PHOTO_TITLE_VALIDATION_ERROR)
            ivValidationSuccess?.gone()
        } else {
            ivValidationSuccess?.loadImage(PHOTO_TITLE_VALIDATION_SUCCESS)
            ivValidationError?.gone()
            tvErrorDescription?.gone()
        }
    }

    private fun setupPoints(blacklistKeyword: Boolean, negativeKeyword: Boolean, typoDetected: Boolean) {
        val iconPoint1: IconUnify? = contentView?.findViewById(R.id.iconPoint1)
        val iconPoint2: IconUnify? = contentView?.findViewById(R.id.iconPoint2)
        val iconPoint3: IconUnify? = contentView?.findViewById(R.id.iconPoint3)
        val textPoint1: Typography? = contentView?.findViewById(R.id.textPoint1)
        val textPoint2: Typography? = contentView?.findViewById(R.id.textPoint2)
        val textPoint3: Typography? = contentView?.findViewById(R.id.textPoint3)
        val redColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500_96)
        val yellowColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Y300)
        val blackColor = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)

        if (blacklistKeyword) {
            iconPoint1?.setImage(INFORMATION, redColor)
            textPoint1?.setWeight(BOLD)
            textPoint1?.setTextColor(redColor)
        }

        if (negativeKeyword) {
            iconPoint2?.setImage(INFORMATION, yellowColor)
            textPoint2?.setWeight(BOLD)
        }

        if (typoDetected) {
            iconPoint3?.setImage(INFORMATION, blackColor)
            textPoint3?.setWeight(BOLD)
        }
    }

    private fun initChildLayout() {
        setCloseClickListener {
            dismiss()
        }
        overlayClickDismiss = true
        contentView = View.inflate(context, R.layout.add_edit_product_title_validation_bottomsheet_content, null)
        setChild(contentView)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
        }
    }
}