package com.tokopedia.inbox.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.inbox.view.activity.InboxActivity
import com.tokopedia.inbox.view.dialog.AccountSwitcherBottomSheet
import dagger.Component

@InboxScope
@Component(
        modules = [
            InboxCommonModule::class,
            InboxViewModelModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface InboxComponent {
    fun inject(fragment: AccountSwitcherBottomSheet)
    fun inject(fragment: InboxActivity)
}