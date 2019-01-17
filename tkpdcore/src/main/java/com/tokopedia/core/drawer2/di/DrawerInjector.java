package com.tokopedia.core.drawer2.di;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.UserAttributesFactory;
import com.tokopedia.core.drawer2.data.pojo.topcash.TokoCashData;
import com.tokopedia.core.drawer2.data.repository.UserAttributesRepository;
import com.tokopedia.core.drawer2.data.repository.UserAttributesRepositoryImpl;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManager;
import com.tokopedia.core.drawer2.domain.datamanager.DrawerDataManagerImpl;
import com.tokopedia.core.drawer2.domain.interactor.GetSellerUserAttributesUseCase;
import com.tokopedia.core.drawer2.domain.interactor.GetUserAttributesUseCase;
import com.tokopedia.core.drawer2.domain.interactor.TokoCashUseCase;
import com.tokopedia.core.drawer2.view.DrawerDataListener;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.network.apiservices.drawer.DrawerService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSession;

import rx.Observable;

/**
 * @author by nisie on 6/6/17.
 */

public class DrawerInjector {
    private static final String BEARER_TOKEN = "Bearer ";

    public static DrawerHelper getDrawerHelper(AppCompatActivity activity,
                                               SessionHandler sessionHandler,
                                               LocalCacheHandler drawerCache) {

        return ((TkpdCoreRouter) activity.getApplication()).getDrawer(activity,
                sessionHandler, drawerCache, new GlobalCacheManager());
    }

    public static DrawerDataManager getDrawerDataManager(Context context,
                                                         DrawerDataListener drawerDataListener,
                                                         LocalCacheHandler drawerCache) {

        JobExecutor jobExecutor = new JobExecutor();
        PostExecutionThread uiThread = new UIThread();

        NetworkRouter networkRouter = (NetworkRouter) context.getApplicationContext();
        UserSession userSession = new UserSession(context);

        UserAttributesRepository userAttributesRepository = new UserAttributesRepositoryImpl(
                new UserAttributesFactory(new DrawerService(context, userSession, networkRouter), drawerCache)
        );

        GetUserAttributesUseCase getUserAttributesUseCase = new GetUserAttributesUseCase(jobExecutor,
                new UIThread(), userAttributesRepository);

        GetSellerUserAttributesUseCase getSellerrAttributesUseCase = new GetSellerUserAttributesUseCase(jobExecutor,
                new UIThread(), userAttributesRepository);

        Observable<TokoCashData> tokoCashModelObservable = ((TkpdCoreRouter) context.getApplicationContext()).getTokoCashBalance();
        TokoCashUseCase tokoCashUseCase = new TokoCashUseCase(
                jobExecutor,
                uiThread,
                tokoCashModelObservable
        );

        return new DrawerDataManagerImpl(
                drawerDataListener,
                tokoCashUseCase,
                getUserAttributesUseCase,
                getSellerrAttributesUseCase);
    }

}
