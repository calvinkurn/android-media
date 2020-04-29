package com.tokopedia.onboarding.view.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.onboarding.view.component.delegate.ButtonAdapterDelegate
import com.tokopedia.onboarding.view.component.delegate.ImageAdapterDelegate
import com.tokopedia.onboarding.view.component.delegate.TitleAdapterDelegate
import com.tokopedia.onboarding.view.component.uimodel.OnboardingUiModel

class ComponentAdapter : BaseAdapter<OnboardingUiModel>() {

    init {
        delegatesManager
                .addDelegate(TitleAdapterDelegate())
                .addDelegate(ImageAdapterDelegate())
                .addDelegate(ButtonAdapterDelegate())
    }

    fun addComponent(uiModels: List<OnboardingUiModel>) {
        addItems(uiModels)
    }
}