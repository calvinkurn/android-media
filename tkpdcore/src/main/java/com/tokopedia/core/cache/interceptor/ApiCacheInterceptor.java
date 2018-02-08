package com.tokopedia.core.cache.interceptor;

import android.text.TextUtils;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.cache.constant.CacheApiConstant;
import com.tokopedia.core.cache.data.repository.ApiCacheRepositoryImpl;
import com.tokopedia.core.cache.data.source.ApiCacheDataSource;
import com.tokopedia.core.cache.data.source.cache.CacheApiVersionCache;
import com.tokopedia.core.cache.data.source.db.CacheApiDataManager;
import com.tokopedia.core.cache.domain.ApiCacheRepository;
import com.tokopedia.core.cache.domain.interactor.BaseApiCacheInterceptorUseCase;
import com.tokopedia.core.cache.domain.interactor.CheckWhiteListUseCase;
import com.tokopedia.core.cache.domain.interactor.ClearTimeOutCache;
import com.tokopedia.core.cache.domain.interactor.GetCacheDataUseCaseUseCase;
import com.tokopedia.core.cache.domain.interactor.SaveToDbUseCase;
import com.tokopedia.core.cache.util.CacheApiUtils;
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

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            return getCacheResponse(chain);
        } catch (Throwable t) {
            return getDefaultResponse(chain);
        }
    }

    private Response getDefaultResponse(Chain chain) throws IOException {
        return chain.proceed(chain.request());
    }

    private Response getCacheResponse(Chain chain) throws Throwable {
        Request request = chain.request();

        ThreadExecutor threadExecutor = new JobExecutor();
        PostExecutionThread postExecutionThread = new UIThread();
        String versionName = MainApplication.getAppContext().getPackageManager().getPackageInfo(MainApplication.getAppContext().getPackageName(), 0).versionName;
        ApiCacheRepository apiCacheRepository = new ApiCacheRepositoryImpl(new ApiCacheDataSource(new CacheApiVersionCache(
                new LocalCacheHandler(MainApplication.getAppContext(), TkpdCache.CACHE_API), versionName),
                new CacheApiDataManager())
        );

        new ClearTimeOutCache(threadExecutor, postExecutionThread, apiCacheRepository).executeSync(RequestParams.EMPTY);

        CheckWhiteListUseCase checkWhiteListUseCase = new CheckWhiteListUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
        GetCacheDataUseCaseUseCase getCacheDataUseCase = new GetCacheDataUseCaseUseCase(threadExecutor, postExecutionThread, apiCacheRepository);
        SaveToDbUseCase saveToDbUseCase = new SaveToDbUseCase(threadExecutor, postExecutionThread, apiCacheRepository);

        RequestParams requestParams = RequestParams.create();

        requestParams.putString(BaseApiCacheInterceptorUseCase.PARAM_METHOD, request.method());
        requestParams.putString(BaseApiCacheInterceptorUseCase.PARAM_HOST, request.url().host());
        requestParams.putString(BaseApiCacheInterceptorUseCase.PARAM_PATH, CacheApiUtils.getPath(request.url().toString()));

        boolean inWhiteList = checkWhiteListUseCase.getData(requestParams);

        if (!inWhiteList) {
            CommonUtils.dumper(String.format("Not registered in white list: %s", request.url().toString()));
            throw new Exception("Not registered in white list");
        }
        requestParams.putString(BaseApiCacheInterceptorUseCase.PARAM_REQUEST_PARAM, CacheApiUtils.getRequestParam(request));
        String cachedResponseData = getCacheDataUseCase.getData(requestParams);
        if (TextUtils.isEmpty(cachedResponseData)) {
            CommonUtils.dumper(String.format("Data is not here, fetch and save: %s", request.url().toString()));
            Response response = getDefaultResponse(chain);
            if (CacheApiUtils.isResponseValidToBeCached(response)) {
                requestParams.putObject(SaveToDbUseCase.RESPONSE, response);
                saveToDbUseCase.executeSync(requestParams);
            }
            return response;
        } else {
            CommonUtils.dumper(String.format("Data exist, return data from db: %s", request.url().toString()));
            Response.Builder builder = new Response.Builder();
            builder.request(request);
            builder.protocol(Protocol.HTTP_1_1);
            builder.code(CacheApiConstant.CODE_OK);
            builder.message("");
            builder.body(ResponseBody.create(MediaType.parse(CacheApiConstant.TYPE_APPLICATION_JSON), cachedResponseData));
            return builder.build();
        }
    }
}