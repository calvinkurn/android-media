package com.tokopedia.product.manage.list.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.gm.common.data.repository.GMCommonRepositoryImpl;
import com.tokopedia.gm.common.data.source.GMCommonDataSource;
import com.tokopedia.gm.common.domain.interactor.SetCashbackUseCase;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor;
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDB;
import com.tokopedia.product.manage.item.main.draft.data.db.ProductDraftDao;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;
import com.tokopedia.product.manage.list.R;
import com.tokopedia.product.manage.list.domain.BulkUpdateProductUseCase;
import com.tokopedia.product.manage.list.domain.EditFeaturedProductUseCase;
import com.tokopedia.product.manage.list.domain.EditPriceUseCase;
import com.tokopedia.product.manage.list.domain.PopupManagerAddProductUseCase;
import com.tokopedia.product.manage.list.view.mapper.ProductListMapperView;
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenter;
import com.tokopedia.product.manage.list.view.presenter.ProductManagePresenterImpl;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenterImpl;
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant;
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase;
import com.tokopedia.shop.common.domain.interactor.GetProductListUseCase;
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase;
import com.tokopedia.topads.sourcetagging.data.repository.TopAdsSourceTaggingRepositoryImpl;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingDataSource;
import com.tokopedia.topads.sourcetagging.data.source.TopAdsSourceTaggingLocal;
import com.tokopedia.topads.sourcetagging.domain.repository.TopAdsSourceTaggingRepository;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.product.manage.list.constant.GqlRawConstantKt.GQL_FEATURED_PRODUCT;
import static com.tokopedia.product.manage.list.constant.GqlRawConstantKt.GQL_UPDATE_PRODUCT;
import static com.tokopedia.product.manage.list.constant.ProductManageListConstant.GQL_POPUP_NAME;
import static com.tokopedia.shop.common.constant.ShopCommonParamApiConstant.GQL_PRODUCT_LIST;

/**
 * Created by zulfikarrahman on 9/26/17.
 */

@Module(includes = {ProductManageNetworkModule.class})
@ProductManageScope
public class ProductManageModule {
    @Provides
    @ProductManageScope
    public ProductManagePresenter provideManageProductPresenter(EditPriceUseCase editPriceUseCase,
                                                                GQLGetShopInfoUseCase gqlGetShopInfoUseCase,
                                                                UserSessionInterface userSession,
                                                                TopAdsGetShopDepositGraphQLUseCase topAdsGetShopDepositGraphQLUseCase,
                                                                SetCashbackUseCase setCashbackUseCase,
                                                                PopupManagerAddProductUseCase popupManagerAddProductUseCase,
                                                                GetProductListUseCase getProductListUseCase,
                                                                ProductListMapperView productListMapperView,
                                                                BulkUpdateProductUseCase bulkUpdateProductUseCase,
                                                                EditFeaturedProductUseCase editFeaturedProductUseCase) {
        return new ProductManagePresenterImpl(editPriceUseCase, gqlGetShopInfoUseCase, userSession, topAdsGetShopDepositGraphQLUseCase,
                setCashbackUseCase, popupManagerAddProductUseCase, getProductListUseCase, productListMapperView, bulkUpdateProductUseCase, editFeaturedProductUseCase);
    }

    @Provides
    ProductDraftListCountPresenter providePresenterDraft(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                                         ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                                         UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        return new ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase,
                clearAllDraftProductUseCase, updateUploadingDraftProductUseCase);
    }

    @ProductManageScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @ProductManageScope
    @Provides
    public MultiRequestGraphqlUseCase provideMultiRequestGraphqlUseCase() {
        return GraphqlInteractor.getInstance().getMultiRequestGraphqlUseCase();
    }

    @Provides
    @ProductManageScope
    public GraphqlUseCase provideGraphqlUseCase() {
        return new GraphqlUseCase();
    }

    @Provides
    @ProductManageScope
    public GQLGetShopInfoUseCase provideGqlGetShopInfoUseCase(MultiRequestGraphqlUseCase graphqlUseCase,
                                                              @Named(GQLQueryNamedConstant.SHOP_INFO)
                                                                      String gqlQuery) {
        return new GQLGetShopInfoUseCase(gqlQuery, graphqlUseCase);
    }

    @Provides
    @ProductManageScope
    public GMCommonRepository provideGmCommonRepository(GMCommonDataSource gmCommonDataSource) {
        return new GMCommonRepositoryImpl(gmCommonDataSource);
    }

    @Provides
    @ProductManageScope
    public TopAdsSourceTaggingLocal provideTopAdsSourceTracking(@ApplicationContext Context context) {
        return new TopAdsSourceTaggingLocal(context);
    }

    @Provides
    @ProductManageScope
    public TopAdsSourceTaggingDataSource provideTopAdsSourceTaggingDataSource(TopAdsSourceTaggingLocal topAdsSourceTaggingLocal) {
        return new TopAdsSourceTaggingDataSource(topAdsSourceTaggingLocal);
    }

    @Provides
    @ProductManageScope
    public TopAdsSourceTaggingRepository provideTopAdsSourceTaggingRepository(TopAdsSourceTaggingDataSource dataSource) {
        return new TopAdsSourceTaggingRepositoryImpl(dataSource);
    }

    @ProductManageScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductManageScope
    @Provides
    ProductDraftDB provideProductDraftDb(@ApplicationContext Context context){
        return ProductDraftDB.getInstance(context);
    }

    @ProductManageScope
    @Provides
    ProductDraftDao provideProductDraftDao(ProductDraftDB productDraftDB){
        return productDraftDB.getProductDraftDao();
    }

    @ProductManageScope
    @Provides
    @Named(GQL_POPUP_NAME)
    public String requestQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.gql_popup_manager
        );
    }

    @ProductManageScope
    @Provides
    @Named(GQL_UPDATE_PRODUCT)
    public String provideUpdateProduct(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.gql_mutation_edit_product
        );
    }

    @ProductManageScope
    @Provides
    @Named(GQL_PRODUCT_LIST)
    public String provideProductListQuery(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(
                context.getResources(),
                com.tokopedia.shop.common.R.raw.gql_get_product_list
        );
    }

    @ProductManageScope
    @Provides
    @Named(GQLQueryNamedConstant.SHOP_INFO)
    public String provideGqlQueryShopInfo(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(
                context.getResources(),
                com.tokopedia.shop.common.R.raw.gql_get_shop_info);
    }

    @ProductManageScope
    @Provides
    @Named(GQL_FEATURED_PRODUCT)
    public String provideGqlMutationFeaturedProduct(@ApplicationContext Context context) {
        return GraphqlHelper.loadRawString(
                context.getResources(),
                R.raw.gql_mutation_gold_manage_featured_product_v2
        );
    }
}
