package com.tokopedia.imagesearch.data.subscriber;

import com.tokopedia.discovery.common.constants.SearchApiConst;
import com.tokopedia.discovery.common.model.SearchParameter;
import com.tokopedia.imagesearch.domain.model.SearchResultModel;
import com.tokopedia.imagesearch.domain.viewmodel.ProductViewModel;
import com.tokopedia.imagesearch.helper.ProductViewModelHelper;
import com.tokopedia.imagesearch.search.ImageSearchContract;
import com.tokopedia.imagesearch.search.exception.ImageNotSupportedException;

import rx.Subscriber;

public class DefaultImageSearchSubscriber extends Subscriber<SearchResultModel> {

    private ImageSearchContract.View view;

    public DefaultImageSearchSubscriber(ImageSearchContract.View view) {
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof ImageNotSupportedException) {
            view.showImageNotSupportedError();
        } else {
            view.onHandleInvalidImageSearchResponse();
        }
        e.printStackTrace();
    }

    @Override
    public void onNext(SearchResultModel searchResultModel) {
        ProductViewModel model = ProductViewModelHelper.convertToProductViewModelFirstPage(view.getContext(), searchResultModel);
        view.onHandleImageResponseSearch(model);
    }
}