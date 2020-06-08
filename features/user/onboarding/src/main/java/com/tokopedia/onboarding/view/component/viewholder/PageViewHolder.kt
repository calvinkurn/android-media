package com.tokopedia.onboarding.view.component.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.domain.model.PageDataModel
import com.tokopedia.onboarding.view.adapter.ComponentAdapter
import com.tokopedia.onboarding.view.component.uimodel.ButtonUiModel
import com.tokopedia.onboarding.view.component.uimodel.ImageUiModel
import com.tokopedia.onboarding.view.component.uimodel.OnboardingUiModel
import com.tokopedia.onboarding.view.component.uimodel.TitleUiModel

class PageViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val componentAdapter = ComponentAdapter()
    private val recycleListViewComponent = itemView.findViewById<RecyclerView>(R.id.listComponentRecyclerView)

    init {
        recycleListViewComponent.apply {
            adapter = componentAdapter
        }
    }

    fun bind(page: PageDataModel) {
        page.componentsDataModel.let {
            val onboardings: MutableList<OnboardingUiModel> = mutableListOf()

            it.textDataModel.let { textDataModel ->
                onboardings.add(TitleUiModel(
                        componentLevel = textDataModel.componentLevel,
                        text = textDataModel.text,
                        textColor = textDataModel.textColor,
                        visibility = textDataModel.visibility
                ))
            }

            it.imageDataModel.let { imageDataModel ->
                onboardings.add(ImageUiModel(
                        componentLevel = imageDataModel.componentLevel,
                        imageUrl = imageDataModel.imageUrl,
                        animationUrl = imageDataModel.animationUrl,
                        visibility = imageDataModel.visibility
                ))
            }

            it.buttonDataModel.let { buttonDataModel ->
                onboardings.add(ButtonUiModel(
                        componentLevel = buttonDataModel.componentLevel,
                        text = buttonDataModel.text,
                        appLink = buttonDataModel.appLink,
                        visibility = buttonDataModel.visibility
                ))
            }

            componentAdapter.addComponent(onboardings.sortedBy { data -> data.position() })
        }
    }
}