package com.tokopedia.digital.product.presenter;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.listener.ISearchNumberDigitalView;
import com.tokopedia.digital.product.model.OrderClientNumber;
import com.tokopedia.digital.widget.interactor.IDigitalWidgetInteractor;
import com.tokopedia.digital.widget.model.DigitalNumberList;

import java.util.List;

import rx.Subscriber;

/**
 * @author rizkyfadillah on 10/4/2017.
 */

public class SearchNumberDigitalPresenter implements ISearchNumberDigitalPresenter {

    private ISearchNumberDigitalView view;
    private IDigitalWidgetInteractor digitalWidgetInteractor;

    public SearchNumberDigitalPresenter(ISearchNumberDigitalView view,
                                        IDigitalWidgetInteractor digitalWidgetInteractor) {
        this.view = view;
        this.digitalWidgetInteractor = digitalWidgetInteractor;
    }

    @Override
    public void getNumberList(String categoryId) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("category_id", categoryId);
        param.put("sort", "label");
        digitalWidgetInteractor.getNumberList(getNumberListSubscriber(),
                view.getGeneratedAuthParamNetwork(param));
    }

    private Subscriber<DigitalNumberList> getNumberListSubscriber() {
        return new Subscriber<DigitalNumberList>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(DigitalNumberList digitalNumberList) {
                view.renderNumberList(digitalNumberList.getOrderClientNumbers());
            }
        };
    }

}
