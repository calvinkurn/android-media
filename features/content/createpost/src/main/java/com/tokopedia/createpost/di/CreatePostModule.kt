package com.tokopedia.createpost.di

import android.app.NotificationManager
import android.content.Context

import com.google.gson.Gson
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.affiliatecommon.analytics.AffiliateAnalytics
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.data.pojo.uploadimage.UploadImageResponse
import com.tokopedia.createpost.domain.usecase.GetAffiliateProductSuggestionUseCase
import com.tokopedia.createpost.domain.usecase.GetShopProductSuggestionUseCase
import com.tokopedia.createpost.view.contract.CreatePostContract
import com.tokopedia.createpost.view.presenter.CreatePostPresenter
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.imageuploader.di.ImageUploaderModule
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier
import com.tokopedia.imageuploader.domain.GenerateHostRepository
import com.tokopedia.imageuploader.domain.UploadImageRepository
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.utils.ImageUploaderUtils
import com.tokopedia.twitter_share.TwitterManager
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videouploader.data.UploadVideoApi
import com.tokopedia.videouploader.di.VideoUploaderModule
import com.tokopedia.videouploader.di.VideoUploaderQualifier
import com.tokopedia.videouploader.domain.pojo.DefaultUploadVideoResponse
import com.tokopedia.videouploader.domain.usecase.GenerateVideoTokenUseCase
import com.tokopedia.videouploader.domain.usecase.UploadVideoUseCase

import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * @author by milhamj on 9/26/18.
 */
@Module(includes = [ImageUploaderModule::class, VideoUploaderModule::class])
class CreatePostModule(private val context: Context) {

    @Provides
    @ApplicationContext
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Provides
    @CreatePostScope
    fun provideUploadImageUseCase(
            @ImageUploaderQualifier uploadImageRepository: UploadImageRepository,
            @ImageUploaderQualifier generateHostRepository: GenerateHostRepository,
            @ImageUploaderQualifier gson: Gson,
            @ImageUploaderQualifier userSession: UserSessionInterface,
            @ImageUploaderQualifier imageUploaderUtils: ImageUploaderUtils): UploadImageUseCase<UploadImageResponse> {
        return UploadImageUseCase(
                uploadImageRepository,
                generateHostRepository,
                gson,
                userSession,
                UploadImageResponse::class.java,
                imageUploaderUtils
        )
    }

    @Provides
    @CreatePostScope
    fun providePresenter(createPostPresenter: CreatePostPresenter): CreatePostContract.Presenter {
        return createPostPresenter
    }

    @Provides
    @CreatePostScope
    fun provideUserSession(@ApplicationContext context: Context): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @CreatePostScope
    fun provideAffiliateAnalytics(userSessionInterface: UserSessionInterface): AffiliateAnalytics {
        return AffiliateAnalytics(userSessionInterface)
    }

    @Provides
    @CreatePostScope
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @CreatePostScope
    fun provideUploadVideoUseCase(
            @VideoUploaderQualifier uploadVideoApi: UploadVideoApi,
            @VideoUploaderQualifier gson: Gson,
            generateVideoTokenUseCase: GenerateVideoTokenUseCase):
            UploadVideoUseCase<DefaultUploadVideoResponse> {
        return UploadVideoUseCase(uploadVideoApi, gson, DefaultUploadVideoResponse::class.java, generateVideoTokenUseCase)
    }

    @Provides
    @CreatePostScope
    fun provideGraphQlRepository(@ApplicationContext context: Context): GraphqlRepository {
        GraphqlClient.init(context)
        return GraphqlInteractor.getInstance().graphqlRepository
    }

    @Provides
    @CreatePostScope
    fun provideMultiRequestGraphqlUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase {
        return MultiRequestGraphqlUseCase(graphqlRepository)
    }

    @Provides
    @CreatePostScope
    @Named(GetShopProductSuggestionUseCase.QUERY_SHOP_PRODUCT_SUGGESTION)
    fun provideGetProductSuggestionShopQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_af_shop_product_suggestion)
    }

    @Provides
    @CreatePostScope
    @Named(GetAffiliateProductSuggestionUseCase.QUERY_AFFILIATE_PRODUCT_SUGGESTION)
    fun provideGetProductSuggestionAffiliateQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(context.resources, R.raw.query_af_affiliate_product_suggestion)
    }


    @Provides
    @CreatePostScope
    fun provideTwitterManager(userSession: UserSessionInterface): TwitterManager {
        return TwitterManager(userSession)
    }
}
