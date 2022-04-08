package com.tokopedia.shop_widget.note.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop_widget.note.di.module.ShopNoteModule
import com.tokopedia.shop_widget.note.di.scope.ShopNoteScope
import com.tokopedia.shop_widget.note.view.bottomsheet.ShopNoteBottomSheet
import com.tokopedia.shop_widget.note.view.fragment.ShopNoteDetailFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopNoteScope
@Component(modules = [ShopNoteModule::class], dependencies = [BaseAppComponent::class])
interface ShopNoteComponent {
    fun inject(shopNoteDetailFragment: ShopNoteDetailFragment)
    fun inject(shopNoteBottomSheet: ShopNoteBottomSheet)
}