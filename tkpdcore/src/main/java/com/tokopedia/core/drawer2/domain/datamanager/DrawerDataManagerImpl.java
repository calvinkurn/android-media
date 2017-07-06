package com.tokopedia.core.drawer2.domain.datamanager;

import android.content.Context;
import android.os.Bundle;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.DepositSourceFactory;
import com.tokopedia.core.drawer2.data.factory.NotificationSourceFactory;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TokoCashSourceFactory;
import com.tokopedia.core.drawer2.data.factory.TopPointsSourceFactory;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.mapper.NotificationMapper;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.mapper.TokoCashMapper;
import com.tokopedia.core.drawer2.data.mapper.TopPointsMapper;
import com.tokopedia.core.drawer2.data.repository.DepositRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.NotificationRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.ProfileRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TokoCashRepositoryImpl;
import com.tokopedia.core.drawer2.data.repository.TopPointsRepositoryImpl;
import com.tokopedia.core.drawer2.domain.DepositRepository;
import com.tokopedia.core.drawer2.domain.NotificationRepository;
import com.tokopedia.core.drawer2.domain.ProfileRepository;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.TopPointsRepository;
import com.tokopedia.core.drawer2.domain.interactor.DepositUseCase;
import com.tokopedia.core.drawer2.domain.interactor.NotificationUseCase;
import com.tokopedia.core.drawer2.domain.interactor.ProfileUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TopPointsUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.subscriber.GetDepositSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.NotificationSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.ProfileSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TokoCashSubscriber;
import com.tokopedia.core.drawer2.view.subscriber.TopPointsSubscriber;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.apiservices.clover.CloverService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by nisie on 1/23/17.
 */

public class DrawerDataManagerImpl implements DrawerDataManager {

    private static final String TAG = DrawerDataManagerImpl.class.getSimpleName();
    private final ProfileUseCase profileUseCase;
    private final DepositUseCase depositUseCase;
    private final NotificationUseCase notificationUseCase;
    private final TokoCashUseCase tokoCashUseCase;
    private final TopPointsUseCase topPointsUseCase;

    private final DrawerDataListener viewListener;

    public DrawerDataManagerImpl(DrawerDataListener viewListener,
                                 ProfileUseCase profileUseCase,
                                 DepositUseCase depositUseCase,
                                 NotificationUseCase notificationUseCase,
                                 TokoCashUseCase tokoCashUseCase,
                                 TopPointsUseCase topPointsUseCase) {
        this.viewListener = viewListener;
        this.profileUseCase = profileUseCase;
        this.depositUseCase = depositUseCase;
        this.notificationUseCase = notificationUseCase;
        this.tokoCashUseCase = tokoCashUseCase;
        this.topPointsUseCase = topPointsUseCase;
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
    public void getTopPoints() {
        topPointsUseCase.execute(RequestParams.EMPTY, new TopPointsSubscriber(viewListener));
    }

    @Override
    public void getTokoCash() {
        tokoCashUseCase.execute(RequestParams.EMPTY, new TokoCashSubscriber(viewListener));
    }

    @Override
    public void getNotification() {
        notificationUseCase.execute(
                notificationUseCase.getRequestParam(
                        GlobalConfig.isSellerApp()),
                new NotificationSubscriber(viewListener));
    }

    @Override
    public void unsubscribe() {
        profileUseCase.unsubscribe();
        topPointsUseCase.unsubscribe();
        notificationUseCase.unsubscribe();
        tokoCashUseCase.unsubscribe();
        depositUseCase.unsubscribe();
    }


}
