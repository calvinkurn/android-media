package com.tokopedia.product.manage.list.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenterImpl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductManageScope
@Module(includes = ProductManageModule.class)
public class ProductDraftListCountModule{

    @ProductManageScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductManageScope
    @Provides
    ProductDraftListCountPresenter providePresenterDraft(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                                         ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                                         UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        return new ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase,
                clearAllDraftProductUseCase, updateUploadingDraftProductUseCase);
    }
}