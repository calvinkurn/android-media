package com.tokopedia.opportunity.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.opportunity.data.mapper.AcceptOpportunityMapper;
import com.tokopedia.opportunity.data.mapper.OpportunityFilterMapper;
import com.tokopedia.opportunity.data.mapper.OpportunityListMapper;
import com.tokopedia.opportunity.data.mapper.OpportunityNewPriceMapper;
import com.tokopedia.opportunity.data.source.CloudActionReplacementSource;
import com.tokopedia.opportunity.data.source.CloudGetFilterOpportunitySource;
import com.tokopedia.opportunity.data.source.CloudGetListOpportunitySource;
import com.tokopedia.opportunity.data.source.CloudProductDataSource;
import com.tokopedia.opportunity.data.source.api.ReplacementActApi;
import com.tokopedia.opportunity.data.source.api.ReplacementApi;
import com.tokopedia.opportunity.data.source.api.WsProductApi;
import com.tokopedia.opportunity.di.scope.OpportunityScope;
import com.tokopedia.opportunity.domain.interactor.AcceptReplacementUseCase;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityFilterUseCase;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityFirstTimeUseCase;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityNewPriceUseCase;
import com.tokopedia.opportunity.domain.interactor.GetOpportunityUseCase;
import com.tokopedia.opportunity.domain.interactor.GetSnapShotProductUseCase;
import com.tokopedia.opportunity.domain.repository.ProductRepository;
import com.tokopedia.opportunity.domain.repository.ProductRepositoryImpl;
import com.tokopedia.opportunity.domain.repository.ReplacementRepository;
import com.tokopedia.opportunity.domain.repository.ReplacementRepositoryImpl;
import com.tokopedia.opportunity.presenter.OpportunityImpl;
import com.tokopedia.opportunity.presenter.OpportunityListPresenter;
import com.tokopedia.opportunity.presenter.OpportunityListPresenterImpl;
import com.tokopedia.opportunity.presenter.OpportunityPresenter;
import com.tokopedia.opportunity.snapshot.presenter.SnapShotFragmentImpl;
import com.tokopedia.opportunity.snapshot.presenter.SnapShotFragmentPresenter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 1/10/18.
 */
@OpportunityScope
@Module
public class OpportunityModule {
    @OpportunityScope
    @Provides
    public ReplacementActApi provideReplacementActApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ReplacementActApi.class);
    }

    @OpportunityScope
    @Provides
    public ReplacementApi provideReplacementApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(ReplacementApi.class);
    }

    @OpportunityScope
    @Provides
    public WsProductApi provideProductApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(WsProductApi.class);
    }

    @OpportunityScope
    @Provides
    public CloudProductDataSource provideCloudDataSource(WsProductApi productApi, @ApplicationContext Context context){
        return new CloudProductDataSource(productApi, context);
    }

    @OpportunityScope
    @Provides
    public ProductRepository provideProductRepository(CloudProductDataSource cloudProductDataSource){
        return new ProductRepositoryImpl(cloudProductDataSource);
    }

    @OpportunityScope
    @Provides
    public ReplacementRepository provideReplacementRepository(
            CloudGetListOpportunitySource cloudGetListOpportunitySource,
            CloudGetFilterOpportunitySource cloudGetFilterOpportunitySource,
            CloudActionReplacementSource cloudActionReplacementSource) {
        return new ReplacementRepositoryImpl(cloudGetListOpportunitySource, cloudGetFilterOpportunitySource, cloudActionReplacementSource);
    }

    @OpportunityScope
    @Provides
    public OpportunityListPresenter provideOpportunityListPresenter(
            GetOpportunityUseCase getOpportunityUseCase,
            GetOpportunityFilterUseCase getFilterUseCase,
            GetOpportunityFirstTimeUseCase getOpportunityFirstTimeUseCase,
            SessionHandler sessionHandler) {
        return new OpportunityListPresenterImpl(getOpportunityUseCase, getFilterUseCase, getOpportunityFirstTimeUseCase, sessionHandler);
    }

    @OpportunityScope
    @Provides
    public OpportunityPresenter provideOpportunityPresenter(AcceptReplacementUseCase acceptReplacementUseCase,
                                                            GetOpportunityNewPriceUseCase newPriceUseCase) {
        return new OpportunityImpl(acceptReplacementUseCase, newPriceUseCase);
    }

    @OpportunityScope
    @Provides
    public SnapShotFragmentPresenter provideSnapShotFragmentPresenter(GetSnapShotProductUseCase getSnapShotProductUseCase){
        return new SnapShotFragmentImpl(getSnapShotProductUseCase);
    }

    @OpportunityScope
    @Provides
    public OpportunityListMapper provideOpportunityListMapper() {
        return new OpportunityListMapper();
    }

    @OpportunityScope
    @Provides
    public OpportunityNewPriceMapper provideOpportunityNewPriceMapper() {
        return new OpportunityNewPriceMapper();
    }

    @OpportunityScope
    @Provides
    public AcceptOpportunityMapper provideAcceptOpportunityMapper() {
        return new AcceptOpportunityMapper();
    }

    @OpportunityScope
    @Provides
    public OpportunityFilterMapper provideOpportunityFilterMapper() {
        return new OpportunityFilterMapper();
    }
}
