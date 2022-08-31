package com.tokopedia.universal_sharing.di

import com.tokopedia.universal_sharing.view.bottomsheet.UniversalShareBottomSheet
import dagger.Component

@Component(modules = [UniversalShareBottomSheetModule::class])
interface UniversalShareBottomSheetComponents {
    fun inject(bottomSheet: UniversalShareBottomSheet)
}