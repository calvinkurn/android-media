package com.tokopedia.core.drawer2.data.source;

import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.mapper.DepositMapper;
import com.tokopedia.core.drawer2.data.pojo.deposit.DepositModel;
import com.tokopedia.core.drawer2.view.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by nisie on 5/4/17.
 */

public class CloudDepositSource {
    public static final String DRAWER_CACHE_DEPOSIT = "DRAWER_CACHE_DEPOSIT";

    private final Context context;
    private final DepositService depositService;
    private final DepositMapper depositMapper;
    private final LocalCacheHandler drawerCache;

    public CloudDepositSource(Context context,
                              DepositService depositService,
                              DepositMapper depositMapper,
                              LocalCacheHandler drawerCache) {
        this.context = context;
        this.depositService = depositService;
        this.depositMapper = depositMapper;
        this.drawerCache = drawerCache;
    }

    public Observable<DepositModel> getDeposit(TKPDMapParam<String, Object> params) {
        return depositService.getApi()
                .getDeposit2(AuthUtil.generateParamsNetwork2(context, params))
                .map(depositMapper)
                .doOnNext(saveToCache());
    }

    private Action1<DepositModel> saveToCache() {
        return new Action1<DepositModel>() {
            @Override
            public void call(DepositModel drawerDeposit) {
                drawerCache.putString(DRAWER_CACHE_DEPOSIT, drawerDeposit.getDepositData().getDepositTotal());
                drawerCache.applyEditor();
            }
        };
    }
}
