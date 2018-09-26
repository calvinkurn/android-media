package com.tokopedia.affiliate.feature.createpost.di;

import com.tokopedia.affiliate.feature.createpost.view.presenter.CreatePostPresenter;
import com.tokopedia.affiliate.feature.createpost.view.contract.CreatePostContract;

import dagger.Binds;
import dagger.Module;

/**
 * @author by milhamj on 9/26/18.
 */
@Module
public abstract class CreatePostModule {

    @Binds
    @CreatePostScope
    abstract CreatePostContract.Presenter providePresenter(CreatePostPresenter createPostPresenter);
}
