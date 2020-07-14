package com.tokopedia.onboarding.view.component.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.view.component.uimodel.OnboardingUiModel
import com.tokopedia.onboarding.view.component.uimodel.TitleUiModel
import com.tokopedia.onboarding.view.component.viewholder.ComponentViewHolder

class TitleAdapterDelegate: TypedAdapterDelegate<TitleUiModel, OnboardingUiModel, ComponentViewHolder>(R.layout.item_title_onboarding) {

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ComponentViewHolder {
        return ComponentViewHolder(basicView)
    }

    override fun onBindViewHolder(item: TitleUiModel, holder: ComponentViewHolder) {
        holder.bindTitle(item)
    }
}