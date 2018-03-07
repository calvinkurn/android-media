package com.tokopedia.gm.common.logout;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.gm.common.logout.di.component.GMLogoutComponent;
import com.tokopedia.gm.common.logout.di.module.GMLogoutModule;
import com.tokopedia.gm.common.logout.di.component.DaggerGMLogoutComponent;

import rx.Subscriber;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class GMLogout {
    public static void onLogOut(AppComponent appComponent) {
        GMLogoutComponent component = DaggerGMLogoutComponent
                .builder()
                .appComponent(appComponent)
                .gMLogoutModule(new GMLogoutModule())
                .build();
        component.getClearStatisticCacheUseCase().execute(RequestParams.EMPTY, new EmptySubscriber());
    }

    public static class EmptySubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Boolean aBoolean) {

        }
    }
}
