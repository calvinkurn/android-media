package com.tokopedia.home.beranda.domain.interactor.repository

import android.os.Bundle
import com.tokopedia.home.beranda.domain.gql.ProductrevDismissSuggestion
import com.tokopedia.home.beranda.domain.interactor.DismissHomeReviewUseCase
import com.tokopedia.home.beranda.domain.interactor.HomeRepository
import javax.inject.Inject

class HomeDismissReviewSuggestedRepository @Inject constructor(
        private val dismissHomeReviewUseCase: DismissHomeReviewUseCase
): HomeRepository<ProductrevDismissSuggestion> {
    override suspend fun getRemoteData(bundle: Bundle): ProductrevDismissSuggestion {
        return dismissHomeReviewUseCase.executeOnBackground()
    }

    override suspend fun getCachedData(bundle: Bundle): ProductrevDismissSuggestion {
        return ProductrevDismissSuggestion()
    }
}