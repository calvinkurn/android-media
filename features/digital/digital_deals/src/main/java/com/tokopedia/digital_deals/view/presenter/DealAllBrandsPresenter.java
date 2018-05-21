package com.tokopedia.digital_deals.view.presenter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.GetNextBrandPageUseCase;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealAllBrandsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.PageViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class DealAllBrandsPresenter extends BaseDaggerPresenter<DealAllBrandsContract.View>
        implements DealAllBrandsContract.Presenter {

    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetNextBrandPageUseCase getNextAllBrandPageUseCase;
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
    private boolean SEARCH_SUBMITTED;
    private String highlight;


    @Inject
    public DealAllBrandsPresenter(GetAllBrandsUseCase getAllBrandsUseCase, GetNextBrandPageUseCase getNextBrandPageUseCase) {
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
////            ArrayList<SearchViewModel> searchViewModelList = Utils.getSingletonInstance().convertIntoSearchViewModel(categoryViewModels);
//            Intent searchIntent = new Intent(getView().getActivity(), DealsSearchActivity.class);
//
//            searchIntent.putParcelableArrayListExtra("TOPDEALS", categoryViewModels);
//            getView().navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
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

    public void getAllBrands() {
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

    public void getLocationListBySearch(String searchText) {
        highlight = searchText;
        List<BrandViewModel> brandModels = new ArrayList<>();
        for (BrandViewModel brand : brandViewModels) {
            Log.d("skhkhdkfh", " " + searchText + "  " + brand.getTitle());
            if (brand.getTitle().trim().toLowerCase().contains(searchText.trim().toLowerCase())) {
                brandModels.add(brand);
            }
        }


        getView().renderBrandList(brandModels, SEARCH_SUBMITTED);
    }

    @Override
    public void searchTextChanged(String searchText) {
        SEARCH_SUBMITTED = false;
        if (searchText != null && !searchText.equals("")) {
            if (searchText.length() > 0) {
                getLocationListBySearch(searchText);
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
        Log.d("InsideSearchSubmitted", " " + SEARCH_SUBMITTED);
        getLocationListBySearch(searchText);

    }

}
