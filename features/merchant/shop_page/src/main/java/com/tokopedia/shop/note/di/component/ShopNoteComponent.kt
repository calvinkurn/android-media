package com.tokopedia.shop.note.di.component

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.note.di.module.ShopNoteModule
import com.tokopedia.shop.note.di.scope.ShopNoteScope
import com.tokopedia.shop.note.view.fragment.ShopNoteDetailFragment
import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopNoteScope
@Component(modules = [ShopNoteModule::class], dependencies = [ShopComponent::class])
interface ShopNoteComponent {
    fun inject(shopNoteDetailFragment: ShopNoteDetailFragment)
}