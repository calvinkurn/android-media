package com.tokopedia.developer_options.deeplink.di.component

import com.tokopedia.developer_options.deeplink.di.module.DeepLinkViewModelModule
import com.tokopedia.developer_options.deeplink.di.scope.DeepLinkScope
import dagger.Component

@DeepLinkScope
@Component(
    modules = [DeepLinkViewModelModule::class]
)
interface DeepLinkComponent {
}