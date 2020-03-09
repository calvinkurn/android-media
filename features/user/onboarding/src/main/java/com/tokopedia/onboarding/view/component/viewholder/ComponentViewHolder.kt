package com.tokopedia.onboarding.view.component.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.view.component.uimodel.ButtonUiModel
import com.tokopedia.onboarding.view.component.uimodel.ImageUiModel
import com.tokopedia.onboarding.view.component.uimodel.TitleUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils

class ComponentViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val title = itemView.findViewById<Typography>(R.id.texTitlePageDynamicOnbaording)
    private val image = itemView.findViewById<ImageView>(R.id.imagePageDynamicOnbaording)
    private val button = itemView.findViewById<UnifyButton>(R.id.buttonPageDynamicOnbaording)

    fun bindTitle(textDataModel: TitleUiModel) {
        title.apply {
            text = textDataModel.text
            setVisible(textDataModel.visibility)
        }
    }

    fun bindImage(imageDataModel: ImageUiModel) {
        image?.apply {
            setVisible(imageDataModel.visibility)
        }

        ImageUtils.loadImage(
                imageView = image,
                url = imageDataModel.imageUrl
        )
    }

    fun bindButton(buttonDataModel: ButtonUiModel) {
        button?.apply {
            text = buttonDataModel.text
            setVisible(buttonDataModel.visibility)
        }
    }

    private fun View.setVisible(isVisible: Boolean) {
        if (isVisible) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.GONE
        }
    }
}