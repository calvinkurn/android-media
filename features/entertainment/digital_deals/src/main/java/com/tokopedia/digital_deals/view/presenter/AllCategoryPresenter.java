package com.tokopedia.digital_deals.view.presenter;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.digital_deals.domain.getusecase.GetAllCategoriesUseCase;
import com.tokopedia.digital_deals.domain.postusecase.PostNsqEventUseCase;
import com.tokopedia.digital_deals.view.contractor.AllBrandsHomeContract;
import com.tokopedia.digital_deals.view.model.CategoryItem;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqMessage;
import com.tokopedia.digital_deals.view.model.nsqevents.NsqServiceModel;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.network.data.model.response.DataResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import timber.log.Timber;

public class AllCategoryPresenter extends BaseDaggerPresenter<AllBrandsHomeContract.View>
        implements AllBrandsHomeContract.Presenter {

    private GetAllCategoriesUseCase getAllCategoriesUseCase;
    private PostNsqEventUseCase postNsqEventUseCase;

    @Inject
    public AllCategoryPresenter(GetAllCategoriesUseCase getAllCategoriesUseCase, PostNsqEventUseCase postNsqEventUseCase) {
        this.getAllCategoriesUseCase = getAllCategoriesUseCase;
        this.postNsqEventUseCase = postNsqEventUseCase;
    }
    @Override
    public void initialize() {
    }

    @Override
    public void onDestroy() {
        getAllCategoriesUseCase.unsubscribe();
        postNsqEventUseCase.unsubscribe();
    }

    @Override
    public void getAllCategories() {
        getAllCategoriesUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                Timber.d("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Timber.d("enter error");
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

    public void sendNSQEvent(String userId, String action) {
        NsqServiceModel nsqServiceModel = new NsqServiceModel();
        nsqServiceModel.setService(Utils.NSQ_SERVICE);
        NsqMessage nsqMessage = new NsqMessage();
        nsqMessage.setUserId(Integer.parseInt(userId));
        nsqMessage.setUseCase(Utils.NSQ_USE_CASE);
        nsqMessage.setAction(action);
        nsqServiceModel.setMessage(nsqMessage);
        postNsqEventUseCase.setRequestModel(nsqServiceModel);
        postNsqEventUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.d(e);
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Log.d("Naveen", "NSQ Event Sent All brands");
            }
        });
    }
}
