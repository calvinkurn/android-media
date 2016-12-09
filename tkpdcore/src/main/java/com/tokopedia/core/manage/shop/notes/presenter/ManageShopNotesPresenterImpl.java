package com.tokopedia.core.manage.shop.notes.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.manage.shop.notes.interactor.ManageShopNotesRetrofitInteractor;
import com.tokopedia.core.manage.shop.notes.interactor.ManageShopNotesRetrofitInteractorImpl;
import com.tokopedia.core.manage.shop.notes.listener.ManageShopNotesView;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.manage.shop.notes.model.ShopNotesResult;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 10/26/16.
 */

public class ManageShopNotesPresenterImpl implements ManageShopNotesPresenter {

    ManageShopNotesRetrofitInteractor networkInteractor;
    ManageShopNotesView viewListener;

    public ManageShopNotesPresenterImpl(ManageShopNotesView viewListener) {
        this.networkInteractor = new ManageShopNotesRetrofitInteractorImpl();
        this.viewListener = viewListener;
    }


    @Override
    public void initData() {
        viewListener.showLoading();
        viewListener.setViewEnabled(false);
        getShopNote();
    }

    private void getShopNote() {
        networkInteractor.getShopNotes(viewListener.getActivity(), getShopNotesParam(), new ManageShopNotesRetrofitInteractor.GetShopNotesListener() {
            @Override
            public void onSuccess(@NonNull ShopNotesResult data) {
                viewListener.setViewEnabled(true);
                viewListener.finishLoading();
                viewListener.setResult(data);
            }

            @Override
            public void onTimeout(String message) {
                viewListener.finishLoading();
                viewListener.showError(message);
            }

            @Override
            public void onError(String error) {
                viewListener.finishLoading();
                viewListener.showError(error);

            }

            @Override
            public void onNullData() {
                viewListener.finishLoading();
                viewListener.showError(viewListener.getActivity().getString(R.string.default_request_error_null_data));

            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.finishLoading();
                viewListener.showError("");
            }
        });
    }

    @Override
    public void onRefresh() {
        viewListener.showRefreshing();
        getShopNote();
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    @Override
    public void onDeleteNote(final ShopNote shopNote) {
        viewListener.showProgressDialog();
        networkInteractor.deleteNote(viewListener.getActivity(), getDeleteNoteParam(shopNote), new ManageShopNotesRetrofitInteractor.DeleteNoteListener() {
            @Override
            public void onSuccess() {
                viewListener.dismissProgressDialog();
                viewListener.onSuccessDeleteNote(shopNote.getPosition());
            }

            @Override
            public void onTimeout(String message) {
                viewListener.dismissProgressDialog();
                viewListener.showErrorSnackbar(message);

            }

            @Override
            public void onError(String error) {
                viewListener.dismissProgressDialog();
                viewListener.showErrorSnackbar(error);

            }

            @Override
            public void onNullData() {
                viewListener.dismissProgressDialog();
                viewListener.showErrorSnackbar(viewListener.getActivity().getString(R.string.default_request_error_null_data));
            }

            @Override
            public void onNoNetworkConnection() {
                viewListener.dismissProgressDialog();
                viewListener.showErrorSnackbar("");

            }
        });
    }

    private Map<String, String> getDeleteNoteParam(ShopNote shopNote) {
        HashMap<String, String> param = new HashMap<>();
        param.put("note_id", shopNote.getNoteId());
        param.put("shop_id", SessionHandler.getShopID(viewListener.getActivity()));
        return param;
    }

    @Override
    public void addNote() {
        if (canAddNote()) {
            viewListener.getActionListener().onAddShopNote(false, null);
        }

    }

    private boolean canAddNote() {
        if ((viewListener.getAdapter().getList().size() == 3 && !viewListener.getAdapter().hasReturnablePolicy()) ||
                (viewListener.getAdapter().getList().size() == 4)) {
            viewListener.showErrorSnackbar(viewListener.getActivity().getString(R.string.error_max_notes));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void addReturnablePolicy() {
        if (canAddPolicy()) {
            viewListener.getActionListener().onAddShopNote(true, null);
        }
    }

    private boolean canAddPolicy() {
        if (!viewListener.getAdapter().hasReturnablePolicy())
            return true;
        else {
            viewListener.showErrorSnackbar(viewListener.getActivity().getString(R.string.returnable_policy_existed));
            return false;
        }

    }

    private Map<String, String> getShopNotesParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("shop_id", SessionHandler.getShopID(viewListener.getActivity()));
        param.put("shop_domain", SessionHandler.getShopDomain(viewListener.getActivity()));
        return param;
    }
}
