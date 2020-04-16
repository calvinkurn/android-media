package com.tokopedia.onboarding.view.component.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.domain.model.ButtonDataModel
import com.tokopedia.onboarding.view.component.uimodel.ButtonUiModel
import com.tokopedia.onboarding.view.component.uimodel.OnboardingUiModel
import com.tokopedia.onboarding.view.component.viewholder.ComponentViewHolder

class ButtonAdapterDelegate: TypedAdapterDelegate<ButtonUiModel, OnboardingUiModel, ComponentViewHolder>(R.layout.item_button_onboarding) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ComponentViewHolder {
        return ComponentViewHolder(basicView)
    }

    override fun onBindViewHolder(item: ButtonUiModel, holder: ComponentViewHolder) {
        holder.bindButton(item)
    }
}