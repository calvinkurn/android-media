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
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDao
import com.tokopedia.product.manage.common.feature.draft.data.db.AddEditProductDraftDb
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepository
import com.tokopedia.product.manage.common.feature.draft.data.db.repository.AddEditProductDraftRepositoryImpl
import com.tokopedia.product.manage.common.feature.draft.data.db.source.AddEditProductDraftDataSource
import com.tokopedia.product.manage.feature.list.constant.GQL_FEATURED_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.GQL_UPDATE_PRODUCT
import com.tokopedia.product.manage.feature.list.constant.ProductManageListConstant
import com.tokopedia.product.manage.feature.multiedit.domain.MultiEditProductUseCase
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module(includes = [ProductManageNetworkModule::class, ViewModelModule::class])
class ProductManageListModule(private val context: Context) {

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
    fun provideProductListQuery(): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_get_product_list
        )
    }

    @ProductManageListScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    fun provideGqlQueryShopInfo(): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_get_shop_info)
    }

    @ProductManageListScope
    @Provides
    @Named(GQL_FEATURED_PRODUCT)
    fun provideGqlMutationFeaturedProduct(): String {
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

    @ProductManageListScope
    @Provides
    fun provideRemoteConfig(@ApplicationContext context: Context): FirebaseRemoteConfigImpl {
        return FirebaseRemoteConfigImpl(context)
    }

    @ProductManageListScope
    @Provides
    fun provideProductDraftDb(@ApplicationContext context: Context): AddEditProductDraftDb = AddEditProductDraftDb.getInstance(context)

    @ProductManageListScope
    @Provides
    fun provideProductDraftDao(draftDb: AddEditProductDraftDb): AddEditProductDraftDao = draftDb.getDraftDao()

    @ProductManageListScope
    @Provides
    fun provideProductDraftRepository(
            draftDataSource: AddEditProductDraftDataSource,
            userSession: UserSessionInterface
    ): AddEditProductDraftRepository {
        return AddEditProductDraftRepositoryImpl(draftDataSource, userSession)
    }
}