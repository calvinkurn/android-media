package com.tokopedia.seller.action.balance.presentation.mapper

import android.content.Context
import android.net.Uri
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.seller.action.balance.presentation.model.SellerActionBalance
import com.tokopedia.seller.action.balance.presentation.slice.SellerBalanceSlice
import com.tokopedia.seller.action.common.presentation.mapper.SellerActionMapper
import com.tokopedia.seller.action.common.presentation.model.SellerSuccessItem
import com.tokopedia.seller.action.common.presentation.slices.SellerSlice

class SellerBalanceMapper(context: Context,
                          sliceUri: Uri, private
                          val remoteConfig: FirebaseRemoteConfigImpl): SellerActionMapper<SellerActionBalance>(context, sliceUri) {

    override fun <T : SellerSuccessItem> getSuccessSlice(itemList: List<T>): SellerSlice {
        return SellerBalanceSlice(context, sliceUri, itemList.filterIsInstance(SellerActionBalance::class.java), remoteConfig)
    }
}