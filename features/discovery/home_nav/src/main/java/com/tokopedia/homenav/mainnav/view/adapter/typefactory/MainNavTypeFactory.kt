package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.BuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.datamodel.buyagain.ShimmerBuyAgainUiModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ErrorStateReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel

interface MainNavTypeFactory {

    fun type(accountHeaderDataModel: AccountHeaderDataModel): Int

    fun type(separatorDataModel: SeparatorDataModel) : Int

    fun type(transactionListItemDataModel: TransactionListItemDataModel) : Int

    fun type(reviewListDataModel: ReviewListDataModel): Int

    fun type(initialShimmerDataModel: InitialShimmerDataModel) : Int

    fun type(initialShimmerProfileDataModel: InitialShimmerProfileDataModel) : Int

    fun type(initialShimmerTransactionRevampDataModel: InitialShimmerTransactionRevampDataModel) : Int

    fun type(initialShimmerTransactionDataModel: InitialShimmerTransactionDataModel) : Int

    fun type(shimmerReviewDataModel: ShimmerReviewDataModel) : Int

    fun type(model: BuyAgainUiModel) : Int

    fun type(model: ShimmerBuyAgainUiModel) : Int

    fun type(errorStateOngoingTransactionModel: ErrorStateOngoingTransactionModel): Int

    fun type(errorStateReviewDataModel: ErrorStateReviewDataModel): Int

    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<*>
}
