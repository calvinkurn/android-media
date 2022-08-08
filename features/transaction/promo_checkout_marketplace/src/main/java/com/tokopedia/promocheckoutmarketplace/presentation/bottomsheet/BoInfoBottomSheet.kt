package com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet

import android.content.Context
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.promocheckoutmarketplace.databinding.LayoutBottomsheetBoInfoBinding
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.BoInfoBottomSheetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.htmltags.HtmlUtil

fun showBoPromoBottomSheet(
    fragmentManager: FragmentManager,
    context: Context,
    uiModel: BoInfoBottomSheetUiModel
) {
    BottomSheetUnify().apply {
        val binding = LayoutBottomsheetBoInfoBinding.inflate(LayoutInflater.from(context))

        showCloseIcon = true
        showHeader = true

        setTitle(uiModel.title)
        with(binding) {
            imageBanner.setImageUrl(uiModel.imageUrl)
            contentTitle.text = uiModel.contentTitle
            contentDescription.text = HtmlUtil.fromHtml(uiModel.contentDescription).trim()
            contentDescription.movementMethod = LinkMovementMethod.getInstance()
            buttonAction.text = uiModel.buttonText
            buttonAction.setOnClickListener {
                dismiss()
            }
        }

        setChild(binding.root)
        show(fragmentManager, "BO Promo")
    }
}