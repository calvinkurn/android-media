package com.tokopedia.imagesearch.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.imagesearch.di.module.ImageSearchModule;
import com.tokopedia.imagesearch.di.scope.ImageSearchScope;
import com.tokopedia.imagesearch.search.ImageSearchActivity;
import com.tokopedia.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.imagesearch.search.fragment.product.ImageProductListPresenterImpl;

import dagger.Component;

/**
 * Created by henrypriyono on 10/10/17.
 */

@ImageSearchScope
@Component(modules = ImageSearchModule.class, dependencies = BaseAppComponent.class)
public interface ImageSearchComponent {

    void inject(ImageSearchActivity imageSearchActiviy);

    void inject(ImageSearchProductListFragment imageProductListFragment);

    void inject(ImageProductListPresenterImpl imageProductListPresenter);
}
