package com.tokopedia.train.common.di;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.train.checkout.TrainCheckoutCloudDataStore;
import com.tokopedia.train.common.TrainRouter;
import com.tokopedia.train.common.constant.TrainApi;
import com.tokopedia.train.common.constant.TrainUrl;
import com.tokopedia.train.common.data.TrainDataStoreFactory;
import com.tokopedia.train.common.data.TrainRepositoryImpl;
import com.tokopedia.train.common.data.interceptor.TrainInterceptor;
import com.tokopedia.train.common.domain.TrainRepository;
import com.tokopedia.train.common.util.TrainFlowUtil;
import com.tokopedia.train.passenger.data.cloud.TrainSoftBookingCloudDataStore;
import com.tokopedia.train.reviewdetail.data.TrainCheckVoucherCloudDataStore;
import com.tokopedia.train.scheduledetail.domain.GetScheduleDetailUseCase;
import com.tokopedia.train.search.data.TrainScheduleCacheDataStore;
import com.tokopedia.train.search.data.TrainScheduleCloudDataStore;
import com.tokopedia.train.search.data.TrainScheduleDataStoreFactory;
import com.tokopedia.train.search.data.TrainScheduleDbDataStore;
import com.tokopedia.train.search.domain.GetDetailScheduleUseCase;
import com.tokopedia.train.seat.data.TrainSeatCloudDataStore;
import com.tokopedia.train.station.data.TrainStationCacheDataStore;
import com.tokopedia.train.station.data.TrainStationCloudDataStore;
import com.tokopedia.train.station.data.TrainStationDataStoreFactory;
import com.tokopedia.train.station.data.TrainStationDbDataStore;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by alvarisi on 2/19/18.
 */
@Module
public class TrainModule {

    public TrainModule() {
    }

    @Provides
    @TrainScope
    public TrainInterceptor provideTrainInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter, UserSession userSession) {
        return new TrainInterceptor(context, abstractionRouter, userSession);
    }

    @Provides
    public TrainFlowUtil provideTrainFlowUtil() {
        return new TrainFlowUtil();
    }

    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor httpLoggingInterceptor, TrainRouter trainRouter, TrainInterceptor trainInterceptor) {
        OkHttpRetryPolicy retryPolicy = OkHttpRetryPolicy.createdDefaultOkHttpRetryPolicy();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS);
        builder.addInterceptor(trainInterceptor);
        if (GlobalConfig.isAllowDebuggingTools()) {
            builder.addInterceptor(httpLoggingInterceptor)
                    .addInterceptor(trainRouter.getChuckInterceptor());
        }

        return builder.build();
    }

    @TrainScope
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient,
                                    Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(TrainUrl.BASE_URL).client(okHttpClient).build();
    }

    @TrainScope
    @Provides
    public TrainApi provideTrainApi(Retrofit retrofit) {
        return retrofit.create(TrainApi.class);
    }

    @TrainScope
    @Provides
    public TrainDataStoreFactory provideDataStoreFactory(TrainApi trainApi) {
        return new TrainDataStoreFactory(trainApi);
    }

    @TrainScope
    @Provides
    public TrainStationDbDataStore provideTrainStationDbDataStore() {
        return new TrainStationDbDataStore();
    }

    @TrainScope
    @Provides
    public TrainStationCloudDataStore provideTrainStationCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        return new TrainStationCloudDataStore(trainApi, context);
    }

    @TrainScope
    @Provides
    public TrainRouter provideTrainRouter(@ApplicationContext Context context) {
        if (context instanceof TrainRouter) {
            return ((TrainRouter) context);
        }
        throw new RuntimeException("Application must implement " + TrainRouter.class.getCanonicalName());
    }

    @TrainScope
    @Provides
    public TrainStationCacheDataStore provideTrainStationCacheDataStore(@ApplicationContext Context context) {
        return new TrainStationCacheDataStore(context);
    }

    @TrainScope
    @Provides
    public TrainStationDataStoreFactory provideTrainStationDataStoreFactory(TrainStationDbDataStore trainStationDbDataStore,
                                                                            TrainStationCloudDataStore trainStationCloudDataStore,
                                                                            TrainStationCacheDataStore trainStationCacheDataStore) {
        return new TrainStationDataStoreFactory(trainStationDbDataStore, trainStationCloudDataStore, trainStationCacheDataStore);
    }

    @TrainScope
    @Provides
    public TrainRepository provideTrainRepository(TrainSeatCloudDataStore trainSeatCloudDataStore,
                                                  TrainStationDataStoreFactory trainStationDataStoreFactory,
                                                  TrainScheduleDataStoreFactory scheduleDataStoreFactory,
                                                  TrainSoftBookingCloudDataStore trainSoftBookingCloudDataStore,
                                                  TrainCheckVoucherCloudDataStore trainCheckVoucherCloudDataStore,
                                                  TrainCheckoutCloudDataStore trainCheckoutCloudDataStore) {
        return new TrainRepositoryImpl(trainSeatCloudDataStore, trainStationDataStoreFactory, scheduleDataStoreFactory,
                trainSoftBookingCloudDataStore, trainCheckVoucherCloudDataStore, trainCheckoutCloudDataStore);
    }

    @TrainScope
    @Provides
    public TrainSoftBookingCloudDataStore provideTrainSoftBookingCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        return new TrainSoftBookingCloudDataStore(trainApi, context);
    }

    @TrainScope
    @Provides
    public TrainSeatCloudDataStore provideTrainSeatCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        return new TrainSeatCloudDataStore(trainApi, context);
    }

    @TrainScope
    @Provides
    public TrainScheduleDbDataStore provideTrainScheduleDbDataStore() {
        return new TrainScheduleDbDataStore();
    }

    @TrainScope
    @Provides
    public TrainScheduleCloudDataStore provideTrainScheduleCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        return new TrainScheduleCloudDataStore(trainApi, context);
    }

    @TrainScope
    @Provides
    public TrainCheckVoucherCloudDataStore provideTrainCheckVoucherCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        return new TrainCheckVoucherCloudDataStore(trainApi, context);
    }

    @TrainScope
    @Provides
    public TrainCheckoutCloudDataStore provideTrainCheckoutCloudDataStore(TrainApi trainApi, @ApplicationContext Context context) {
        return new TrainCheckoutCloudDataStore(trainApi, context);
    }

    @TrainScope
    @Provides
    public TrainScheduleCacheDataStore provideTrainScheduleCacheDataStore(@ApplicationContext Context context) {
        return new TrainScheduleCacheDataStore(context);
    }

    @TrainScope
    @Provides
    public TrainScheduleDataStoreFactory provideTrainScheduleDataStoreFactory(TrainScheduleDbDataStore trainScheduleDbDataStore,
                                                                              TrainScheduleCloudDataStore trainScheduleCloudDataStore,
                                                                              TrainScheduleCacheDataStore trainScheduleCacheDataStore) {
        return new TrainScheduleDataStoreFactory(trainScheduleDbDataStore, trainScheduleCacheDataStore, trainScheduleCloudDataStore);
    }

    @TrainScope
    @Provides
    GetDetailScheduleUseCase provideGetDetailScheduleUseCase(TrainRepository trainRepository) {
        return new GetDetailScheduleUseCase(trainRepository);
    }

    @TrainScope
    @Provides
    GetScheduleDetailUseCase provideGetScheduleDetailUseCase(TrainRepository trainRepository) {
        return new GetScheduleDetailUseCase(trainRepository);
    }

}
