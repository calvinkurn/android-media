package com.tokopedia.product.manage.item.category.domain;

import com.tokopedia.product.manage.item.main.base.data.mapper.CategoryRecommDataToDomainMapper;
import com.tokopedia.product.manage.item.main.base.domain.model.CategoryRecommDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/3/17.
 */

public class GetCategoryRecommUseCase extends UseCase<CategoryRecommDomainModel> {

    public static final String TITLE = "title";
    public static final String ROW = "row";
    private final CategoryRecommRepository categoryRecommRepository;
    private final CategoryRecommDataToDomainMapper categoryRecommDataToDomainMapper;

    @Inject
    public GetCategoryRecommUseCase(CategoryRecommRepository categoryRecommRepository,
                                    CategoryRecommDataToDomainMapper categoryRecommDataToDomainMapper) {
        this.categoryRecommRepository = categoryRecommRepository;
        this.categoryRecommDataToDomainMapper = categoryRecommDataToDomainMapper;
    }

    public static RequestParams createRequestParams(String productTitle, int expectRow) {
        RequestParams params = RequestParams.create();
        params.putString(TITLE, productTitle);
        params.putInt(ROW, expectRow);

        return params;
    }

    @Override
    public Observable<CategoryRecommDomainModel> createObservable(RequestParams requestParams) {
        String title = requestParams.getString(TITLE, "");
        int row = requestParams.getInt(ROW, 0);
        return categoryRecommRepository.fetchCategoryRecomm(title, row).map(categoryRecommDataToDomainMapper);
    }

}
