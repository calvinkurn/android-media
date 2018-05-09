package com.tokopedia.checkout.domain.usecase;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.transactiondata.entity.response.notifcounter.NotifCounterCartDataResponse;
import com.tokopedia.transactiondata.repository.ICartRepository;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.router.transactionmodule.TransactionRouter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 16/03/18.
 */

public class GetMarketPlaceCartCounterUseCase extends UseCase<Integer> {

    private final ICartRepository cartRepository;

    public GetMarketPlaceCartCounterUseCase(ICartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        return cartRepository.getNotificationCounter().map(new Func1<NotifCounterCartDataResponse, Integer>() {
            @Override
            public Integer call(NotifCounterCartDataResponse notifCounterCartDataResponse) {
                return notifCounterCartDataResponse.getCounter();
            }
        });
    }

    @Override
    public Observable<Integer> getExecuteObservable(RequestParams requestParams) {
        return super.getExecuteObservable(requestParams);
    }

    public void executeWithSubscriber(final Context context, final TransactionRouter.CartNotificationListener listener) {
        this.execute(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(Integer integer) {
                LocalCacheHandler cache = new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
                cache.putInt(DrawerNotification.IS_HAS_CART, integer > 0 ? 1 : 0);
                cache.putInt(DrawerNotification.CACHE_TOTAL_CART, integer);
                cache.applyEditor();

                listener.onDataReady();
            }
        });
    }

}
