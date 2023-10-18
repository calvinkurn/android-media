package com.tokopedia.promocheckoutmarketplace.presentation.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.FragmentManager
import com.tokopedia.promocheckoutmarketplace.databinding.LayoutBottomsheetBoInfoBinding
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.BoInfoBottomSheetUiModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.htmltags.HtmlUtil

fun showBoInfoBottomSheet(
    fragmentManager: FragmentManager,
    context: Context,
    uiData: BoInfoBottomSheetUiModel.UiData
) {
    BottomSheetUnify().apply {
        val binding = LayoutBottomsheetBoInfoBinding.inflate(LayoutInflater.from(context))

        showCloseIcon = true
        showHeader = true

        setTitle(uiData.title)
        with(binding) {
            imageBanner.setImageUrl(uiData.imageUrl)
            contentTitle.text = uiData.contentTitle
            contentDescription.text = HtmlUtil.fromHtml(uiData.contentDescription).trim()
            buttonAction.text = uiData.buttonText
            buttonAction.setOnClickListener {
                dismiss()
            }
        }

        setChild(binding.root)
        show(fragmentManager, "BO Info")
    }
}
