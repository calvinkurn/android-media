package com.tokopedia.createpost.di

import android.content.Context
import com.tokopedia.createpost.common.di.CreatePostCommonModule
import com.tokopedia.createpost.common.di.CreatePostScope
import com.tokopedia.createpost.common.view.contract.CreatePostContract
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.shop.common.di.ShopCommonModule
import dagger.Module
import dagger.Provides

/**
 * @author by milhamj on 9/266/18.
 */
@Module(includes = [CreatePostCommonModule::class, ShopCommonModule::class])
class CreatePostModule(private val context: Context) {

    @Provides
    @CreatePostScope
    fun providePresenter(createPostPresenter: CreatePostPresenter): CreatePostContract.Presenter {
        return createPostPresenter
    }
}
