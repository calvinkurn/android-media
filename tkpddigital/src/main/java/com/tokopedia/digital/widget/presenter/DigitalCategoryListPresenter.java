package com.tokopedia.digital.widget.presenter;

import com.tokopedia.digital.widget.data.entity.DigitalCategoryItemData;
import com.tokopedia.digital.widget.interactor.IDigitalCategoryListInteractor;
import com.tokopedia.digital.widget.listener.IDigitalCategoryListView;

import java.util.List;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 7/3/17.
 */

public class DigitalCategoryListPresenter implements IDigitalCategoryListPresenter {

    private final IDigitalCategoryListInteractor digitalCategoryListInteractor;
    private final IDigitalCategoryListView digitalCategoryListView;

    public DigitalCategoryListPresenter(
            IDigitalCategoryListInteractor digitalCategoryListInteractor,
            IDigitalCategoryListView iDigitalCategoryListView
    ) {
        this.digitalCategoryListInteractor = digitalCategoryListInteractor;
        this.digitalCategoryListView = iDigitalCategoryListView;
    }

    @Override
    public void processGetDigitalCategoryList() {
        digitalCategoryListInteractor.getDigitalCategoryItemDataList(
                new Subscriber<List<DigitalCategoryItemData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DigitalCategoryItemData> digitalCategoryItemDataList) {
                        digitalCategoryListView.renderDigitalCategoryDataList(
                                digitalCategoryItemDataList
                        );
                    }
                }
        );
    }
}
