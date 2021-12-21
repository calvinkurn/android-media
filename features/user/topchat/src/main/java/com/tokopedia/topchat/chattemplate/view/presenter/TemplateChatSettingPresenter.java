package com.tokopedia.topchat.chattemplate.view.presenter;

import com.google.gson.JsonArray;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.topchat.chattemplate.domain.usecase.SetAvailabilityTemplateUseCase;
import com.tokopedia.topchat.chattemplate.view.listener.TemplateChatContract;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateUiModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by stevenfredian on 12/11/17.
 */

public class TemplateChatSettingPresenter extends BaseDaggerPresenter<TemplateChatContract.View>
        implements TemplateChatContract.Presenter {

    private final SetAvailabilityTemplateUseCase setAvailabilityTemplateUseCase;
    private boolean isSeller;

    @Inject
    TemplateChatSettingPresenter(SetAvailabilityTemplateUseCase setAvailabilityTemplateUseCase) {
        this.setAvailabilityTemplateUseCase = setAvailabilityTemplateUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();
        setAvailabilityTemplateUseCase.unsubscribe();
    }

    @Override
    public void switchTemplateAvailability(final boolean enabled) {
        JsonArray array = null;
        getView().showLoading();
        setAvailabilityTemplateUseCase.execute(SetAvailabilityTemplateUseCase.generateParam(array, enabled, isSeller), new Subscriber<GetTemplateUiModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
                getView().setChecked(!enabled);
                getView().finishLoading();
            }

            @Override
            public void onNext(GetTemplateUiModel getTemplateViewModel) {
                if (getTemplateViewModel.isSuccess()) {
                    getView().successSwitch();
                }
                getView().finishLoading();
            }
        });
    }

    @Override
    public void setArrange(boolean enabled, final ArrayList arrayList, final int from, final int to) {
        JsonArray array = null;
        if (arrayList != null) array = toJsonArray(arrayList);
        getView().showLoading();
        setAvailabilityTemplateUseCase.execute(SetAvailabilityTemplateUseCase.generateParam(array, enabled, isSeller), new Subscriber<GetTemplateUiModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(e);
                getView().revertArrange(from, to);
                getView().finishLoading();
            }

            @Override
            public void onNext(GetTemplateUiModel getTemplateViewModel) {
                getView().successRearrange();
                getView().finishLoading();
            }
        });
    }

    private JsonArray toJsonArray(List<Integer> list) {
        JsonArray array = new JsonArray();
        for (Integer o : list) {
            array.add(o);
        }
        return array;
    }

    public void setMode(Boolean isSeller) {
        this.isSeller = isSeller;
    }
}
