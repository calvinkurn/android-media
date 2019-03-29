package com.tokopedia.attachproduct.view.presenter;

import com.tokopedia.attachproduct.domain.usecase.AttachProductUseCase;
import com.tokopedia.attachproduct.resultmodel.ResultProduct;
import com.tokopedia.attachproduct.view.subscriber.AttachProductGetProductListSubscriber;
import com.tokopedia.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Hendri on 19/02/18.
 */

public class AttachProductPresenter implements AttachProductContract.Presenter {
    private final AttachProductUseCase useCase;
    private AttachProductContract.View view;
    private AttachProductContract.Activity activityContract;
    private List<AttachProductItemViewModel> checkedList;

    @Inject
    public AttachProductPresenter(AttachProductUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void attachView(AttachProductContract.View view) {
        this.view = view;
    }

    @Override
    public void attachActivityContract(AttachProductContract.Activity activityContract) {
        this.activityContract = activityContract;
    }

    @Override
    public void loadProductData(String query, String shopId, int page) {
        useCase.execute(AttachProductUseCase.createRequestParams(query, shopId, page),
                new AttachProductGetProductListSubscriber(view));
    }

    @Override
    public void updateCheckedList(List<AttachProductItemViewModel> products) {
        if (checkedList == null) {
            checkedList = new ArrayList<>();
        }
        resetCheckedList();
        checkedList.addAll(products);
        view.updateButtonBasedOnChecked(checkedList.size());
    }

    @Override
    public void resetCheckedList() {
        checkedList.removeAll(checkedList);
    }

    @Override
    public void completeSelection() {
        ArrayList<ResultProduct> resultProducts = new ArrayList<>();
        for(AttachProductItemViewModel product:checkedList){
            resultProducts.add(new ResultProduct(product));
        }
        activityContract.finishActivityWithResult(resultProducts);
    }

    @Override
    public void detachView() {
        if(useCase != null) useCase.unsubscribe();
        if(view != null) view = null;
        if(activityContract != null) activityContract = null;
    }
}
