package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.repository.TokoPointRepository;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.domain.usecase.TrainCheckVoucherUseCase;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.interactor.PromoCouponInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoCouponPresenter;
import com.tokopedia.loyalty.view.presenter.PromoCouponPresenter;
import com.tokopedia.loyalty.view.view.IPromoCouponView;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.user.session.UserSession;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@Module(includes = {RouterModule.class, ServiceApiModule.class})
public class PromoCouponViewModule {

    private final IPromoCouponView view;

    public PromoCouponViewModule(IPromoCouponView view) {
        this.view = view;
    }

    @Provides
    @LoyaltyScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @LoyaltyScope
    IPromoCouponInteractor provideIPromoCouponInteractor(CompositeSubscription compositeSubscription,
                                                         ITokoPointRepository loyaltyRepository) {
        return new PromoCouponInteractor(compositeSubscription, loyaltyRepository);
    }

    @Provides
    TrainCheckVoucherUseCase provideTrainCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        return new TrainCheckVoucherUseCase(loyaltyModuleRouter);
    }

    @Provides
    @LoyaltyScope
    IPromoCouponPresenter provideIPromoCouponPresenter(IPromoCouponInteractor promoCouponInteractor, FlightCheckVoucherUseCase flightCheckVoucherUseCase,
                                                       TrainCheckVoucherUseCase trainVoucherUseCase, @ApplicationContext Context context) {
        return new PromoCouponPresenter(view, promoCouponInteractor, flightCheckVoucherUseCase, trainVoucherUseCase, new UserSession(context));
    }


    @Provides
    LoyaltyModuleRouter provideLoyaltyViewModule(@ApplicationContext Context context) {
        if (context instanceof LoyaltyModuleRouter) {
            return (LoyaltyModuleRouter) context;
        }
        throw new RuntimeException("Applicaton should implement LoyaltyModuleRouter");
    }


    @Provides
    FlightCheckVoucherUseCase provideFlightCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        return new FlightCheckVoucherUseCase(loyaltyModuleRouter);
    }


}
