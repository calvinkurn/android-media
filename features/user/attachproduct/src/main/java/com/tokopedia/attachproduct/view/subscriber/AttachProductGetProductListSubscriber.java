package com.tokopedia.attachproduct.view.subscriber;

import com.tokopedia.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.attachproduct.view.uimodel.AttachProductItemUiModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Hendri on 08/03/18.
 */

public class AttachProductGetProductListSubscriber extends Subscriber<List<AttachProductItemUiModel>> {
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
    public void onNext(List<AttachProductItemUiModel> attachProductItemUiModels) {
        view.hideAllLoadingIndicator();

        boolean hasNext = false;
        if((attachProductItemUiModels.size() >= DEFAULT_ROWS)) {
            hasNext = true;
            attachProductItemUiModels.remove(attachProductItemUiModels.size()-1);
        }
        view.addProductToList(attachProductItemUiModels, hasNext);

        if(attachProductItemUiModels.size() > 0) {
            view.setShopName(attachProductItemUiModels.get(0).getShopName());
        }
    }
}
