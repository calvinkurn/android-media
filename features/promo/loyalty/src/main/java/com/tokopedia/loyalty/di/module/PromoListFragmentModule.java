package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.PromoFragmentScope;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.interactor.IPromoInteractor;
import com.tokopedia.loyalty.view.interactor.PromoInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoListPresenter;
import com.tokopedia.loyalty.view.presenter.PromoListPresenter;
import com.tokopedia.loyalty.view.util.PromoTrackingUtil;
import com.tokopedia.loyalty.view.view.IPromoListView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 03/01/18.
 */
@Module(includes = {ServiceApiModule.class})
public class PromoListFragmentModule {

    private final IPromoListView view;

    public PromoListFragmentModule(IPromoListView view) {
        this.view = view;
    }

    @Provides
    @PromoFragmentScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @PromoFragmentScope
    IPromoInteractor provideIPromoInteractor(CompositeSubscription compositeSubscription,
                                             IPromoRepository promoRepository) {
        return new PromoInteractor(compositeSubscription, promoRepository);
    }

    @Provides
    @PromoFragmentScope
    IPromoListPresenter provideIPromoListPresenter(IPromoInteractor promoInteractor, PromoTrackingUtil promoTrackingUtil) {
        return new PromoListPresenter(promoInteractor, view, promoTrackingUtil);
    }

    @Provides
    @PromoFragmentScope
    PromoTrackingUtil providePromoTrackingUtil(@ApplicationContext Context context){
        if(context instanceof LoyaltyModuleRouter) {
            return new PromoTrackingUtil((LoyaltyModuleRouter) context);
        }
        throw new RuntimeException("application must implement LoyaltyModuleRouter");
    }
}
