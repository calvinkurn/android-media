package com.tokopedia.core.drawer2.domain.datamanager;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.anals.UserAttribute;
import com.tokopedia.core.analytics.domain.usecase.GetUserAttributesUseCase;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NewNotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.subscriber.GetDepositSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.ProfileCompletionSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.ProfileSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TokoCashSubscriber;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import rx.Subscriber;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final ProfileUseCase profileUseCase;
    private final DepositUseCase depositUseCase;
    private final TokoCashUseCase tokoCashUseCase;
    private final GetUserAttributesUseCase userAttributesUseCase;
    private final NewNotificationUseCase newNotificationUseCase;

    private final DrawerDataListener viewListener;

    public DrawerDataManagerImpl(DrawerDataListener viewListener,
                                 ProfileUseCase profileUseCase,
                                 DepositUseCase depositUseCase,
                                 NewNotificationUseCase newNotificationUseCase,
                                 TokoCashUseCase tokoCashUseCase,
                                 GetUserAttributesUseCase uaUseCase) {
        this.viewListener = viewListener;
        this.profileUseCase = profileUseCase;
        this.depositUseCase = depositUseCase;
        this.tokoCashUseCase = tokoCashUseCase;
        this.userAttributesUseCase = uaUseCase;
        this.newNotificationUseCase = newNotificationUseCase;
    }

    @Override
    public void getProfile() {
        profileUseCase.execute(RequestParams.EMPTY, new ProfileSubscriber(viewListener));
    }

    @Override
    public void getDeposit() {
        depositUseCase.execute(RequestParams.EMPTY, new GetDepositSubscriber(viewListener));
    }

    @Override
    public void getTokoCash() {
        tokoCashUseCase.execute(RequestParams.EMPTY, new TokoCashSubscriber(viewListener));
    }

    @Override
    public void getNotification() {
        newNotificationUseCase.execute( NotificationUseCase.getRequestParam(
                GlobalConfig.isSellerApp()),
                new NotificationSubscriber(viewListener));
    }

    @Override
    public void unsubscribe() {
        profileUseCase.unsubscribe();
        newNotificationUseCase.unsubscribe();
        tokoCashUseCase.unsubscribe();
        depositUseCase.unsubscribe();

    }

    @Override
    public void getProfileCompletion() {
        if (viewListener.getActivity().getApplication() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) viewListener.getActivity().getApplication()).getUserInfo(
                    RequestParams.EMPTY, new ProfileCompletionSubscriber(viewListener)
            );
        }
    }


    @Override
    public void getUserAttributes(SessionHandler sessionHandler) {
        userAttributesUseCase.execute(userAttributesUseCase.getUserAttrParam(sessionHandler), new Subscriber<UserAttribute.Data>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(UserAttribute.Data s) {
                CommonUtils.dumper("rxapollo string " + s.toString());
            }
        });
    }
}
