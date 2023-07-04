package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.base.datamodel.HomeNavGlobalErrorDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavMenuDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTickerDataModel
import com.tokopedia.homenav.base.datamodel.HomeNavTitleDataModel
import com.tokopedia.homenav.base.diffutil.HomeNavTypeFactory
import com.tokopedia.homenav.base.diffutil.holder.HomeNavGlobalErrorViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavMenuViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTickerViewHolder
import com.tokopedia.homenav.base.diffutil.holder.HomeNavTitleViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.*
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.review.ErrorReviewViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.review.ReviewViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist.ErrorWishlistViewHolder
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist.WishlistViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ErrorStateReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ReviewListDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.review.ShimmerReviewDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ErrorStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.ShimmerWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener

class MainNavTypeFactoryImpl(
    private val mainNavListener: MainNavListener,
    private val userSession: UserSessionInterface,
    private val tokopediaPlusListener: TokopediaPlusListener
) :
    HomeNavTypeFactory, MainNavTypeFactory {

    override fun type(accountHeaderDataModel: AccountHeaderDataModel): Int {
        return AccountHeaderViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavMenuDataModel): Int {
        return HomeNavMenuViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavTitleDataModel): Int {
        return HomeNavTitleViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavGlobalErrorDataModel): Int {
        return HomeNavGlobalErrorViewHolder.LAYOUT
    }

    override fun type(separatorDataModel: SeparatorDataModel): Int {
        return SeparatorViewHolder.LAYOUT
    }

    override fun type(transactionListItemDataModel: TransactionListItemDataModel): Int {
        return TransactionListViewHolder.LAYOUT
    }

    override fun type(wishlistDataModel: WishlistDataModel): Int {
        return WishlistViewHolder.LAYOUT
    }

    override fun type(reviewListDataModel: ReviewListDataModel): Int {
        return ReviewViewHolder.LAYOUT
    }

    override fun type(visitable: HomeNavTickerDataModel): Int {
        return HomeNavTickerViewHolder.LAYOUT
    }

    override fun type(initialShimmerDataModel: InitialShimmerDataModel): Int {
        return InitialShimmeringDataViewHolder.LAYOUT
    }

    override fun type(initialShimmerProfileDataModel: InitialShimmerProfileDataModel): Int {
        return InitialShimmeringProfileDataViewHolder.LAYOUT
    }

    override fun type(initialShimmerTransactionRevampDataModel: InitialShimmerTransactionRevampDataModel): Int {
        return InitialShimmeringTransactionDataRevampViewHolder.LAYOUT
    }

    override fun type(initialShimmerTransactionDataModel: InitialShimmerTransactionDataModel): Int {
        return InitialShimmeringTransactionDataViewHolder.LAYOUT
    }

    override fun type(shimmerWishlistDataModel: ShimmerWishlistDataModel): Int {
        return InitialShimmeringTransactionDataRevampViewHolder.LAYOUT
    }

    override fun type(shimmerReviewDataModel: ShimmerReviewDataModel): Int {
        return InitialShimmeringTransactionDataRevampViewHolder.LAYOUT
    }

    override fun type(errorStateBuDataModel: ErrorStateBuDataModel): Int {
        return ErrorStateBuViewHolder.LAYOUT
    }

    override fun type(errorStateOngoingTransactionModel: ErrorStateOngoingTransactionModel): Int {
        return ErrorStateOngoingTransactionViewHolder.LAYOUT
    }

    override fun type(errorStateWishlistDataModel: ErrorStateWishlistDataModel): Int {
        return ErrorWishlistViewHolder.LAYOUT
    }

    override fun type(errorStateReviewDataModel: ErrorStateReviewDataModel): Int {
        return ErrorReviewViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, viewType: Int): AbstractViewHolder<*> {
        return when (viewType) {
            HomeNavMenuViewHolder.LAYOUT -> HomeNavMenuViewHolder(view, mainNavListener)
            AccountHeaderViewHolder.LAYOUT -> AccountHeaderViewHolder(view, mainNavListener, userSession, tokopediaPlusListener)
            SeparatorViewHolder.LAYOUT -> SeparatorViewHolder(view, mainNavListener)
            TransactionListViewHolder.LAYOUT -> TransactionListViewHolder(view, mainNavListener)
            HomeNavTitleViewHolder.LAYOUT -> HomeNavTitleViewHolder(view, mainNavListener)
            HomeNavTickerViewHolder.LAYOUT -> HomeNavTickerViewHolder(view, mainNavListener)
            ErrorStateBuViewHolder.LAYOUT -> ErrorStateBuViewHolder(view, mainNavListener)
            ErrorStateOngoingTransactionViewHolder.LAYOUT -> ErrorStateOngoingTransactionViewHolder(view, mainNavListener)
            InitialShimmeringDataViewHolder.LAYOUT -> InitialShimmeringDataViewHolder(view)
            InitialShimmeringProfileDataViewHolder.LAYOUT -> InitialShimmeringProfileDataViewHolder(view)
            InitialShimmeringTransactionDataRevampViewHolder.LAYOUT -> InitialShimmeringTransactionDataRevampViewHolder(view)
            InitialShimmeringTransactionDataViewHolder.LAYOUT -> InitialShimmeringTransactionDataViewHolder(view)
            WishlistViewHolder.LAYOUT -> WishlistViewHolder(view, mainNavListener)
            ErrorWishlistViewHolder.LAYOUT -> ErrorWishlistViewHolder(view, mainNavListener)
            ReviewViewHolder.LAYOUT -> ReviewViewHolder(view, mainNavListener)
            ErrorReviewViewHolder.LAYOUT -> ErrorReviewViewHolder(view, mainNavListener)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        } as AbstractViewHolder<Visitable<*>>
    }
}
