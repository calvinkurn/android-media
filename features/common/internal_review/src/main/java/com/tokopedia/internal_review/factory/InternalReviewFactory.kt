package com.tokopedia.internal_review.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.internal_review.analytics.CustomerReviewTracking
import com.tokopedia.internal_review.analytics.ReviewTracking
import com.tokopedia.internal_review.analytics.SellerReviewTracking
import com.tokopedia.internal_review.common.ReviewRemoteConfig
import com.tokopedia.internal_review.common.SellerReviewCacheHandler
import com.tokopedia.internal_review.common.InternalReviewHelper
import com.tokopedia.internal_review.domain.usecase.SendReviewUseCase
import com.tokopedia.internal_review.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.internal_review.view.bottomsheet.RatingBottomSheet
import com.tokopedia.internal_review.view.bottomsheet.ThankYouBottomSheet
import com.tokopedia.internal_review.view.viewmodel.ReviewViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession

fun createReviewHelper(context: Context?) : InternalReviewHelper? {
    if (context == null) return null

    val cacheHandler = SellerReviewCacheHandler(context)
    val userSession = UserSession(context)
    val remoteConfig = ReviewRemoteConfig(FirebaseRemoteConfigImpl(context))
    val dispatcher = CoroutineDispatchersProvider

    return InternalReviewHelper(cacheHandler, userSession, remoteConfig, dispatcher)
}

fun createReviewTracking(userSession: UserSession) : ReviewTracking {
    if (GlobalConfig.isSellerApp()) {
        return SellerReviewTracking(userSession)
    } else {
        return CustomerReviewTracking(userSession)
    }
}

fun createReviewViewModel(storeOwner: ViewModelStoreOwner) : ReviewViewModel {
    val gqlRepository = GraphqlInteractor.getInstance().graphqlRepository
    val reviewUseCase = SendReviewUseCase(gqlRepository)
    val dispatcher = CoroutineDispatchersProvider
    val viewModelFactory = ReviewViewModelFactory(reviewUseCase, dispatcher)

    return ViewModelProvider(storeOwner, viewModelFactory).get(ReviewViewModel::class.java)
}

fun createReviewFeedbackBottomSheet(context: Context) : FeedbackBottomSheet {
    val userSession = UserSession(context)
    val reviewTracking = createReviewTracking(userSession)
    return FeedbackBottomSheet(reviewTracking, userSession)
}

fun createReviewRatingBottomSheet(context: Context) : RatingBottomSheet {
    val userSession = UserSession(context)
    val reviewTracking = createReviewTracking(userSession)
    return RatingBottomSheet(reviewTracking, userSession)
}

fun createReviewThankyouBottomSheet(context: Context) : ThankYouBottomSheet {
    val userSession = UserSession(context)
    val reviewTracking = createReviewTracking(userSession)
    return ThankYouBottomSheet(reviewTracking, userSession)
}





