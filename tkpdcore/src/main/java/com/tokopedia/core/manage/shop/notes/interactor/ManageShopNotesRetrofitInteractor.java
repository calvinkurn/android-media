package com.tokopedia.core.manage.shop.notes.interactor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.deposit.model.WithdrawForm;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.manage.shop.notes.model.ShopNoteDetailResult;
import com.tokopedia.core.manage.shop.notes.model.ShopNotesResult;

import java.util.Map;

/**
 * Created by nisie on 10/26/16.
 */

public interface ManageShopNotesRetrofitInteractor {
    void getShopNotes(@NonNull Context context, @NonNull Map<String, String> params,
                      @NonNull GetShopNotesListener listener);

    void deleteNote(@NonNull Context context, @NonNull Map<String, String> params,
                      @NonNull DeleteNoteListener listener);

    void getShopNoteDetail(@NonNull Context context, @NonNull Map<String, String> params,
                      @NonNull GetShopNoteDetailListener listener);

    void addNote(@NonNull Context context, @NonNull Map<String, String> params,
                  @NonNull AddNoteListener listener);

    void editNote(@NonNull Context context, @NonNull Map<String, String> params,
                    @NonNull EditNoteListener listener);

    void unsubscribe();


    interface GetShopNotesListener {

        void onSuccess(@NonNull ShopNotesResult data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    interface DeleteNoteListener {

        void onSuccess();

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    interface GetShopNoteDetailListener {

        void onSuccess(@NonNull ShopNoteDetailResult data);

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }

    interface EditNoteListener {

        void onSuccess();

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }


    interface AddNoteListener {

        void onSuccess();

        void onTimeout(String message);

        void onError(String error);

        void onNullData();

        void onNoNetworkConnection();
    }
}
