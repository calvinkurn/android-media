package com.tokopedia.product.manage.feature.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl
import com.tokopedia.gm.common.data.source.GMCommonDataSource
import com.tokopedia.gm.common.domain.repository.GMCommonRepository
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.feature.cashback.domain.SetCashbackUseCase
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.product.manage.feature.quickedit.delete.domain.DeleteProductUseCase
import com.tokopedia.product.manage.feature.quickedit.price.domain.EditPriceUseCase
import com.tokopedia.product.manage.feature.quickedit.stock.domain.EditStockUseCase
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDB
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDao
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase
import com.tokopedia.product.manage.oldlist.constant.GQL_FEATURED_PRODUCT
import com.tokopedia.product.manage.oldlist.constant.GQL_UPDATE_PRODUCT
import com.tokopedia.product.manage.oldlist.constant.ProductManageListConstant
import com.tokopedia.product.manage.oldlist.domain.ClearAllDraftProductUseCase
import com.tokopedia.product.manage.oldlist.domain.FetchAllDraftProductCountUseCase
import com.tokopedia.product.manage.oldlist.view.presenter.ProductDraftListCountPresenter
import com.tokopedia.product.manage.oldlist.view.presenter.ProductDraftListCountPresenterImpl
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@ProductManageListScope
@Module(includes = [ProductManageNetworkModule::class, ViewModelModule::class])
class ProductManageListModule {

    @Provides
    @ProductManageListScope
    fun providePresenterDraft(
        fetchAllDraftProductCountUseCase: FetchAllDraftProductCountUseCase,
        clearAllDraftProductUseCase: ClearAllDraftProductUseCase,
        updateUploadingDraftProductUseCase: UpdateUploadingDraftProductUseCase
    ): ProductDraftListCountPresenter {
        return ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase,
            clearAllDraftProductUseCase, updateUploadingDraftProductUseCase)
    }

    @ProductManageListScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @Provides
    @ProductManageListScope
    fun provideGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @ProductManageListScope
    fun provideGqlGetShopInfoUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                     @Named(GQLQueryNamedConstant.SHOP_INFO) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @Provides
    @ProductManageListScope
    fun provideGmCommonRepository(gmCommonDataSource: GMCommonDataSource?): GMCommonRepository {
        return GMCommonRepositoryImpl(gmCommonDataSource)
    }

    @Provides
    @ProductManageListScope
    fun provideTopAdsSourceTracking(@ApplicationContext context: Context?): TopAdsSourceTaggingLocal {
        return TopAdsSourceTaggingLocal(context)
    }

    @Provides
    @ProductManageListScope
    fun provideTopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal: TopAdsSourceTaggingLocal?): TopAdsSourceTaggingDataSource {
        return TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal)
    }

    @Provides
    @ProductManageListScope
    fun provideTopAdsSourceTaggingRepository(dataSource: TopAdsSourceTaggingDataSource?): TopAdsSourceTaggingRepository {
        return TopAdsSourceTaggingRepositoryImpl(dataSource)
    }

    @ProductManageListScope
    @Provides
    internal fun provideProductDraftRepository(productDraftDataSource: ProductDraftDataSource?, @ApplicationContext context: Context?): ProductDraftRepository {
        return ProductDraftRepositoryImpl(productDraftDataSource, context)
    }

    @ProductManageListScope
    @Provides
    internal fun provideProductDraftDb(@ApplicationContext context: Context?): ProductDraftDB {
        return ProductDraftDB.getInstance(context!!)
    }

    @ProductManageListScope
    @Provides
    internal fun provideProductDraftDao(productDraftDB: ProductDraftDB): ProductDraftDao {
        return productDraftDB.getProductDraftDao()
    }

    @ProductManageListScope
    @Provides
    fun provideEditStockUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) =
            EditStockUseCase(multiRequestGraphqlUseCase)

    @ProductManageListScope
    @Provides
    fun provideDeleteProductkUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) =
            DeleteProductUseCase(multiRequestGraphqlUseCase)

    @ProductManageListScope
    @Provides
    fun provideEditPriceUseCase(multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase) =
            EditPriceUseCase(multiRequestGraphqlUseCase)

    @ProductManageListScope
    @Provides
    @Named(ProductManageListConstant.GQL_POPUP_NAME)
    fun requestQuery(): String {
        return """
            query GetShopManagerPopups(${'$'}shopID:Int!){
              getShopManagerPopups(shopID: ${'$'}shopID) {
                 data {
                   showPopUp
                 }
              }
            }
        """.trimIndent()
    }

    @ProductManageListScope
    @Provides
    @Named(GQL_UPDATE_PRODUCT)
    fun provideUpdateProduct(): String {
        return """
            mutation productUpdateV3(${'$'}input: ProductInputV3!){
              ProductUpdateV3(input:${'$'}input) {
                header {
                  messages
                  reason
                  errorCode
                }
                isSuccess
              }
            }
        """.trimIndent()
    }

    @ProductManageListScope
    @Provides
    @Named(ShopCommonParamApiConstant.GQL_PRODUCT_LIST)
    fun provideProductListQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_get_product_list
        )
    }

    @ProductManageListScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    fun provideGqlQueryShopInfo(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_get_shop_info)
    }

    @ProductManageListScope
    @Provides
    @Named(GQL_FEATURED_PRODUCT)
    fun provideGqlMutationFeaturedProduct(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_mutation_gold_manage_featured_product_v2
        )
    }

    @ProductManageListScope
    @Provides
    fun provideMultiEditProductUseCase(graphqlRepository: GraphqlRepository): MultiEditProductUseCase {
        return MultiEditProductUseCase(graphqlRepository)
    }
}