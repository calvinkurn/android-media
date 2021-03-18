package com.tokopedia.internal_review.factory

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.internal_review.analytics.SellerReviewTracking
import com.tokopedia.internal_review.common.SellerAppReviewRemoteConfig
import com.tokopedia.internal_review.common.SellerReviewCacheHandler
import com.tokopedia.internal_review.common.InternalReviewHelper
import com.tokopedia.internal_review.domain.usecase.SendReviewUseCase
import com.tokopedia.internal_review.view.bottomsheet.FeedbackBottomSheet
import com.tokopedia.internal_review.view.bottomsheet.RatingBottomSheet
import com.tokopedia.internal_review.view.bottomsheet.ThankYouBottomSheet
import com.tokopedia.internal_review.view.viewmodel.ReviewViewModel
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession

public fun createReviewHelper(context: Context) : InternalReviewHelper {
    val cacheHandler = SellerReviewCacheHandler(context)
    val userSession = UserSession(context)
    val remoteConfig = SellerAppReviewRemoteConfig(FirebaseRemoteConfigImpl(context))
    val dispatcher = CoroutineDispatchersProvider

    return InternalReviewHelper(cacheHandler, userSession, remoteConfig, dispatcher)
}

public fun createReviewTracking(userSession: UserSession) : SellerReviewTracking {
    return SellerReviewTracking(userSession)
}

public fun createReviewViewModel(storeOwner: ViewModelStoreOwner) : ReviewViewModel {
    val gqlRepository = GraphqlInteractor.getInstance().graphqlRepository
    val reviewUseCase = SendReviewUseCase(gqlRepository)
    val dispatcher = CoroutineDispatchersProvider
    val viewModelFactory = ReviewViewModelFactory(reviewUseCase, dispatcher)

    return ViewModelProvider(storeOwner, viewModelFactory).get(ReviewViewModel::class.java)
}

public fun createReviewFeedbackBottomSheet(context: Context) : FeedbackBottomSheet {
    val userSession = UserSession(context)
    val reviewTracking = createReviewTracking(userSession)
    return FeedbackBottomSheet(reviewTracking, userSession)
}

public fun createReviewRatingBottomSheet(context: Context) : RatingBottomSheet {
    val userSession = UserSession(context)
    val reviewTracking = createReviewTracking(userSession)
    return RatingBottomSheet(reviewTracking, userSession)
}

public fun createReviewThankyouBottomSheet(context: Context) : ThankYouBottomSheet {
    val userSession = UserSession(context)
    val reviewTracking = createReviewTracking(userSession)
    return ThankYouBottomSheet(reviewTracking, userSession)
}





