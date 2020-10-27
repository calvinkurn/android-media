package com.tokopedia.gm.common.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.datepicker.range.data.repository.DatePickerRepositoryImpl;
import com.tokopedia.datepicker.range.data.source.DatePickerDataSource;
import com.tokopedia.datepicker.range.domain.DatePickerRepository;
import com.tokopedia.datepicker.range.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.datepicker.range.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.datepicker.range.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.datepicker.range.view.presenter.DatePickerPresenter;
import com.tokopedia.datepicker.range.view.presenter.DatePickerPresenterImpl;
import com.tokopedia.gm.common.di.scope.GMScope;
import com.tokopedia.gm.shopinfo.data.cloud.ShopApi;
import com.tokopedia.gm.shopinfo.data.cloud.TomeProductApi;
import com.tokopedia.gm.shopinfo.data.cloud.source.ShopInfoDataSource;
import com.tokopedia.gm.shopinfo.domain.repository.ShopInfoRepository;
import com.tokopedia.gm.shopinfo.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.common.usecase.JobExecutor;
import com.tokopedia.seller.common.usecase.PostExecutionThread;
import com.tokopedia.seller.common.usecase.ThreadExecutor;
import com.tokopedia.seller.common.usecase.UIThread;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@GMScope
@Module
public class GMModule {

    @GMScope
    @Provides
    public ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @GMScope
    @Provides
    public PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @GMScope
    @Provides
    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
                                                   SaveDatePickerUseCase saveDatePickerUseCase,
                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
    }

    @GMScope
    @Provides
    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
        return new DatePickerRepositoryImpl(datePickerDataSource);
    }

    @GMScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @GMScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GMScope
    @Provides
    TomeProductApi provideTomeApi(@TomeQualifier Retrofit retrofit){
        return retrofit.create(TomeProductApi.class);
    }

    @Provides
    @GMScope
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

}