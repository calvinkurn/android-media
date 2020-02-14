package com.tokopedia.autocomplete.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.autocomplete.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.autocomplete.domain.interactor.SearchMapper;
import com.tokopedia.autocomplete.network.BrowseApi;
import com.tokopedia.autocomplete.repository.AutoCompleteDataSource;
import com.tokopedia.autocomplete.repository.AutoCompleteRepository;
import com.tokopedia.autocomplete.repository.AutoCompleteRepositoryImpl;
import com.tokopedia.autocomplete.usecase.AutoCompleteUseCase;
import com.tokopedia.autocomplete.usecase.DeleteRecentSearchUseCase;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dagger.Module;
import dagger.Provides;

@AutoCompleteScope
@Module
public class AutoCompleteModule {

    @AutoCompleteScope
    @Provides
    AutoCompleteUseCase provideSearchUseCase(
            AutoCompleteRepository autoCompleteRepository
    ) {
        return new AutoCompleteUseCase(
                autoCompleteRepository
        );
    }

    @AutoCompleteScope
    @Provides
    AutoCompleteRepository provideAutoCompleteRepository(
        @AutoCompleteQualifier BrowseApi browseApi,
        SearchMapper autoCompleteMapper
    ) {
        return new AutoCompleteRepositoryImpl(
            new AutoCompleteDataSource(browseApi, autoCompleteMapper, PersistentCacheManager.instance)
        );
    }

    @AutoCompleteScope
    @Provides
    SearchMapper provideSearchMapper() {
        return new SearchMapper();
    }

    @AutoCompleteScope
    @Provides
    DeleteRecentSearchUseCase provideDeleteRecentSearchUseCase(
            AutoCompleteRepository autoCompleteRepository,
            AutoCompleteUseCase autoCompleteUseCase
    ) {
        return new DeleteRecentSearchUseCase(
                autoCompleteRepository,
                autoCompleteUseCase
        );
    }

    @AutoCompleteScope
    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
//        return new UserSessionInterface() {
//            @Override
//            public String getAccessToken() {
//                return null;
//            }
//
//            @Override
//            public String getTokenType() {
//                return null;
//            }
//
//            @Override
//            public String getFreshToken() {
//                return null;
//            }
//
//            @Override
//            public String getUserId() {
//                return "23750474";
//            }
//
//            @Override
//            public boolean isLoggedIn() {
//                return true;
//            }
//
//            @Override
//            public String getShopId() {
//                return null;
//            }
//
//            @Override
//            public String getName() {
//                return null;
//            }
//
//            @Override
//            public String getProfilePicture() {
//                return null;
//            }
//
//            @Override
//            public String getTemporaryUserId() {
//                return null;
//            }
//
//            @Override
//            public String getDeviceId() {
//                return "cqRdP4DxcI4:APA91bGSejfaXv8pzuavq5s3SvfUT42oQ99Mo-JNDfPnKih5bfC6VCi0RWemIS_i8apvDCG-zUIyjAqS3Z5KshDTXC1odZ4P2bZ4bISJ7dOeZGKMp6M6ZoQ-l8yOD9RcUVS9NxjfCFZX";
//            }
//
//            @Override
//            public String getTempEmail() {
//                return null;
//            }
//
//            @Override
//            public String getTempPhoneNumber() {
//                return null;
//            }
//
//            @Override
//            public boolean isMsisdnVerified() {
//                return false;
//            }
//
//            @Override
//            public boolean hasShownSaldoWithdrawalWarning() {
//                return false;
//            }
//
//            @Override
//            public String getPhoneNumber() {
//                return null;
//            }
//
//            @Override
//            public String getEmail() {
//                return null;
//            }
//
//            @Override
//            public String getRefreshTokenIV() {
//                return null;
//            }
//
//            @Override
//            public boolean isFirstTimeUser() {
//                return false;
//            }
//
//            @Override
//            public boolean isGoldMerchant() {
//                return false;
//            }
//
//            @Override
//            public String getShopName() {
//                return null;
//            }
//
//            @Override
//            public boolean hasShop() {
//                return false;
//            }
//
//            @Override
//            public boolean hasPassword() {
//                return false;
//            }
//
//            @Override
//            public String getGCToken() {
//                return null;
//            }
//
//            @Override
//            public String getShopAvatar() {
//                return null;
//            }
//
//            @Override
//            public boolean isPowerMerchantIdle() {
//                return false;
//            }
//
//            @Override
//            public String getAutofillUserData() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public String getTwitterAccessToken() {
//                return null;
//            }
//
//            @Nullable
//            @Override
//            public String getTwitterAccessTokenSecret() {
//                return null;
//            }
//
//            @Override
//            public boolean getTwitterShouldPost() {
//                return false;
//            }
//
//            @Override
//            public String getLoginMethod() {
//                return null;
//            }
//
//            @Override
//            public void setUUID(String s) {
//
//            }
//
//            @Override
//            public void setIsLogin(boolean b) {
//
//            }
//
//            @Override
//            public void setUserId(String s) {
//
//            }
//
//            @Override
//            public void setName(String s) {
//
//            }
//
//            @Override
//            public void setEmail(String s) {
//
//            }
//
//            @Override
//            public void setPhoneNumber(String s) {
//
//            }
//
//            @Override
//            public void setShopId(String s) {
//
//            }
//
//            @Override
//            public void setShopName(String s) {
//
//            }
//
//            @Override
//            public void setIsGoldMerchant(boolean b) {
//
//            }
//
//            @Override
//            public void setTempLoginName(String s) {
//
//            }
//
//            @Override
//            public void setTempUserId(String s) {
//
//            }
//
//            @Override
//            public void setIsAffiliateStatus(boolean b) {
//
//            }
//
//            @Override
//            public void setTempPhoneNumber(String s) {
//
//            }
//
//            @Override
//            public void setTempLoginEmail(String s) {
//
//            }
//
//            @Override
//            public void setToken(String s, String s1) {
//
//            }
//
//            @Override
//            public void clearToken() {
//
//            }
//
//            @Override
//            public void logoutSession() {
//
//            }
//
//            @Override
//            public void setFirstTimeUserOnboarding(boolean b) {
//
//            }
//
//            @Override
//            public void setFirstTimeUser(boolean b) {
//
//            }
//
//            @Override
//            public void setToken(String s, String s1, String s2) {
//
//            }
//
//            @Override
//            public void setRefreshToken(String s) {
//
//            }
//
//            @Override
//            public void setLoginSession(boolean b, String s, String s1, String s2, boolean b1, String s3, String s4, boolean b2, String s5) {
//
//            }
//
//            @Override
//            public void setIsMSISDNVerified(boolean b) {
//
//            }
//
//            @Override
//            public void setHasPassword(boolean b) {
//
//            }
//
//            @Override
//            public void setProfilePicture(String s) {
//
//            }
//
//            @Override
//            public void setSaldoWithdrawalWaring(boolean b) {
//
//            }
//
//            @Override
//            public void setSaldoIntroPageStatus(boolean b) {
//
//            }
//
//            @Override
//            public void setGCToken(String s) {
//
//            }
//
//            @Override
//            public void setShopAvatar(String s) {
//
//            }
//
//            @Override
//            public void setIsPowerMerchantIdle(boolean b) {
//
//            }
//
//            @Override
//            public void setTwitterAccessTokenAndSecret(@NotNull String s, @NotNull String s1) {
//
//            }
//
//            @Override
//            public void setTwitterShouldPost(boolean b) {
//
//            }
//
//            @Override
//            public void setAutofillUserData(String s) {
//
//            }
//
//            @Override
//            public void setLoginMethod(@NotNull String s) {
//
//            }
//        };
    }
}
