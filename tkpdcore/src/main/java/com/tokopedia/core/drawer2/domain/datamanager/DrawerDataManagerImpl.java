package com.tokopedia.core.drawer2.domain.datamanager;

import android.content.Context;
import android.os.Bundle;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.drawer2.data.factory.DepositSourceFactory;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TopPointsSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.mapper.TopPointsMapper;
import com.tokopedia.core.drawer2.data.repository.DepositRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TopPointsRepositoryImpl;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.domain.DepositRepository;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.TopPointsRepository;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileNetworkInteractor;
import com.tokopedia.core.drawer2.domain.interactor.ProfileNetworkInteractorImpl;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopPointsUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.GetDepositSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TokoCashSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TopPointsSubscriber;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.util.SessionHandler;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final ProfileNetworkInteractor profileNetworkInteractor;
    private final DepositUseCase depositUseCase;
    private final NotificationUseCase notificationUseCase;
    private final TokoCashUseCase tokoCashUseCase;
    private final DrawerDataListener viewListener;
    private final SessionHandler sessionHandler;
    private final LocalCacheHandler drawerCache;
    private final GlobalCacheManager cacheManager;
    private final TopPointsUseCase topPointsUseCase;

    public DrawerDataManagerImpl(Context context, DrawerDataListener viewListener, LocalCacheHandler drawerCache) {
        this.viewListener = viewListener;
        sessionHandler = new SessionHandler(context);
        cacheManager = new GlobalCacheManager();
        this.drawerCache = new LocalCacheHandler(context, DrawerHelper.DRAWER_CACHE);
        profileNetworkInteractor = new ProfileNetworkInteractorImpl(new PeopleService());

        TopPointsSourceFactory topPointsSourceFactory = new TopPointsSourceFactory(
                context,
                new CloverService(),
                new TopPointsMapper(),
                cacheManager);

        TopPointsRepository topPointsRepository = new TopPointsRepositoryImpl(topPointsSourceFactory);

        topPointsUseCase = new TopPointsUseCase(
                new JobExecutor(),
                new UIThread(),
                topPointsRepository
        );

        Bundle bundle = new Bundle();
        String authKey = sessionHandler.getAccessToken(context);
        authKey = "Bearer" + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        AccountsService accountsService = new AccountsService(bundle);

        TokoCashSourceFactory tokoCashSourceFactory = new TokoCashSourceFactory(
                context,
                accountsService,
                new TokoCashMapper(),
                cacheManager);

        TokoCashRepository tokoCashRepository = new TokoCashRepositoryImpl(tokoCashSourceFactory);
        tokoCashUseCase = new TokoCashUseCase(
                new JobExecutor(),
                new UIThread(),
                tokoCashRepository
        );

        NotificationSourceFactory notificationSourceFactory = new NotificationSourceFactory(
                context,
                new NotificationService(),
                new NotificationMapper(),
                this.drawerCache
        );
        NotificationRepository notificationRepository = new NotificationRepositoryImpl(notificationSourceFactory);
        notificationUseCase = new NotificationUseCase(
                new JobExecutor(),
                new UIThread(),
                notificationRepository
        );

        DepositSourceFactory depositSourceFactory = new DepositSourceFactory(
                context,
                new DepositService(),
                new DepositMapper(),
                this.drawerCache);
        DepositRepository depositRepository = new DepositRepositoryImpl(depositSourceFactory);
        depositUseCase = new DepositUseCase(
                new JobExecutor(),
                new UIThread(),
                depositRepository);
    }

    @Override
    public Observable<DrawerProfile> getDrawerProfile(Context context) {
        return profileNetworkInteractor.getProfileInfo(context, new TKPDMapParam<String, String>())
                .flatMap(new Func1<Response<TkpdResponse>, Observable<DrawerProfile>>() {
                    @Override
                    public Observable<DrawerProfile> call(Response<TkpdResponse> response) {
                        DrawerProfile drawerProfile = convertToDrawerProfile(response);
                        return Observable.just(drawerProfile);
                    }
                });
    }

    @Override
    public void getDeposit() {
        depositUseCase.execute(getDepositParam(), new GetDepositSubscriber(viewListener));
    }

    private RequestParams getDepositParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public void getTopPoints() {
        topPointsUseCase.execute(getTopPointsParam(), new TopPointsSubscriber(viewListener));
    }

    private RequestParams getTopPointsParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public void getTokoCash() {
        tokoCashUseCase.execute(getTokoCashParam(), new TokoCashSubscriber(viewListener));
    }

    private RequestParams getTokoCashParam() {
        return RequestParams.EMPTY;
    }

    @Override
    public void getNotification() {
        notificationUseCase.execute(getNotificationParam(), new NotificationSubscriber(viewListener));
    }

    private RequestParams getNotificationParam() {
        return RequestParams.EMPTY;
    }

    private DrawerProfile convertToDrawerProfile(Response<TkpdResponse> response) {
        ProfileData profileData = response.body()
                .convertDataObj(ProfileData.class);
        DrawerProfile profile = new DrawerProfile();
        profile.setUserName(profileData.getUserInfo().getUserName());
        profile.setUserAvatar(profileData.getUserInfo().getUserImage());
        profile.setShopName(profileData.getShopInfo().getShopName());
        profile.setShopCover(profileData.getShopInfo().getShopCover());
        profile.setShopAvatar(profileData.getShopInfo().getShopAvatar());
        return profile;
    }

    @Override
    public void unsubscribe() {

    }
}
