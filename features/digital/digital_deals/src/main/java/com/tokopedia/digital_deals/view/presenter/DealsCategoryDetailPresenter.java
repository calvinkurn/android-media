package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.GetCategoryDetailRequestUseCase;
import com.tokopedia.digital_deals.domain.GetNextCategoryPageUseCase;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.domain.model.categorydomainmodel.CategoryDetailsDomain;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealsCategoryDetailContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PageViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class DealsCategoryDetailPresenter extends BaseDaggerPresenter<DealsCategoryDetailContract.View>
        implements DealsCategoryDetailContract.Presenter {

    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase;
    private GetNextCategoryPageUseCase getNextCategoryPageUseCase;
    private ArrayList<CategoryItemsViewModel> categoryViewModels;
    private List<BrandViewModel> brandViewModels;
    private PageViewModel pageViewModel;
    private String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    private String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    private String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";
    public final static String TAG = "url";
    private boolean isLoading;
    private boolean isLastPage;
    private final int PAGE_SIZE = 20;
    RequestParams searchNextParams = RequestParams.create();
    private volatile boolean isDealsLoaded = false;
    private volatile boolean isBrandsLoaded = false;


    @Inject
    public DealsCategoryDetailPresenter(GetCategoryDetailRequestUseCase getCategoryDetailRequestUseCase, GetNextCategoryPageUseCase getNextCategoryPageUseCase, GetAllBrandsUseCase getAllBrandsUseCase) {
        this.getCategoryDetailRequestUseCase = getCategoryDetailRequestUseCase;
        this.getNextCategoryPageUseCase = getNextCategoryPageUseCase;
        this.getAllBrandsUseCase=getAllBrandsUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getCategoryDetailRequestUseCase.unsubscribe();
        getNextCategoryPageUseCase.unsubscribe();
        getAllBrandsUseCase.unsubscribe();
    }


    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.search_input_view) {
//            ArrayList<SearchViewModel> searchViewModelList = Utils.getSingletonInstance().convertIntoSearchViewModel(categoryViewModels);
            Intent searchIntent = new Intent(getView().getActivity(), DealsSearchActivity.class);

            searchIntent.putParcelableArrayListExtra("TOPDEALS", categoryViewModels);
            getView().navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
        } else if (id == R.id.tv_see_all) {

        } else if (id == R.id.action_promo) {
            getView().startGeneralWebView(PROMOURL);
        } else if (id == R.id.action_booked_history) {
            getView().startGeneralWebView(TRANSATIONSURL);
        } else if (id == R.id.action_faq) {
            getView().startGeneralWebView(FAQURL);
        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    public void getBrandsList() {
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
                        getBrandsList();
                    }
                });
            }

            @Override
            public void onNext(AllBrandsDomain dealEntity) {
                isBrandsLoaded = true;
                brandViewModels = Utils.getSingletonInstance().convertIntoBrandListViewModel(dealEntity.getBrands());
                getView().renderBrandList(brandViewModels);
                showHideViews();
                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public void getCategoryDetails() {
        getView().showProgressBar();
        getCategoryDetailRequestUseCase.execute(getView().getParams(), new Subscriber<CategoryDetailsDomain>() {

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
                        getCategoryDetails();
                    }
                });
            }

            @Override
            public void onNext(CategoryDetailsDomain dealEntity) {
                isDealsLoaded=true;
                isBrandsLoaded=true;        //to be removed
                categoryViewModels = Utils.getSingletonInstance()
                        .convertIntoCategoryListItemsViewModel(dealEntity.getDealItems());
                brandViewModels = Utils.getSingletonInstance().convertIntoBrandListViewModel(dealEntity.getDealBrands()); //to be removed
                pageViewModel = Utils.getSingletonInstance().convertIntoPageViewModel(dealEntity.getPage());
                getNextPageUrl();
                getView().renderCategoryList(categoryViewModels);
                getView().renderBrandList(brandViewModels);     //to be removed
                checkIfToLoad(getView().getLayoutManager());
                showHideViews();
            }
        });
    }



    private void loadMoreItems() {
        isLoading = true;

        getNextCategoryPageUseCase.execute(searchNextParams, new Subscriber<CategoryDetailsDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isLoading = false;
            }

            @Override
            public void onNext(CategoryDetailsDomain categoryDetailsDomain) {
                isLoading = false;
                ArrayList<CategoryItemsViewModel> categoryList = Utils.getSingletonInstance()
                        .convertIntoCategoryListItemsViewModel(categoryDetailsDomain.getDealItems());
                pageViewModel = Utils.getSingletonInstance().convertIntoPageViewModel(categoryDetailsDomain.getPage());
                getView().removeFooter();
                getNextPageUrl();
                getView().addDealsToCards(categoryList);
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

    private void showHideViews() {
        if (isBrandsLoaded && isDealsLoaded) {
            getView().hideProgressBar();
            getView().showViews();
        }
    }

}

