package com.tokopedia.loyalty.di.module;

import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.loyalty.di.PromoDetailScope;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.interactor.PromoInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoDetailPresenter;
import com.tokopedia.loyalty.view.presenter.PromoDetailPresenter;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Aghny A. Putra on 26/03/18
 */

@Module(includes = {ServiceApiModule.class})
public class PromoDetailModule {

    @Provides
    @PromoDetailScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @PromoDetailScope
    IPromoInteractor provideIPromoInteractor(CompositeSubscription compositeSubscription,
                                             IPromoRepository promoRepository) {
        return new PromoInteractor(compositeSubscription, promoRepository);
    }

    @Provides
    @PromoDetailScope
    IPromoDetailPresenter providePromoDetailPresenter(IPromoInteractor promoInteractor) {
        return new PromoDetailPresenter(promoInteractor);
    }

    @Provides
    @PromoDetailScope
    PerformanceMonitoring providePerformanceMonitoring(){
        return new PerformanceMonitoring();
    }
}
