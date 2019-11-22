package com.tokopedia.imagesearch.search.fragment.product;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.imagesearch.di.component.DaggerImageSearchComponent;
import com.tokopedia.imagesearch.di.component.ImageSearchComponent;
import com.tokopedia.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.imagesearch.domain.usecase.RefreshImageSearchUseCase;
import com.tokopedia.imagesearch.domain.viewmodel.CategoryFilterModel;
import com.tokopedia.imagesearch.domain.viewmodel.ProductItem;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;
import com.tokopedia.imagesearch.search.exception.ImageNotSupportedException;
import com.tokopedia.wishlist.common.listener.WishListActionListener;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sachinbansal on 4/13/18.
 */

public class ImageProductListPresenterImpl extends BaseDaggerPresenter<ImageProductListFragmentView> implements ImageProductListPresenter {

    public static final int ITEM_COUNT_PER_PAGE = 12;
    private static final long LOAD_MORE_DELAY_MS = 1000;

    @Inject
    GetImageSearchUseCase getImageSearchUseCase;
    @Inject
    RefreshImageSearchUseCase refreshImageSearchUseCase;
    @Inject
    AddWishListUseCase addWishlistActionUseCase;
    @Inject
    RemoveWishListUseCase removeWishlistActionUseCase;

    private WishListActionListener wishListActionListener;
    private List<Visitable> originalDataList = new ArrayList<>();
    private List<Visitable> presentedDataList = new ArrayList<>();
    private CategoryFilterModel categoryFilterModel;
    private String selectedCategoryId = "";
    private String token = "";

    @Override
    public void detachView() {
        if(getImageSearchUseCase != null) getImageSearchUseCase.unsubscribe();
        if(refreshImageSearchUseCase != null) refreshImageSearchUseCase.unsubscribe();
        if(addWishlistActionUseCase != null) addWishlistActionUseCase.unsubscribe();
        if(removeWishlistActionUseCase != null) removeWishlistActionUseCase.unsubscribe();
    }

    @Override
    public void attachView(ImageProductListFragmentView viewListener,
                           WishListActionListener wishlistActionListener) {
        super.attachView(viewListener);
        initInjector(viewListener);
        this.wishListActionListener = wishlistActionListener;
    }

    private void initInjector(ImageProductListFragmentView viewListener) {
        ImageSearchComponent component = DaggerImageSearchComponent.builder()
                .baseAppComponent(viewListener.getBaseAppComponent())
                .build();
        component.inject(this);
    }

    @Override
    public void initData(List<Visitable> data, CategoryFilterModel categoryFilterModel, String token) {
        this.categoryFilterModel = categoryFilterModel;
        this.token = token;
        originalDataList.clear();
        originalDataList.addAll(data);
        presentedDataList.clear();
        presentedDataList.addAll(data);
    }

    @Override
    public boolean isCategoryFilterSelected(String categoryId) {
        return selectedCategoryId.equals(categoryId);
    }

    @Override
    public void setFilterCategory(String categoryId) {
        selectedCategoryId = categoryId;

        presentedDataList.clear();

        if (TextUtils.isEmpty(categoryId)) {
            presentedDataList.addAll(originalDataList);
            return;
        }

        for (Visitable item : originalDataList) {
            if (item instanceof ProductItem) {
                String itemCategoryId = String.valueOf(((ProductItem) item).getCategoryID());
                if (isCategoryFilterSelected(itemCategoryId)) {
                    presentedDataList.add(item);
                }
            }
        }
    }

    @Override
    public void requestImageSearch(String imagePath) {
        getImageSearchUseCase.setImagePath(imagePath);
        getImageSearchUseCase.execute(
                GetImageSearchUseCase.generateParams(getView().getSearchParameter()),
                new Subscriber<ProductViewModel>() {

                    @Override
                    public void onStart() {
                        getView().showRefreshLayout();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideRefreshLayout();
                        if (e instanceof ImageNotSupportedException) {
                            getView().showImageNotSupportedError();
                        } else {
                            getView().onHandleInvalidImageSearchResponse();
                        }
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ProductViewModel productViewModel) {
                        getView().hideRefreshLayout();
                        handleGetImageSearchResult(productViewModel);
                        getView().onHandleImageResponseSearch(productViewModel);
                    }
                });
    }

    @Override
    public void refreshData() {
        refreshImageSearchUseCase.execute(
                RefreshImageSearchUseCase.generateParams(token, getView().getSearchParameter()),
                new Subscriber<ProductViewModel>() {

                    @Override
                    public void onStart() {
                        getView().showRefreshLayout();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideRefreshLayout();
                        getView().displayErrorRefresh();
                    }

                    @Override
                    public void onNext(ProductViewModel productViewModel) {
                        getView().hideRefreshLayout();
                        handleGetImageSearchResult(productViewModel);
                    }
                }
        );
    }

    private void handleGetImageSearchResult(ProductViewModel model) {
        initData(new ArrayList<>(model.getProductList()), model.getCategoryFilterModel(), model.getToken());
        if (!isSelectedCategoryValid(model.getCategoryFilterModel())) {
            selectedCategoryId = "";
        }
        setFilterCategory(selectedCategoryId);
        getView().renderDynamicFilter(model.getDynamicFilterModel());
        getView().setTotalSearchResultCount(model.getTotalDataText());
        getView().reloadData();
    }

    private boolean isSelectedCategoryValid(CategoryFilterModel categoryFilterModel) {
        for (CategoryFilterModel.Item item : categoryFilterModel.getItemList()) {
            if (selectedCategoryId.equals(item.getCategoryId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadMoreData(int page) {
        List<Visitable> responseList = new ArrayList<>();

        int fromIndex = page * ITEM_COUNT_PER_PAGE;
        int toIndex = fromIndex + ITEM_COUNT_PER_PAGE;
        toIndex = toIndex > presentedDataList.size() ? presentedDataList.size() : toIndex;

        final boolean hasNextPage = toIndex < presentedDataList.size();

        long delay = LOAD_MORE_DELAY_MS;

        if (page == 0) {
            delay = 0;
            responseList.add(categoryFilterModel);
            if (presentedDataList.size() == 0) responseList.add(createEmptyModel());
        }

        if (fromIndex < presentedDataList.size()) {
            responseList.addAll(presentedDataList.subList(fromIndex, toIndex));
        }

        Observable.just(responseList)
                .delay(delay, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Visitable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Visitable> visitables) {
                        if (!visitables.isEmpty()) {
                            getView().appendProductList(visitables, hasNextPage);
                        } else {
                            getView().onLoadMoreEmpty();
                        }
                    }
                });
    }

    public EmptyModel createEmptyModel() {
        EmptyModel emptyModel = new EmptyModel();
        emptyModel.setTitle(getView().getEmptyResultMessage());
        return emptyModel;
    }

    @Override
    public void handleWishlistButtonClicked(ProductItem productItem) {
        if (getView().isUserHasLogin()) {
            getView().disableWishlistButton(productItem.getProductID());

            if (productItem.isWishlisted()) {
                removeWishlist(productItem.getProductID(), getView().getUserId());
            } else {
                addWishlist(productItem.getProductID(), getView().getUserId());
            }

        } else {
            launchLoginActivity(productItem.getProductID());
        }
    }

    @NonNull
    protected Subscriber<Boolean> getWishlistSubscriber(final ProductItem productItem) {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    wishListActionListener.onErrorRemoveWishlist("", productItem.getProductID());
                }
            }

            @Override
            public void onNext(Boolean result) {
                if (isViewAttached()) {
                    if (result) {
                        wishListActionListener.onSuccessAddWishlist(productItem.getProductID());
                    } else
                        wishListActionListener.onErrorRemoveWishlist("", productItem.getProductID());
                }
            }
        };
    }

    private void launchLoginActivity(String productId) {
        Bundle extras = new Bundle();
        extras.putString("product_id", productId);
        getView().launchLoginActivity(extras);
    }

    private void addWishlist(String productId, String userId) {
        addWishlistActionUseCase.createObservable(productId, userId, wishListActionListener);
    }

    private void removeWishlist(String productId, String userId) {
        removeWishlistActionUseCase.createObservable(productId, userId, wishListActionListener);
    }

    @Override
    public String getToken() {
        return token;
    }
}
