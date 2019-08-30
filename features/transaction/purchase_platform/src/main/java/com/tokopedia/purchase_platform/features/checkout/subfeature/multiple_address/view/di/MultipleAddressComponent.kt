package com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.view.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import dagger.Component

/**
 * Created by Irfan Khoirul on 2019-08-29.
 */

@MultipleAddressScope
@Component(modules = [MultipleAddressModule::class], dependencies = [BaseAppComponent::class])
interface MultipleAddressComponent {
}