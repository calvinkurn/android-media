package com.tokopedia.onboarding.view.component.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.domain.model.ButtonDataModel
import com.tokopedia.onboarding.view.component.uimodel.ButtonUiModel
import com.tokopedia.onboarding.view.component.uimodel.ImageUiModel
import com.tokopedia.onboarding.view.component.uimodel.OnboardingUiModel
import com.tokopedia.onboarding.view.component.viewholder.ComponentViewHolder

class ImageAdapterDelegate: TypedAdapterDelegate<ImageUiModel, OnboardingUiModel, ComponentViewHolder>(R.layout.item_image_onboarding) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ComponentViewHolder {
        return ComponentViewHolder(basicView)
    }

    override fun onBindViewHolder(item: ImageUiModel, holder: ComponentViewHolder) {
        holder.bindImage(item)
    }
}