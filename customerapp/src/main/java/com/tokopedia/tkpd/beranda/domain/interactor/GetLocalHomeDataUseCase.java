package com.tokopedia.tkpd.beranda.domain.interactor;

import com.google.gson.Gson;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.domain.TokoCashRepository;
import com.tokopedia.core.drawer2.domain.TopPointsRepository;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.beranda.data.mapper.HomeDataMapper;
import com.tokopedia.tkpd.beranda.data.repository.HomeRepository;
import com.tokopedia.tkpd.beranda.domain.model.banner.HomeBannerResponseModel;
import com.tokopedia.tkpd.beranda.presentation.view.adapter.viewmodel.SaldoViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by errysuprayogion 12/8/17.
 */

public class GetLocalHomeDataUseCase extends UseCase<List<Visitable>> {

    private final HomeRepository homeRepository;
    private final HomeDataMapper homeDataMapper;
    private final GlobalCacheManager cacheManager;
    private final Gson gson;

    public GetLocalHomeDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                   HomeRepository homeRepository, HomeDataMapper homeDataMapper,
                                   GlobalCacheManager cacheManager, Gson gson) {
        super(threadExecutor, postExecutionThread);
        this.homeRepository = homeRepository;
        this.homeDataMapper = homeDataMapper;
        this.cacheManager = cacheManager;
        this.gson = gson;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getSaldoCache(),
                homeRepository.getBannersCache(),
                homeRepository.getTickersCache(),
                homeRepository.getBrandsOfficialStoreCache(),
                homeRepository.getTopPicksCache(),
                homeRepository.getHomeCategorysCache(), homeDataMapper);
    }

    public Observable<SaldoViewModel> getSaldoCache() {
        return Observable.just(true).map(new Func1<Boolean, SaldoViewModel>() {
            @Override
            public SaldoViewModel call(Boolean aBoolean) {
                String cache = cacheManager.getValueString(TkpdCache.Key.HOME_SALDO_CACHE);
                if (cache != null)
                    return gson.fromJson(cache, SaldoViewModel.class);
                throw new RuntimeException("Cache is empty!!");
            }
        });
    }
}
