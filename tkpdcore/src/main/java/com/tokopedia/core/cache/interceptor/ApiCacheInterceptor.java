package com.tokopedia.core.cache.interceptor;

import android.content.pm.PackageManager;
import android.util.Log;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.cache.CacheHelper;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.ApiCacheInterceptorUseCase;
import com.tokopedia.core.cache.domain.interactor.BaseApiCacheInterceptor;
import com.tokopedia.core.cache.domain.interactor.CheckWhiteListUseCase;
import com.tokopedia.core.cache.domain.interactor.ClearTimeOutCacheDataUseCase;
import com.tokopedia.core.cache.domain.interactor.GetCacheDatatUseCase;
import com.tokopedia.core.cache.domain.interactor.ClearTimeOutCacheData;
import com.tokopedia.core.cache.domain.interactor.GetCacheDataUseCase;
import com.tokopedia.core.cache.domain.interactor.SaveToDbUseCase;
import com.tokopedia.core.var.TkpdCache;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by normansyahputa on 8/9/17.
 */

public class ApiCacheInterceptor implements Interceptor {
    private static final String LOG_TAG = "ApiCacheInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        String versionName = null;
        try {
            versionName = MainApplication.getAppContext().getPackageManager().getPackageInfo(MainApplication.getAppContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        ApiCacheRepository apiCacheRepository = new ApiCacheRepositoryImpl(
                new LocalCacheHandler(MainApplication.getAppContext(), TkpdCache.CACHE_API),
                versionName,
                new CacheHelper()
        );

        new ClearTimeOutCacheData(threadExecutor, postExecutionThread, apiCacheRepository).createObservableSync(RequestParams.EMPTY).toBlocking().first();

        CheckWhiteListUseCase checkWhiteListUseCase = new CheckWhiteListUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
        GetCacheDataUseCase getCacheDataUseCase = new GetCacheDataUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
        SaveToDbUseCase saveToDbUseCase = new SaveToDbUseCase(threadExecutor, postExecutionThread, apiCacheRepository);

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BaseApiCacheInterceptor.FULL_URL, request.url().toString());
        requestParams.putString(BaseApiCacheInterceptor.METHOD, request.method());

        String cacheData = getCacheDataUseCase.createObservableSync(requestParams).toBlocking().first();
        Boolean isInWhiteList = checkWhiteListUseCase.createObservableSync(requestParams).toBlocking().first();

        if (isInWhiteList) {
            if (cacheData == null || cacheData.equals("")) {
                Log.d(LOG_TAG, request.url().toString() + " data is not here !!");
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    throw e;
                }

                requestParams.putObject(SaveToDbUseCase.RESPONSE, response);

                saveToDbUseCase.createObservableSync(requestParams).toBlocking().first();

                return response;
            } else {
                Log.d(LOG_TAG, request.url().toString() + " already in here !!");
                Response.Builder builder = new Response.Builder();
                builder.request(request);
                builder.protocol(Protocol.HTTP_1_1);
                builder.code(200);
                builder.message("");
                builder.body(ResponseBody.create(MediaType.parse("application/json"), cacheData));
                return builder.build();
            }
        }else{
            Log.d(LOG_TAG, String.format("%s just hit another network !!", request.url().toString()));
            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception e) {
                throw e;
            }

            return response;
        }
    }


}
