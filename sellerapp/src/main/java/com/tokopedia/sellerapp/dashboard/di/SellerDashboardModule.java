package com.tokopedia.sellerapp.dashboard.di;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.common.logout.di.scope.TkpdSellerLogoutScope;
import com.tokopedia.seller.goldmerchant.statistic.data.repository.GMStatRepositoryImpl;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.seller.product.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.seller.product.category.data.source.CategoryDataSource;
import com.tokopedia.seller.product.category.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.product.category.data.source.FetchCategoryDataSource;
import com.tokopedia.seller.product.category.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.seller.product.category.domain.CategoryRepository;
import com.tokopedia.seller.product.category.domain.interactor.ClearCategoryCacheUseCase;
import com.tokopedia.seller.product.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/8/17.
 */

@SellerDashboardScope
@Module
public class SellerDashboardModule {

}
