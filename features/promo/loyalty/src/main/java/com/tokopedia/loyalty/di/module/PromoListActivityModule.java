package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.PromoActivityScope;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.interactor.PromoInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoListActivityPresenter;
import com.tokopedia.loyalty.view.presenter.PromoListActivityPresenter;
import com.tokopedia.loyalty.view.util.PromoTrackingUtil;
import com.tokopedia.loyalty.view.view.IPromoListActivityView;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 04/01/18.
 */
@Module(includes = {ServiceApiModule.class})
public class PromoListActivityModule {
    private final IPromoListActivityView view;

    @Inject
    public PromoListActivityModule(IPromoListActivityView view) {
        this.view = view;
    }

    @Provides
    @PromoActivityScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @PromoActivityScope
    IPromoInteractor provideIPromoInteractor(CompositeSubscription compositeSubscription,
                                             IPromoRepository promoRepository) {
        return new PromoInteractor(compositeSubscription, promoRepository);
    }

    @Provides
    @PromoActivityScope
    IPromoListActivityPresenter provideIPromoListActivityPresenter(IPromoInteractor promoInteractor) {
        return new PromoListActivityPresenter(promoInteractor, view);
    }

    @Provides
    @PromoActivityScope
    PromoTrackingUtil providePromoTrackingUtil(@ApplicationContext Context context){
        if(context instanceof LoyaltyModuleRouter) {
            return new PromoTrackingUtil((LoyaltyModuleRouter) context);
        }
        throw new RuntimeException("application must implement LoyaltyModuleRouter");
    }
}
