package com.tokopedia.homenav.mainnav.view.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.mainnav.view.datamodel.*
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel

interface MainNavTypeFactory {

    fun type(accountHeaderDataModel: AccountHeaderDataModel): Int

    fun type(separatorDataModel: SeparatorDataModel) : Int

    fun type(transactionListItemDataModel: TransactionListItemDataModel) : Int

    fun type(wishlistListItemDataModel: WishlistListItemDataModel): Int

    fun type(favoriteShopListItemDataModel: FavoriteShopListItemDataModel): Int

    fun type(initialShimmerDataModel: InitialShimmerDataModel) : Int

    fun type(initialShimmerProfileDataModel: InitialShimmerProfileDataModel) : Int

    fun type(initialShimmerTransactionDataModel: InitialShimmerTransactionDataModel) : Int

    fun type(initialShimmerFavoriteShopDataModel: InitialShimmerFavoriteShopDataModel) : Int

    fun type(errorStateBuDataModel: ErrorStateBuDataModel): Int

    fun type(errorStateOngoingTransactionModel: ErrorStateOngoingTransactionModel): Int

    fun type(errorStateFavoriteShopDataModel: ErrorStateFavoriteShopDataModel): Int

    fun createViewHolder(view: View, viewType: Int) : AbstractViewHolder<*>
}