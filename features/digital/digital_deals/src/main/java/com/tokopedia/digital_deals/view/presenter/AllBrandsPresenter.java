package com.tokopedia.digital_deals.view.presenter;

import android.support.v7.widget.LinearLayoutManager;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.getusecase.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.getusecase.GetNextBrandPageUseCase;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.view.contractor.AllBrandsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PageViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class AllBrandsPresenter extends BaseDaggerPresenter<AllBrandsContract.View>
        implements AllBrandsContract.Presenter {

    private boolean isLoading;
    private boolean isLastPage;
    private final int PAGE_SIZE = 20;
    public final static String TAG = "url";
    private boolean SEARCH_SUBMITTED;

    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetNextBrandPageUseCase getNextAllBrandPageUseCase;
    private List<BrandViewModel> brandViewModels;
    private PageViewModel pageViewModel;
    private RequestParams searchNextParams = RequestParams.create();


    @Inject
    public AllBrandsPresenter(GetAllBrandsUseCase getAllBrandsUseCase, GetNextBrandPageUseCase getNextBrandPageUseCase) {
        this.getAllBrandsUseCase = getAllBrandsUseCase;
        this.getNextAllBrandPageUseCase =getNextBrandPageUseCase;
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
        getAllBrandsUseCase.execute(getView().getParams(), new Subscriber<AllBrandsDomain>() {

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
            public void onNext(AllBrandsDomain dealEntity) {
                getView().hideProgressBar();

                brandViewModels = Utils.getSingletonInstance().convertIntoBrandListViewModel(dealEntity.getBrands());
                pageViewModel = Utils.getSingletonInstance().convertIntoPageViewModel(dealEntity.getPage());
                getNextPageUrl();
                getView().renderBrandList(brandViewModels, SEARCH_SUBMITTED);


            }
        });
    }

    private void loadMoreItems() {
        isLoading = true;

        getNextAllBrandPageUseCase.execute(searchNextParams, new Subscriber<AllBrandsDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isLoading = false;
            }

            @Override
            public void onNext(AllBrandsDomain allBrandsDomain) {
                isLoading = false;
                List<BrandViewModel> brandViewModels1 = Utils.getSingletonInstance().convertIntoBrandListViewModel(allBrandsDomain.getBrands());
                pageViewModel = Utils.getSingletonInstance().convertIntoPageViewModel(allBrandsDomain.getPage());
                getView().removeFooter();
                getNextPageUrl();
                brandViewModels.addAll(brandViewModels1);
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
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            } else {
                getView().addFooter();
            }
        }
    }

    void getNextPageUrl() {

        String nexturl = pageViewModel.getUriNext();
        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
            searchNextParams.putString(TAG, nexturl);
            isLastPage = false;
        } else {
            isLastPage = true;
        }
    }

    public void getBrandListBySearch(String searchText) {
        List<BrandViewModel> brandModels = new ArrayList<>();
        if(brandViewModels!=null) {
            for (BrandViewModel brand : brandViewModels) {
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
                getView().renderBrandList(brandViewModels, SEARCH_SUBMITTED);
            }
        } else {
            getView().renderBrandList(brandViewModels, SEARCH_SUBMITTED);
        }
    }

    @Override
    public void searchSubmitted(String searchText) {
        SEARCH_SUBMITTED = true;
        getBrandListBySearch(searchText);

    }

}
