package com.tokopedia.topchat.attachproduct.di;

import com.tokopedia.topchat.attachproduct.data.model.mapper.TkpdResponseToAttachProductDomainModelMapper;
import com.tokopedia.topchat.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.topchat.attachproduct.data.repository.AttachProductRepositoryImpl;
import com.tokopedia.topchat.attachproduct.data.source.service.GetShopProductService;
import com.tokopedia.topchat.attachproduct.domain.model.mapper.DataModelToDomainModelMapper;
import com.tokopedia.topchat.attachproduct.domain.usecase.AttachProductUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hendri on 19/02/18.
 */

@Module
public class AttachProductModule {
    @Provides
    public static GetShopProductService provideShopService(){
        return new GetShopProductService();
    }

    @Provides
    public static AttachProductRepository provideAttachProductRepository(GetShopProductService shopService,TkpdResponseToAttachProductDomainModelMapper mapper){
        return new AttachProductRepositoryImpl(shopService, mapper);
    }

    @Provides
    public static AttachProductUseCase provideAttachProductUseCase(AttachProductRepository repository, DataModelToDomainModelMapper mapper){
        return new AttachProductUseCase(repository,mapper);
    }
}
