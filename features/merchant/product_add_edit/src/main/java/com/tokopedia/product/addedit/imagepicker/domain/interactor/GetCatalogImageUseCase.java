package com.tokopedia.product.addedit.imagepicker.domain.interactor;

import com.tokopedia.product.addedit.imagepicker.domain.CatalogImageRepository;
import com.tokopedia.product.addedit.imagepicker.domain.mapper.CatalogImageMapper;
import com.tokopedia.product.addedit.imagepicker.view.model.CatalogModelView;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 6/5/18.
 */

public class GetCatalogImageUseCase extends UseCase<List<CatalogModelView>> {

    public static final String CATALOG_ID = "CATALOG_ID";
    private CatalogImageRepository catalogRepository;
    private CatalogImageMapper catalogImageMapper;

    @Inject
    public GetCatalogImageUseCase(CatalogImageRepository catalogRepository,
                                  CatalogImageMapper catalogImageMapper) {
        this.catalogRepository = catalogRepository;
        this.catalogImageMapper = catalogImageMapper;
    }

    @Override
    public Observable<List<CatalogModelView>> createObservable(RequestParams requestParams) {
        return catalogRepository.getCatalogImage(requestParams.getString(CATALOG_ID, ""))
                .flatMap(catalogImageMapper);
    }

    public RequestParams createRequestParams(String catalogId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CATALOG_ID, catalogId);
        return requestParams;
    }
}
