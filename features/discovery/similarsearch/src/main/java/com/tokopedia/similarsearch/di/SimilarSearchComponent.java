package com.tokopedia.similarsearch.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.similarsearch.di.scope.SimilarSearchModuleScope;
import com.tokopedia.similarsearch.view.SimilarSearchFragment;

import dagger.Component;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@SimilarSearchModuleScope
@Component(modules = {SimilarSearchModule.class, }, dependencies = {BaseAppComponent.class})
public interface SimilarSearchComponent {
    void inject(SimilarSearchFragment view);
}