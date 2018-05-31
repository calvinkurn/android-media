package com.tokopedia.digital_deals.view.presenter;


import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tkpd.library.utils.CommonUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.digital_deals.R;
import com.tokopedia.digital_deals.domain.GetAllBrandsUseCase;
import com.tokopedia.digital_deals.domain.GetDealsListRequestUseCase;
import com.tokopedia.digital_deals.domain.GetNextDealPageUseCase;
import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.view.activity.AllBrandsActivity;
import com.tokopedia.digital_deals.view.activity.DealDetailsActivity;
import com.tokopedia.digital_deals.view.activity.DealsHomeActivity;
import com.tokopedia.digital_deals.view.activity.DealsLocationActivity;
import com.tokopedia.digital_deals.view.activity.DealsSearchActivity;
import com.tokopedia.digital_deals.view.contractor.DealsContract;
import com.tokopedia.digital_deals.view.utils.Utils;
import com.tokopedia.digital_deals.view.viewmodel.BrandViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoriesModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.digital_deals.view.viewmodel.CategoryViewModel;
import com.tokopedia.digital_deals.view.viewmodel.LocationViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;


public class DealsHomePresenter extends BaseDaggerPresenter<DealsContract.View>
        implements DealsContract.Presenter {

    private GetDealsListRequestUseCase getDealsListRequestUseCase;
    private GetAllBrandsUseCase getAllBrandsUseCase;
    private GetNextDealPageUseCase getNextDealPageUseCase;
    private ArrayList<CategoryViewModel> categoryViewModels;
    private List<BrandViewModel> brandViewModels;
    private List<CategoriesModel> categoriesModels;
    private TouchViewPager mTouchViewPager;
    private int currentPage, totalPages;
    private String PROMOURL = "https://www.tokopedia.com/promo/tiket/events/";
    private String FAQURL = "https://www.tokopedia.com/bantuan/faq-tiket-event/";
    private String TRANSATIONSURL = "https://pulsa.tokopedia.com/order-list/";
    public final static String TAG = "url";
    private final String CAROUSEL = "carousel";
    private final String TOP = "top";
    private final int PAGE_SIZE = 20;
    private boolean isLoading;
    private boolean isLastPage;
    private RequestParams searchNextParams = RequestParams.create();
    private volatile boolean isDealsLoaded = false;
    private volatile boolean isBrandsLoaded = false;


    @Inject
    public DealsHomePresenter(GetDealsListRequestUseCase getDealsListRequestUseCase, GetAllBrandsUseCase getAllBrandsUseCase, GetNextDealPageUseCase getNextDealPageUseCase) {
        this.getDealsListRequestUseCase = getDealsListRequestUseCase;
        this.getAllBrandsUseCase = getAllBrandsUseCase;
        this.getNextDealPageUseCase = getNextDealPageUseCase;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        getDealsListRequestUseCase.unsubscribe();
    }

    @Override
    public void startBannerSlide(TouchViewPager viewPager) {
        this.mTouchViewPager = viewPager;
        currentPage = viewPager.getCurrentItem();
        try {
            totalPages = viewPager.getAdapter().getCount();
        } catch (Exception e) {
            e.printStackTrace();
            totalPages = viewPager.getChildCount();
        }
        Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (currentPage + 1 < totalPages)
                            ++currentPage;
                        else if (currentPage + 1 >= totalPages) {
                            currentPage = 0;
                        }
                        mTouchViewPager.setCurrentItem(currentPage, true);
                    }
                });
    }

    @Override
    public void onBannerSlide(int page) {
        currentPage = page;
    }

    @Override
    public boolean onOptionMenuClick(int id) {
        if (id == R.id.search_input_view) {
//            ArrayList<SearchViewModel> searchViewModelList = Utils.getSingletonInstance().convertIntoSearchViewModel(categoryViewModels);
            Intent searchIntent = new Intent(getView().getActivity(), DealsSearchActivity.class);

            searchIntent.putParcelableArrayListExtra("TOPDEALS", (ArrayList<? extends Parcelable>) getCarouselOrTop(categoryViewModels, TOP).getItems());
            getView().navigateToActivityRequest(searchIntent, DealsHomeActivity.REQUEST_CODE_DEALSSEARCHACTIVITY);
        } else if (id == R.id.cl_location) {
            getView().navigateToActivityRequest(new Intent(getView().getActivity(), DealsLocationActivity.class), DealsHomeActivity.REQUEST_CODE_DEALSLOCATIONACTIVITY);
        } else if (id == R.id.action_menu_favourite) {

        } else if (id == R.id.action_promo) {
            getView().startGeneralWebView(PROMOURL);
        } else if (id == R.id.action_booked_history) {
            getView().startGeneralWebView(TRANSATIONSURL);
        } else if (id == R.id.action_faq) {
            getView().startGeneralWebView(FAQURL);
        } else if (id == R.id.tv_see_all_brands) {
            Intent brandIntent = new Intent(getView().getActivity(), AllBrandsActivity.class);
            brandIntent.putParcelableArrayListExtra(AllBrandsActivity.EXTRA_LIST, (ArrayList<? extends Parcelable>) categoriesModels);
            getView().navigateToActivity(brandIntent);

        } else {
            getView().getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    public void getDealsList() {
        getView().showProgressBar();
        getDealsListRequestUseCase.execute(getView().getParams(), new Subscriber<DealsDomain>() {

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
                        getDealsList();
                    }
                });
            }

            @Override
            public void onNext(DealsDomain dealEntity) {
                isDealsLoaded = true;

                processSearchResponse(dealEntity);

                getView().renderCategoryList(getCategories(categoryViewModels),
                        getCarouselOrTop(categoryViewModels, CAROUSEL),
                        getCarouselOrTop(categoryViewModels, TOP));
                showHideViews();

                CommonUtils.dumper("enter onNext");
            }
        });
    }

    public void getBrandsList() {
        RequestParams brandsParams = RequestParams.create();
        brandsParams.putString(Utils.BRAND_QUERY_PARAM_TREE, Utils.BRAND_QUERY_PARAM_BRAND);
        LocationViewModel location=Utils.getSingletonInstance()
                .getLocation(getView().getActivity());
        Log.d("hdjshdjs", location.toString());

//        brandsParams.putInt(Utils.BRAND_QUERY_PARAM_CITY_ID, location.getId());
        getView().showProgressBar();
        getAllBrandsUseCase.execute(brandsParams, new Subscriber<AllBrandsDomain>() {

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

    private void loadMoreItems() {
        isLoading = true;

        getNextDealPageUseCase.execute(searchNextParams, new Subscriber<DealsDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isLoading = false;
            }

            @Override
            public void onNext(DealsDomain dealsDomain) {
                isLoading = false;
                processSearchResponse(dealsDomain);
                getView().addDealsToCards(getCarouselOrTop(categoryViewModels, TOP));

                checkIfToLoad(getView().getLayoutManager());
            }
        });
    }

    private void showHideViews() {
        if (isBrandsLoaded && isDealsLoaded) {
            getView().showFavouriteButton();
            getView().hideProgressBar();
            getView().showViews();
        }
    }

    public ArrayList<String> getCarouselImages(List<CategoryItemsViewModel> categoryItemsViewModels) {
        ArrayList<String> imagesList = new ArrayList<>();
        if (categoryItemsViewModels != null) {
            for (CategoryItemsViewModel categoryItemsViewModel : categoryItemsViewModels
                    ) {
                imagesList.add(categoryItemsViewModel.getImageWeb());
            }
        }
        return imagesList;
    }

    public void onClickBanner() {
        CategoryViewModel carousel = getCarouselOrTop(categoryViewModels, CAROUSEL);
        if (carousel != null) {
            CategoryItemsViewModel categoryItemsViewModel = carousel.getItems().get(currentPage);
            if (categoryItemsViewModel.getUrl().contains("www.tokopedia.com")
                    || categoryItemsViewModel.getUrl().contains("docs.google.com")) {
                getView().startGeneralWebView(categoryItemsViewModel.getUrl());
            } else {
                Intent detailsIntent = new Intent(getView().getActivity(), DealDetailsActivity.class);
                detailsIntent.putExtra(DealDetailsPresenter.HOME_DATA, categoryItemsViewModel);
                getView().navigateToActivity(detailsIntent);
            }
        }
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

    private CategoryViewModel getCarouselOrTop(List<CategoryViewModel> categoryList, String carouselOrTop) {

        if (categoryList.get(0).getName().equalsIgnoreCase(carouselOrTop)) {
            return categoryList.get(0);
        } else {
            return categoryList.get(1);
        }
    }

    private List<CategoryViewModel> getCategories(List<CategoryViewModel> listItems) {
        List<CategoryViewModel> categoryList = null;
        if (listItems != null && listItems.size() > 2) {
            categoryList = new ArrayList<>();
            categoriesModels = new ArrayList<>();
            for (int i = 2; i < listItems.size(); i++) {
                categoryList.add(listItems.get(i));
                CategoriesModel categoriesModel = new CategoriesModel();
                categoriesModel.setName(listItems.get(i).getName());
                categoriesModel.setTitle(listItems.get(i).getTitle());
                categoriesModel.setUrl(listItems.get(i).getUrl());
                categoriesModel.setPosition(i-1);
                categoriesModels.add(categoriesModel);
            }
        }
        CategoriesModel categoriesModel = new CategoriesModel();
        categoriesModel.setUrl("");
        categoriesModel.setTitle(getView().getActivity().getResources().getString(R.string.all_brands));
        categoriesModel.setName(getView().getActivity().getResources().getString(R.string.all_brands));
        categoriesModel.setPosition(0);
        categoriesModels.add(0, categoriesModel);
        return categoryList;
    }

    void processSearchResponse(DealsDomain dealEntity) {
//        String nexturl = dealEntity.getPage().getUriNext();
//        if (nexturl != null && !nexturl.isEmpty() && nexturl.length() > 0) {
//            searchNextParams.putString(TAG, nexturl);
//            isLastPage = false;
//        } else {
//            isLastPage = true;
//        }

        categoryViewModels = Utils.getSingletonInstance()
                .convertIntoCategoryListViewModel(dealEntity.getDealsCategory());
    }
}
