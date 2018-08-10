package com.tokopedia.shop.settings.notes.view;

import android.os.Bundle;
import android.util.Log;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.AddShopNoteUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.DeleteShopNoteUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.GetShopNotesUseCase;
import com.tokopedia.shop.common.graphql.data.shopnote.ShopNoteModel;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.ReorderShopNoteUseCase;
import com.tokopedia.shop.common.graphql.domain.usecase.shopnotes.UpdateShopNoteUseCase;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;

import rx.Subscriber;

/**
 * Created by Toped10 on 5/19/2016.
 */
public class ShopSettingsNotesFragment extends BaseDaggerFragment {

    public static ShopSettingsNotesFragment newInstance() {
        return new ShopSettingsNotesFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(getContext());
        GetShopNotesUseCase useCase = new GetShopNotesUseCase(getContext());
        useCase.execute(RequestParams.EMPTY, new Subscriber<ArrayList<ShopNoteModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("Test", "test");
            }

            @Override
            public void onNext(ArrayList<ShopNoteModel> shopNotes) {
                Log.i("Test", "test");
            }
        });

        AddShopNoteUseCase addShopNoteUseCase = new AddShopNoteUseCase(getContext());
        addShopNoteUseCase.execute(AddShopNoteUseCase.createRequestParams("Kebijakan pengembalian",
                "<b>Sebuah kebijakan</b>",
                true)
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });

        UpdateShopNoteUseCase updateShopNoteUseCase = new UpdateShopNoteUseCase(getContext());
        updateShopNoteUseCase.execute(UpdateShopNoteUseCase.createRequestParams(
                "123",
                "Kebijakan pengembalian",
                "<b>Sebuah kebijakan</b>")
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });

        DeleteShopNoteUseCase deleteShopNoteUseCase = new DeleteShopNoteUseCase(getContext());
        deleteShopNoteUseCase.execute(DeleteShopNoteUseCase.createRequestParams("123")
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });

        ReorderShopNoteUseCase reorderShopNoteUseCase = new ReorderShopNoteUseCase(getContext());
        ArrayList<String> idList = new ArrayList<>();
        idList.add("123");
        idList.add("456");
        idList.add("789");
        idList.add("012");
        reorderShopNoteUseCase.execute(ReorderShopNoteUseCase.createRequestParams(idList)
                , new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("Test", "test");
                    }

                    @Override
                    public void onNext(String successMessage) {
                        Log.i("Test", successMessage);
                    }
                });
    }

}
