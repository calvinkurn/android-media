package com.tokopedia.affiliate.feature.dashboard.di

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

@Module
class GqlRawQueryModule {

    @DashboardScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_AFF_PRODUCT_DETAIL)
    fun provideRawProductDetail(): String = """
        query GetCuratedProductDetail(${'$'}affiliatedProductID: Int!){
            affiliatedProductDetail(affiliatedProductID: ${'$'}affiliatedProductID) {
                price
                priceFormatted
                isActive
                commission
                commissionFormatted
                totalClick
                totalSold
                totalCommission
                totalCommissionFormatted
                shopID
                shopName
                productID
                productName
                productImg
            }
        }
    """

    @DashboardScope
    @Provides
    @IntoMap
    @StringKey(RawQueryKeyConstant.QUERY_AFF_PRODUCT_TX_LIST)
    fun provideRawTxList(): String = """
        query GetAffiliatedProductTxList(${'$'}affiliatedProductID: Int!, ${'$'}limit: Int!, ${'$'}next: String){
           affiliatedProductTxList(affiliatedProductID: ${'$'}affiliatedProductID, next: ${'$'}next, limit: ${'$'}limit) {
               history {
                 itemSent
                 affCommission
                 affCommissionFormatted
                 affInvoice
                 affInvoiceURL
                 txTimeFormatted
                 txTime
                 tkpdInvoice
                 tkpdInvoiceURL
                 tkpdCommission
                 tkpdCommissionFormatted
                 netCommission
                 netCommissionFormatted
               }
               next
               has_next
           }
        }
    """


}