package com.tokopedia.tokopedianow.shoppinglist.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ShoppingListContextModule(private val context: Context) {

    @Provides
    fun provideContext(): Context = context
}
