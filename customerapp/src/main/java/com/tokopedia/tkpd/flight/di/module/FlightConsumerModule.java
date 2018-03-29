package com.tokopedia.tkpd.flight.di.module;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.seller.base.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.seller.base.data.source.UploadImageDataSource;
import com.tokopedia.seller.base.domain.UploadImageRepository;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.di.SessionModule.BEARER_SERVICE;

/**
 * @author by alvarisi on 3/27/18.
 */
@Module
public class FlightConsumerModule {
    public FlightConsumerModule() {
    }

    @Provides
    UploadImageUseCase<UploadShopImageModel> provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       UploadImageRepository uploadImageRepository,
                                                                       GenerateHostRepository generateHostRepository,
                                                                       Gson gson,
                                                                       NetworkCalculator networkCalculator) {
        return new UploadImageUseCase<UploadShopImageModel>(threadExecutor, postExecutionThread, uploadImageRepository,
                generateHostRepository, gson, networkCalculator, UploadShopImageModel.class);
    }

    @Provides
    NetworkCalculator provideNetworkCalculator(@ApplicationContext Context context){
        return new NetworkCalculator(NetworkConfig.POST, context, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL).setIdentity().compileAllParam().finish();
    }

    @Provides
    UploadImageRepository provideUploadImageRepository(UploadImageDataSource uploadImageDataSource){
        return new UploadImageRepositoryImpl(uploadImageDataSource);
    }


    @Provides
    GetUserInfoUseCase provideGetUserInfoUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 ProfileRepository profileRepository) {
        return new GetUserInfoUseCase(threadExecutor, postExecutionThread, profileRepository);
    }

    @Provides
    ProfileRepository provideProfileRepository(ProfileSourceFactory profileSourceFactory) {
        return new ProfileRepositoryImpl(profileSourceFactory);
    }

    @Provides
    ProfileSourceFactory provideProfileSourceFactory(@ApplicationContext Context context,
                                                     @Named(BEARER_SERVICE) AccountsService accountsService,
                                                     GetUserInfoMapper getUserInfoMapper,
                                                     EditUserInfoMapper editUserInfoMapper,
                                                     SessionHandler sessionHandler) {
        return new ProfileSourceFactory(
                context, accountsService,
                getUserInfoMapper, editUserInfoMapper,
                sessionHandler);
    }

    @Named(BEARER_SERVICE)
    @Provides
    AccountsService provideBearerAccountsService(@ApplicationContext Context context,
                                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(sessionHandler.getAccessToken(context)))
            authKey = sessionHandler.getTokenType(context) + " " + sessionHandler
                    .getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }

    @Provides
    GetUserInfoMapper provideGetUserInfoMapper() {
        return new GetUserInfoMapper();
    }

    @Provides
    EditUserInfoMapper provideEditUserInfoMapper() {
        return new EditUserInfoMapper();
    }

}
