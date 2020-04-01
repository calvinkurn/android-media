package com.tokopedia.imagesearch.search.fragment.product;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;

import java.net.ContentHandler;
import java.util.List;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListFragmentView extends CustomerView {

    void launchLoginActivity(Bundle extras);

    boolean isUserHasLogin();

    String getUserId();

    void onLoadMoreEmpty();

    void appendProductList(List<Visitable> list, boolean hasNextPage);

    void disableWishlistButton(String productId);

    void enableWishlistButton(String productId);

    void setQueryKey(String query);

    SearchParameter getSearchParameter();

    void setSearchParameter(SearchParameter searchParameter);

    void backToTop();

    void showRefreshLayout();

    void hideRefreshLayout();

    BaseAppComponent getBaseAppComponent();

    String getEmptyResultMessage();

    Context getContext();

    void reloadData();

    void displayErrorRefresh();

    void renderDynamicFilter(DynamicFilterModel pojo);

    void onHandleImageResponseSearch(ProductViewModel productViewModel);

    void onHandleInvalidImageSearchResponse();

    void showImageNotSupportedError();

    void setTotalSearchResultCount(String formattedResultCount);
}
