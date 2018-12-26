package com.tokopedia.topchat.chattemplate.view.presenter;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.topchat.chattemplate.view.listener.EditTemplateChatContract;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase;
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase;
import com.tokopedia.topchat.chattemplate.domain.usecase.EditTemplateUseCase;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;
import com.tokopedia.topchat.chattemplate.domain.usecase.CreateTemplateUseCase;
import com.tokopedia.topchat.chattemplate.domain.usecase.DeleteTemplateUseCase;
import com.tokopedia.topchat.chattemplate.domain.usecase.EditTemplateUseCase;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;
import com.tokopedia.topchat.common.analytics.TopChatAnalytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by stevenfredian on 12/22/17.
 */

public class EditTemplateChatPresenter extends BaseDaggerPresenter<EditTemplateChatContract.View>
                    implements EditTemplateChatContract.Presenter{

    private final EditTemplateUseCase editTemplateUseCase;
    private final CreateTemplateUseCase createTemplateUseCase;
    private final DeleteTemplateUseCase deleteTemplateUseCase;

    @Inject
    EditTemplateChatPresenter(EditTemplateUseCase editTemplateUseCase, CreateTemplateUseCase createTemplateUseCase, DeleteTemplateUseCase deleteTemplateUseCase){
        this.editTemplateUseCase = editTemplateUseCase;
        this.createTemplateUseCase = createTemplateUseCase;
        this.deleteTemplateUseCase = deleteTemplateUseCase;
    }

    @Override
    public void attachView(EditTemplateChatContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getView().dropKeyboard();
        super.detachView();
        editTemplateUseCase.unsubscribe();
        createTemplateUseCase.unsubscribe();
        deleteTemplateUseCase.unsubscribe();
    }

    public void submitText(final String s, String text, List<String> list) {
        final int index = list.indexOf(text);
        List<String> temp = new ArrayList<>();
        temp.addAll(list);
        if(index<0){
            temp.add(s);
            createTemplateUseCase.execute(CreateTemplateUseCase.generateParam(s), new Subscriber<EditTemplateViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().showError(ErrorHandler.getErrorMessage(e));
                }

                @Override
                public void onNext(EditTemplateViewModel editTemplateViewModel) {
                    if(editTemplateViewModel.isSuccess()) {

                        getView().onResult(editTemplateViewModel, index, s);
                        getView().finish();
                    }else {
                        getView().showError("");
                    }
                }
            });
        }else {
            temp.set(index, s);
            editTemplateUseCase.execute(EditTemplateUseCase.generateParam(index+1,s), new Subscriber<EditTemplateViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    getView().showError(ErrorHandler.getErrorMessage(e));
                }

                @Override
                public void onNext(EditTemplateViewModel editTemplateViewModel) {
                    if(editTemplateViewModel.isSuccess()) {
                        getView().onResult(editTemplateViewModel, index, s);
                        getView().finish();
                    }else {
                        getView().showError("");
                    }
                }
            });
        }
    }

    @Override
    public void deleteTemplate(final int index) {
        deleteTemplateUseCase.execute(DeleteTemplateUseCase.generateParam(index+1), new Subscriber<EditTemplateViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showError(ErrorHandler.getErrorMessage(e));
            }

            @Override
            public void onNext(EditTemplateViewModel editTemplateViewModel) {
                if(editTemplateViewModel.isSuccess()) {
                    getView().onResult(editTemplateViewModel, index);
                    getView().finish();
                }else {
                    getView().showError("");
                }
            }
        });
    }
}
