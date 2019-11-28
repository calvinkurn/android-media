package com.tokopedia.product.manage.list.di

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl
import com.tokopedia.gm.common.data.source.GMCommonDataSource
import com.tokopedia.gm.common.domain.interactor.SetCashbackUseCase
import com.tokopedia.gm.common.domain.repository.GMCommonRepository
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDB
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDB.Companion.getInstance
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDao
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase
import com.tokopedia.product.manage.list.R
import com.tokopedia.product.manage.list.constant.GQL_UPDATE_PRODUCT
import com.tokopedia.product.manage.list.constant.ProductManageListConstant.GQL_POPUP_NAME
import com.tokopedia.product.manage.list.domain.BulkUpdateProductUseCase
import com.tokopedia.product.manage.list.domain.EditPriceUseCase
import com.tokopedia.product.manage.list.domain.PopupManagerAddProductUseCase
import com.tokopedia.product.manage.list.view.mapper.ProductListMapperView
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenter
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenterImpl
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenterImpl
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.constant.ShopCommonParamApiConstant
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GetProductListUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by zulfikarrahman on 9/26/17.
 */
@Module(includes = [ProductManageNetworkModule::class])
@ProductManageScope
class ProductManageModule {
    @Provides
    @ProductManageScope
    fun provideManageProductPresenter(editPriceUseCase: EditPriceUseCase?,
                                      gqlGetShopInfoUseCase: GQLGetShopInfoUseCase?,
                                      userSession: UserSessionInterface?,
                                      topAdsGetShopDepositGraphQLUseCase: TopAdsGetShopDepositGraphQLUseCase?,
                                      setCashbackUseCase: SetCashbackUseCase?,
                                      popupManagerAddProductUseCase: PopupManagerAddProductUseCase?,
                                      getProductListUseCase: GetProductListUseCase?,
                                      productListMapperView: ProductListMapperView?,
                                      bulkUpdateProductUseCase: BulkUpdateProductUseCase?): ProductManagePresenter {
        return ProductManagePresenterImpl(editPriceUseCase!!, gqlGetShopInfoUseCase!!, userSession!!, topAdsGetShopDepositGraphQLUseCase!!,
            setCashbackUseCase!!, popupManagerAddProductUseCase!!, getProductListUseCase!!, productListMapperView!!, bulkUpdateProductUseCase!!)
    }

    @Provides
    internal fun providePresenterDraft(fetchAllDraftProductCountUseCase: FetchAllDraftProductCountUseCase?,
                                       clearAllDraftProductUseCase: ClearAllDraftProductUseCase?,
                                       updateUploadingDraftProductUseCase: UpdateUploadingDraftProductUseCase?): ProductDraftListCountPresenter {
        return ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase,
            clearAllDraftProductUseCase, updateUploadingDraftProductUseCase)
    }

    @ProductManageScope
    @Provides
    fun provideUserSessionInterface(@ApplicationContext context: Context?): UserSessionInterface {
        return UserSession(context)
    }

    @ProductManageScope
    @Provides
    fun provideMultiRequestGraphqlUseCase(): MultiRequestGraphqlUseCase {
        return GraphqlInteractor.getInstance().multiRequestGraphqlUseCase
    }

    @Provides
    @ProductManageScope
    fun provideGraphqlUseCase(): GraphqlUseCase {
        return GraphqlUseCase()
    }

    @Provides
    @ProductManageScope
    fun provideGqlGetShopInfoUseCase(graphqlUseCase: MultiRequestGraphqlUseCase?,
                                     @Named(GQLQueryNamedConstant.SHOP_INFO) gqlQuery: String?): GQLGetShopInfoUseCase {
        return GQLGetShopInfoUseCase(gqlQuery!!, graphqlUseCase!!)
    }

    @Provides
    @ProductManageScope
    fun provideGmCommonRepository(gmCommonDataSource: GMCommonDataSource?): GMCommonRepository {
        return GMCommonRepositoryImpl(gmCommonDataSource)
    }

    @Provides
    @ProductManageScope
    fun provideTopAdsSourceTracking(@ApplicationContext context: Context?): TopAdsSourceTaggingLocal {
        return TopAdsSourceTaggingLocal(context)
    }

    @Provides
    @ProductManageScope
    fun provideTopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal: TopAdsSourceTaggingLocal?): TopAdsSourceTaggingDataSource {
        return TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal)
    }

    @Provides
    @ProductManageScope
    fun provideTopAdsSourceTaggingRepository(dataSource: TopAdsSourceTaggingDataSource?): TopAdsSourceTaggingRepository {
        return TopAdsSourceTaggingRepositoryImpl(dataSource)
    }

    @ProductManageScope
    @Provides
    internal fun provideProductDraftRepository(productDraftDataSource: ProductDraftDataSource?, @ApplicationContext context: Context?): ProductDraftRepository {
        return ProductDraftRepositoryImpl(productDraftDataSource, context)
    }

    @ProductManageScope
    @Provides
    internal fun provideProductDraftDb(@ApplicationContext context: Context?): ProductDraftDB {
        return getInstance(context!!)
    }

    @ProductManageScope
    @Provides
    internal fun provideProductDraftDao(productDraftDB: ProductDraftDB): ProductDraftDao {
        return productDraftDB.getProductDraftDao()
    }

    @ProductManageScope
    @Provides
    @Named(GQL_POPUP_NAME)
    fun requestQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            R.raw.gql_popup_manager
        )
    }

    @ProductManageScope
    @Provides
    @Named(GQL_UPDATE_PRODUCT)
    fun provideUpdateProduct(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            R.raw.gql_mutation_edit_product
        )
    }

    @ProductManageScope
    @Provides
    @Named(ShopCommonParamApiConstant.GQL_PRODUCT_LIST)
    fun provideProductListQuery(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_get_product_list
        )
    }

    @ProductManageScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    fun provideGqlQueryShopInfo(@ApplicationContext context: Context): String {
        return GraphqlHelper.loadRawString(
            context.resources,
            com.tokopedia.shop.common.R.raw.gql_get_shop_info)
    }
}