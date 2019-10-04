package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.usecase.TrainCheckVoucherUseCase;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.interactor.IPromoCodeInteractor;
import com.tokopedia.loyalty.view.interactor.PromoCodeInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoCodePresenter;
import com.tokopedia.loyalty.view.presenter.PromoCodePresenter;
import com.tokopedia.loyalty.view.view.IPromoCodeView;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

@Module(includes = {RouterModule.class, ServiceApiModule.class})
public class PromoCodeViewModule {

    private final IPromoCodeView view;

    public PromoCodeViewModule(IPromoCodeView view) {
        this.view = view;
    }

    @Provides
    @LoyaltyScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @LoyaltyScope
    IPromoCodeInteractor providePromoCodeInteractor(CompositeSubscription compositeSubscription,
                                                    ITokoPointRepository loyaltyRepository) {
        return new PromoCodeInteractor(compositeSubscription, loyaltyRepository);
    }

    @Provides
    @LoyaltyScope
    IPromoCodePresenter provideIPromoCodePresenter(IPromoCodeInteractor promoCodeInteractor,
                                                   TrainCheckVoucherUseCase trainCheckVoucherUseCase,
                                                   @ApplicationContext Context context) {
        return new PromoCodePresenter(view, promoCodeInteractor,
                trainCheckVoucherUseCase, new UserSession(context));
    }

    @Provides
    LoyaltyModuleRouter provideLoyaltyViewModule(@ApplicationContext Context context) {
        if (context instanceof LoyaltyModuleRouter) {
            return (LoyaltyModuleRouter) context;
        }
        throw new RuntimeException("Applicaton should implement LoyaltyModuleRouter");
    }

    @Provides
    TrainCheckVoucherUseCase provideTrainCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        return new TrainCheckVoucherUseCase(loyaltyModuleRouter);
    }

}
