package com.tokopedia.digital_deals.view.presenter;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetAllCategoriesUseCase;
import com.tokopedia.digital_deals.view.contractor.AllBrandsHomeContract;
import com.tokopedia.digital_deals.view.model.CategoryItem;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class AllCategoryPresenter extends BaseDaggerPresenter<AllBrandsHomeContract.View>
        implements AllBrandsHomeContract.Presenter {

    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    @Inject
    public AllCategoryPresenter(GetAllCategoriesUseCase getAllCategoriesUseCase) {
        this.getAllCategoriesUseCase = getAllCategoriesUseCase;
    }
    @Override
    public void initialize() {
    }

    @Override
    public void onDestroy() {
        getAllCategoriesUseCase.unsubscribe();
    }

    @Override
    public void getAllCategories() {
        getAllCategoriesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getAllCategories();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<List<CategoryItem>>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                List<CategoryItem> categoryItems = (List)dataResponse.getData();
                getView().renderCategoryList(categoryItems);
            }
        });
    }
}
