package com.tokopedia.manageaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.manageaddress.di.module.ManageAddressModule
import com.tokopedia.manageaddress.di.module.ManageAddressViewModelModule
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressFragment
import com.tokopedia.manageaddress.ui.manageaddress.fromfriend.FromFriendFragment
import com.tokopedia.manageaddress.ui.manageaddress.mainaddress.MainAddressFragment
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressBottomSheet
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressConfirmationBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [ManageAddressModule::class, ManageAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ManageAddressComponent {
    fun inject(manageAddressFragment: ManageAddressFragment)
    fun inject(mainAddressFragment: MainAddressFragment)
    fun inject(fromFriendFragment: FromFriendFragment)
    fun inject(fragment: ShareAddressBottomSheet)
    fun inject(fragment: ShareAddressConfirmationBottomSheet)
}
