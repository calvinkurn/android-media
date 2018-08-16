package com.tokopedia.shop.settings.notes.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.DeleteShopNoteUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.ReorderShopNoteUseCase;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by hendry on 16/08/18.
 */
public class ShopSettingNoteListPresenter extends BaseDaggerPresenter<ShopSettingNoteListPresenter.View> {
    private GetShopNotesUseCase getShopNotesUseCase;
    private ReorderShopNoteUseCase reorderShopNoteUseCase;
    private DeleteShopNoteUseCase deleteShopNoteUseCase;

    public interface View extends CustomerView {
        void onSuccessGetShopNotes(ArrayList<ShopNoteViewModel> shopNoteModels);
        void onErrorGetShopNotes(Throwable throwable);
        void onSuccessReorderShopNote(String successMessage);
        void onErrorReorderShopNote(Throwable throwable);
        void onSuccessDeleteShopNote(String successMessage);
        void onErrorDeleteShopNote(Throwable throwable);
    }

    @Inject
    public ShopSettingNoteListPresenter(GetShopNotesUseCase getShopNotesUseCase,
                                        ReorderShopNoteUseCase reorderShopNoteUseCase,
                                        DeleteShopNoteUseCase deleteShopNoteUseCase) {
        this.getShopNotesUseCase = getShopNotesUseCase;
        this.reorderShopNoteUseCase = reorderShopNoteUseCase;
        this.deleteShopNoteUseCase = deleteShopNoteUseCase;
    }

    public void getShopNotes(){
        getShopNotesUseCase.unsubscribe();
        getShopNotesUseCase.execute(RequestParams.EMPTY, new Subscriber<ArrayList<ShopNoteModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopNotes(e);
                }
            }

            @Override
            public void onNext(ArrayList<ShopNoteModel> shopNoteModels) {
                if (isViewAttached()) {
                    ArrayList<ShopNoteViewModel> shopNoteViewModelArrayList = new ArrayList<>();
                    for (ShopNoteModel shopNoteModel: shopNoteModels) {
                        shopNoteViewModelArrayList.add(new ShopNoteViewModel(shopNoteModel));
                    }
                    getView().onSuccessGetShopNotes(shopNoteViewModelArrayList);
                }
            }
        });
    }

    public void reorderShopNotes(ArrayList<String> noteIdList){
        reorderShopNoteUseCase.unsubscribe();
        reorderShopNoteUseCase.execute(ReorderShopNoteUseCase.createRequestParams(noteIdList),
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorReorderShopNote(e);
                        }
                    }

                    @Override
                    public void onNext(String successMessage) {
                        if (isViewAttached()) {
                            getView().onSuccessReorderShopNote(successMessage);
                        }
                    }
                });
    }

    public void deleteShopNote(String noteId){
        deleteShopNoteUseCase.unsubscribe();
        deleteShopNoteUseCase.execute(DeleteShopNoteUseCase.createRequestParams(noteId),
                new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().onErrorDeleteShopNote(e);
                        }
                    }

                    @Override
                    public void onNext(String successMessage) {
                        if (isViewAttached()) {
                            getView().onSuccessDeleteShopNote(successMessage);
                        }
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopNotesUseCase.unsubscribe();
        reorderShopNoteUseCase.unsubscribe();
        deleteShopNoteUseCase.unsubscribe();
    }
}
