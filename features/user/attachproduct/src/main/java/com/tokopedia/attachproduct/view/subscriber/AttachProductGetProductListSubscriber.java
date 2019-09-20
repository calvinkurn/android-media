package com.tokopedia.attachproduct.view.subscriber;

import com.tokopedia.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Hendri on 08/03/18.
 */

public class AttachProductGetProductListSubscriber extends Subscriber<List<AttachProductItemViewModel>> {
    public static final int DEFAULT_ROWS = 10;
    private final AttachProductContract.View view;

    public AttachProductGetProductListSubscriber(AttachProductContract.View view){
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {
        view.showErrorMessage(throwable);
    }

    @Override
    public void onNext(List<AttachProductItemViewModel> attachProductItemViewModels) {
        view.hideAllLoadingIndicator();

        boolean hasNext = false;
        if((attachProductItemViewModels.size() >= DEFAULT_ROWS)) {
            hasNext = true;
            attachProductItemViewModels.remove(attachProductItemViewModels.size()-1);
        }
        view.addProductToList(attachProductItemViewModels, hasNext);

        if(attachProductItemViewModels.size() > 0) {
            view.setShopName(attachProductItemViewModels.get(0).getShopName());
        }
    }
}
