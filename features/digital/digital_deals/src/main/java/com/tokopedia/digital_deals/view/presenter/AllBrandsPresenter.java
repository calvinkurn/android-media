package com.tokopedia.digital_deals.view.presenter;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextBrandPageUseCase;
import com.tokopedia.digital_deals.view.contractor.AllBrandsContract;
import com.tokopedia.digital_deals.view.model.Brand;
import com.tokopedia.digital_deals.view.model.Page;
import com.tokopedia.digital_deals.view.model.response.AllBrandsResponse;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class AllBrandsPresenter extends BaseDaggerPresenter<AllBrandsContract.View>
        implements AllBrandsContract.Presenter {

    private boolean isLoading;
    private boolean isLastPage;
    public final static String TAG = "url";
    private boolean SEARCH_SUBMITTED;

    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetNextBrandPageUseCase getNextAllBrandPageUseCase;
    private List<Brand> brands;
    private Page page;
    private RequestParams searchNextParams = RequestParams.create();


    @Inject
    public AllBrandsPresenter(GetAllBrandsUseCase getAllBrandsUseCase, GetNextBrandPageUseCase getNextBrandPageUseCase) {
        this.getAllBrandsUseCase = getAllBrandsUseCase;
        this.getNextAllBrandPageUseCase = getNextBrandPageUseCase;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void onDestroy() {
        getAllBrandsUseCase.unsubscribe();
        getNextAllBrandPageUseCase.unsubscribe();
    }


    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.search_input_view) {
        } else if (id == R.id.tv_see_all) {
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    public void getAllBrands() {
        getView().hideEmptyView();
        getView().showProgressBar();
        getAllBrandsUseCase.setRequestParams(getView().getParams());
        getAllBrandsUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {
                CommonUtils.dumper("enter onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("enter error");
                e.printStackTrace();
                getView().hideProgressBar();
                NetworkErrorHelper.showEmptyState(getView().getActivity(), getView().getRootView(), new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        getAllBrands();
                    }
                });
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<AllBrandsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse data = restResponse.getData();
                AllBrandsResponse brandsResponse = (AllBrandsResponse) data.getData();
                getView().hideProgressBar();

                brands = brandsResponse.getBrands();
                page = brandsResponse.getPage();
                getNextPageUrl();
                getView().renderBrandList(brands, SEARCH_SUBMITTED);
            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;
        getNextAllBrandPageUseCase.setRequestParams(searchNextParams);
        getNextAllBrandPageUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<AllBrandsResponse>>() {
                }.getType();
                RestResponse restResponse = typeRestResponseMap.get(token);
                DataResponse dataResponse = restResponse.getData();
                AllBrandsResponse allBrandsResponse = (AllBrandsResponse) dataResponse.getData();
                isLoading = false;
                List<Brand> brandViewModels1 = allBrandsResponse.getBrands();
                page = allBrandsResponse.getPage();
                getView().removeFooter();
                getNextPageUrl();
                if (brandViewModels1 != null)
                    brands.addAll(brandViewModels1);
                getView().addBrandsToCards(brandViewModels1);
                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            } else {
                getView().addFooter();
            }
        }
    }

    private void getNextPageUrl() {

        if (page != null) {
            String nexturl = page.getUriNext();
            if (!TextUtils.isEmpty(nexturl)) {
                searchNextParams.putString(Utils.NEXT_URL, nexturl);
                isLastPage = false;
            } else {
                isLastPage = true;
            }
        }
    }

    private void getBrandListBySearch(String searchText) {
        List<Brand> brandModels = new ArrayList<>();
        if (brands != null) {
            for (Brand brand : brands) {
                if (brand.getTitle().trim().toLowerCase().contains(searchText.trim().toLowerCase())) {
                    brandModels.add(brand);
                }
            }
        }
        getView().renderBrandList(brandModels, SEARCH_SUBMITTED);
    }

    @Override
    public void searchTextChanged(String searchText) {
        SEARCH_SUBMITTED = false;
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 0) {
                getBrandListBySearch(searchText);
            }
            if (searchText.length() == 0) {
                getView().renderBrandList(brands, SEARCH_SUBMITTED);
            }
        } else {
            getView().renderBrandList(brands, SEARCH_SUBMITTED);
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        SEARCH_SUBMITTED = true;
        getBrandListBySearch(searchText);

    }

}
