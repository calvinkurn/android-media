package com.tokopedia.digital.product.presenter;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.digital.product.interactor.IProductDigitalInteractor;
import com.tokopedia.digital.product.listener.IProductDigitalView;
import com.tokopedia.digital.product.model.ProductDigitalData;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 4/26/17.
 */

public class ProductDigitalPresenter implements IProductDigitalPresenter {

    private IProductDigitalView view;
    private IProductDigitalInteractor productDigitalInteractor;

    public ProductDigitalPresenter(IProductDigitalView view,
                                   IProductDigitalInteractor productDigitalInteractor) {
        this.view = view;
        this.productDigitalInteractor = productDigitalInteractor;
    }

    @Override
    public void processGetCategoryAndBannerData() {
        String categoryId = view.getCategoryId();
        TKPDMapParam<String, String> paramQueryCategory = new TKPDMapParam<>();
        TKPDMapParam<String, String> paramQueryBanner = new TKPDMapParam<>();
        paramQueryBanner.put("category_id", categoryId);
        productDigitalInteractor.getCategoryAndBanner(
                view.getCategoryId(),
                view.getGeneratedAuthParamNetwork(paramQueryCategory),
                view.getGeneratedAuthParamNetwork(paramQueryBanner),
                new Subscriber<ProductDigitalData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ProductDigitalData productDigitalData) {
                        view.renderProductDigitalData(
                                productDigitalData.getCategoryData(),
                                productDigitalData.getBannerDataList()
                        );
                    }
                }
        );
    }
}
