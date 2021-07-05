package com.tokopedia.attachproduct.domain.usecase;

import com.tokopedia.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.attachproduct.domain.model.mapper.DataModelToDomainModelMapper;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 14/02/18.
 */

public class AttachProductUseCase extends UseCase<List<AttachProductItemUiModel>> {
    private static final String KEYWORD_KEY = "q";
    private static final String SHOP_ID_KEY = "shop_id";
    private static final String PAGE_KEY = "start";
    private static final String SOURCE_KEY = "source";
    private static final String DEVICE_KEY = "device";
    private static final String PER_PAGE_KEY = "rows";
    private final AttachProductRepository repository;
    private final DataModelToDomainModelMapper mapper;

    @Inject
    public AttachProductUseCase(AttachProductRepository repository, DataModelToDomainModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<AttachProductItemUiModel>> createObservable(RequestParams requestParams) {
        return repository.loadProductFromShop(requestParams.getParamsAllValueInString()).map(mapper);
    }

    public static RequestParams createRequestParams(String query, String shopId, int page) {
        RequestParams param = RequestParams.create();
        param.putString(KEYWORD_KEY, query);
        param.putString(SHOP_ID_KEY, shopId);
        param.putString(PAGE_KEY, String.valueOf((page-1) * 10));
        param.putString(SOURCE_KEY, "attach_product");
        param.putString(PER_PAGE_KEY, "11");
        param.putString(DEVICE_KEY, "android");
        return param;
    }
}
