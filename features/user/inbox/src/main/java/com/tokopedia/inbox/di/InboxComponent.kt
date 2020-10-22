package com.tokopedia.inbox.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.inbox.view.dialog.AccountSwitcherBottomSheet
import dagger.Component

@InboxScope
@Component(
        modules = [InboxCommonModule::class],
        dependencies = [BaseAppComponent::class]
)
interface InboxComponent {
    fun inject(fragment: AccountSwitcherBottomSheet)
}