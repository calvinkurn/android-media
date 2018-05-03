package com.tokopedia.topads.keyword.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.mapper.SimpleDataResponseMapper;
import com.tokopedia.seller.product.variant.data.cloud.api.TomeProductApi;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;
import com.tokopedia.topads.dashboard.di.qualifier.TopAdsManagementQualifier;
import com.tokopedia.topads.keyword.data.repository.TopAdsKeywordRepositoryImpl;
import com.tokopedia.topads.keyword.data.source.KeywordDashboardDataSouce;
import com.tokopedia.topads.keyword.data.source.cloud.api.KeywordApi;
import com.tokopedia.topads.keyword.di.scope.TopAdsKeywordScope;
import com.tokopedia.topads.keyword.domain.TopAdsKeywordRepository;
import com.tokopedia.topads.keyword.domain.interactor.EditTopAdsKeywordDetailUseCase;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordEditDetailPresenter;
import com.tokopedia.topads.keyword.view.presenter.TopAdsKeywordEditDetailPresenterImpl;
import com.tokopedia.topads.sourcetagging.constant.TopAdsSourceTaggingConstant;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.interactor.TopAdsGetSourceTaggingUseCase;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/26/17.
 */

@TopAdsKeywordScope
@Module
public class TopAdsKeywordEditDetailModule {

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordEditDetailPresenter provideTopAdsKeywordEditDetailPresenter(EditTopAdsKeywordDetailUseCase editTopadsKeywordDetailUseCase,
                                                                             TopAdsGetSourceTaggingUseCase topAdsGetSourceTaggingUseCase){
        return new TopAdsKeywordEditDetailPresenterImpl(editTopadsKeywordDetailUseCase, topAdsGetSourceTaggingUseCase);
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsKeywordRepository provideTopAdsKeywordRepository(KeywordDashboardDataSouce keywordDashboardDataSouce/*, ShopInfoRepository shopInfoRepository*/){
        return new TopAdsKeywordRepositoryImpl(keywordDashboardDataSouce/*, shopInfoRepository*/);
    }

    @TopAdsKeywordScope
    @Provides
    TomeProductApi provideTomeApi(/*@TomeQualifier*/@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(TomeProductApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    KeywordApi provideKeywordApi(@TopAdsManagementQualifier Retrofit retrofit){
        return retrofit.create(KeywordApi.class);
    }

    @TopAdsKeywordScope
    @Provides
    SimpleDataResponseMapper<ShopModel> provideMapper(){
        return new SimpleDataResponseMapper<>();
    }

    @TopAdsKeywordScope
    @Provides
    TopAdsSourceTaggingLocal provideTopAdsSourceTagging(@ApplicationContext Context context){
        return new TopAdsSourceTaggingLocal(context);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal){
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @TopAdsKeywordScope
    @Provides
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource){
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }

}

