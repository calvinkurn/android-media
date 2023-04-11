package com.tokopedia.tokopedianow.searchcategory.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tokopedianow.searchcategory.presentation.bottomsheet.TokoNowProductFeedbackBottomSheet
import dagger.Component

@SearchCategoryScope
@Component(
    modules = [AddFeedbackViewModelModule::class,
        ContextModule::class,
        UserSessionModule::class],
    dependencies = [BaseAppComponent::class]
)
interface SearchCategoryComponent {
    fun inject(addFeedbackBottomSheet: TokoNowProductFeedbackBottomSheet)
}
