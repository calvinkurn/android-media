package com.tokopedia.product.manage.item.main.base.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.goldmerchant.GoldMerchantService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.product.manage.item.common.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.product.manage.item.common.data.source.UploadImageDataSource;
import com.tokopedia.product.manage.item.common.domain.UploadImageRepository;
import com.tokopedia.product.manage.item.common.domain.interactor.UploadImageUseCase;
import com.tokopedia.product.manage.item.common.domain.repository.GenerateHostRepository;
import com.tokopedia.product.manage.item.main.base.data.repository.GenerateHostRepositoryImpl;
import com.tokopedia.product.manage.item.main.base.data.repository.ProductRepositoryImpl;
import com.tokopedia.product.manage.item.main.base.data.source.GenerateHostDataSource;
import com.tokopedia.product.manage.item.main.base.data.source.ProductDataSource;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.product.manage.item.main.base.data.source.cloud.model.UploadImageModel;
import com.tokopedia.product.manage.item.main.base.di.scope.AddProductServiceScope;
import com.tokopedia.product.manage.item.main.base.domain.ProductRepository;
import com.tokopedia.product.manage.item.main.base.domain.SubmitProductUseCase;
import com.tokopedia.product.manage.item.main.base.domain.mapper.ProductUploadMapper;
import com.tokopedia.product.manage.item.main.base.view.presenter.AddProductServicePresenter;
import com.tokopedia.product.manage.item.main.base.view.presenter.AddProductServicePresenterImpl;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.DeleteSingleDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.FetchDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;
import com.tokopedia.product.manage.item.variant.data.repository.ProductVariantRepository;
import com.tokopedia.product.manage.item.variant.data.repository.ProductVariantRepositoryImpl;
import com.tokopedia.product.manage.item.variant.data.source.ProductVariantDataSource;
import com.tokopedia.product.manage.item.video.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.productdraftdatabase.ProductDraftDB;
import com.tokopedia.productdraftdatabase.ProductDraftDao;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/20/17.
 */
@AddProductServiceScope
@Module
public class AddProductserviceModule {

    @AddProductServiceScope
    @Provides
    AddProductServicePresenter provideAddProductServicePresenter(FetchDraftProductUseCase fetchDraftProductUseCase,
                                                                 SubmitProductUseCase uploadProductUseCase,
                                                                 DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                                                 UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                                                 ProductUploadMapper productUploadMapper) {
        return new AddProductServicePresenterImpl(fetchDraftProductUseCase, uploadProductUseCase, deleteSingleDraftProductUseCase, updateUploadingDraftProductUseCase, productUploadMapper);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context) {
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostRepository provideGenerateHostRepository(GenerateHostDataSource generateHostDataSource) {
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @AddProductServiceScope
    @Provides
    ProductRepository provideUploadProductRepository(ProductDataSource productDataSource,
                                                     FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        return new ProductRepositoryImpl(productDataSource, fetchVideoEditProductDataSource);
    }

    @AddProductServiceScope
    @Provides
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit) {
        return retrofit.create(GenerateHostApi.class);
    }

    @AddProductServiceScope
    @Provides
    ProductVariantRepository productVariantRepository(ProductVariantDataSource productVariantDataSource) {
        return new ProductVariantRepositoryImpl(productVariantDataSource);
    }

    @AddProductServiceScope
    @Provides
    GoldMerchantService productGoldMerchantService() {
        return new GoldMerchantService();
    }

    @AddProductServiceScope
    @Provides
    UploadImageUseCase<UploadImageModel> provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                                   PostExecutionThread postExecutionThread,
                                                                   UploadImageRepository uploadImageRepository,
                                                                   GenerateHostRepository generateHostRepository,
                                                                   Gson gson,
                                                                   NetworkCalculator networkCalculator) {
        return new UploadImageUseCase<UploadImageModel>(threadExecutor, postExecutionThread, uploadImageRepository,
                generateHostRepository, gson, networkCalculator, UploadImageModel.class);
    }

    @AddProductServiceScope
    @Provides
    UploadImageRepository provideUploadImageRepository(UploadImageDataSource uploadImageDataSource) {
        return new UploadImageRepositoryImpl(uploadImageDataSource);
    }

    @AddProductServiceScope
    @Provides
    NetworkCalculator provideNetworkCalculator(@ApplicationContext Context context,
                                               UploadImageDataSource uploadImageDataSource) {
        return new NetworkCalculator(NetworkConfig.POST, context, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL).setIdentity().compileAllParam().finish();
    }

    @AddProductServiceScope
    @Provides
    ProductUploadMapper provideProductUploadMapper() {
        return new ProductUploadMapper();
    }

    @AddProductServiceScope
    @Provides
    FetchDraftProductUseCase provideFetchDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        return new FetchDraftProductUseCase(productDraftRepository);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftDB provideProductDraftDb(@ApplicationContext Context context){
        return ProductDraftDB.getInstance(context);
    }

    @AddProductServiceScope
    @Provides
    ProductDraftDao provideProductDraftDao(ProductDraftDB productDraftDB){
        return productDraftDB.getProductDraftDao();
    }
}