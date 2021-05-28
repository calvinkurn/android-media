package com.tokopedia.product.addedit.detail.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TITLE_VALIDATION_ERROR
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants.PHOTO_TITLE_VALIDATION_SUCCESS
import com.tokopedia.product.addedit.detail.presentation.customview.TitleCorrectionView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class TitleValidationBottomSheet(
        val title: String = "",
        val errorKeywords: List<String> = emptyList(),
        val warningKeywords: List<String> = emptyList()
): BottomSheetUnify() {

    companion object {
        const val TAG = "Tag Title Validation Bottom Sheet"
    }

    var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle(getString(R.string.title_product_title_validation_bottomsheet))
        setupImageView()
        setupTitleCorrection()
        setupButton()
    }

    private fun setupTitleCorrection() {
        val tvTitleCorrection: TitleCorrectionView? = contentView?.findViewById(R.id.tvTitleCorrection)
        tvTitleCorrection?.setTitleAndKeywords(title, errorKeywords, warningKeywords)
    }

    private fun setupButton() {
        contentView?.findViewById<UnifyButton>(R.id.btnDismiss)?.setOnClickListener {
            dismiss()
        }
    }

    private fun setupImageView() {
        val ivValidationSuccess: ImageView? = contentView?.findViewById(R.id.ivValidationSuccess)
        val ivValidationError: ImageView? = contentView?.findViewById(R.id.ivValidationError)
        ivValidationSuccess?.loadImage(PHOTO_TITLE_VALIDATION_SUCCESS)
        ivValidationError?.loadImage(PHOTO_TITLE_VALIDATION_ERROR)
    }

    fun show(manager: FragmentManager?) {
        manager?.run {
            super.show(this , TAG)
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
}