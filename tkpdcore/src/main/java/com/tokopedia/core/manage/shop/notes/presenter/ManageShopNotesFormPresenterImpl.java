package com.tokopedia.core.manage.shop.notes.presenter;

import android.support.annotation.NonNull;

import com.tokopedia.core.R;
import com.tokopedia.core.manage.shop.notes.activity.ManageShopNotesActivity;
import com.tokopedia.core.manage.shop.notes.interactor.ManageShopNotesRetrofitInteractor;
import com.tokopedia.core.manage.shop.notes.interactor.ManageShopNotesRetrofitInteractorImpl;
import com.tokopedia.core.manage.shop.notes.listener.ManageShopNoteFormView;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.manage.shop.notes.model.ShopNoteDetailResult;
import com.tokopedia.core.util.SessionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 10/26/16.
 */

public class ManageShopNotesFormPresenterImpl implements ManageShopNotesFormPresenter {


    private final ManageShopNoteFormView viewListener;
    private ManageShopNotesRetrofitInteractor networkInteractor;

    public ManageShopNotesFormPresenterImpl(ManageShopNoteFormView viewListener) {
        this.viewListener = viewListener;
        this.networkInteractor = new ManageShopNotesRetrofitInteractorImpl();
    }

    @Override
    public void getShopNoteDetail() {
        viewListener.showLoading();
        networkInteractor.getShopNoteDetail(viewListener.getActivity(), getShopNoteDetailParam(), new ManageShopNotesRetrofitInteractor.GetShopNoteDetailListener() {
            @Override
            public void onSuccess(@NonNull ShopNoteDetailResult data) {
                viewListener.finishLoading();
                viewListener.setResult(data.getDetail());
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
    public void onEditNote() {
        if (isNoteValid()) {
            viewListener.showLoading();
            networkInteractor.editNote(viewListener.getActivity(), getEditNoteParam(), new ManageShopNotesRetrofitInteractor.EditNoteListener() {
                @Override
                public void onSuccess() {
                    viewListener.finishLoading();
                    viewListener.onSuccessEdit();
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
    }

    private boolean isNoteValid() {
        boolean isValid = true;
        if (viewListener.getNoteContent().getText().toString().trim().length() == 0) {
            viewListener.getNoteContentLayout().setError(viewListener.getActivity().getString(R.string.error_field_required));
            viewListener.getNoteContent().requestFocus();
            isValid = false;
        } else if (viewListener.getNoteContent().getText().toString().trim().length() > 6000) {
            viewListener.getNoteContentLayout().setError(viewListener.getActivity().getString(R.string.error_max_shop_notes_content_length));
            viewListener.getNoteContent().requestFocus();
            isValid = false;
        }

        if (viewListener.getNoteName().getText().toString().trim().length() == 0) {
            viewListener.getNoteNameLayout().setError(viewListener.getActivity().getString(R.string.error_field_required));
            viewListener.getNoteContent().requestFocus();
            isValid = false;
        } else if (viewListener.getNoteName().getText().toString().trim().length() > 128) {
            viewListener.getNoteNameLayout().setError(viewListener.getActivity().getString(R.string.error_max_shop_notes_title_length));
            viewListener.getNoteContent().requestFocus();
            isValid = false;
        }

        return isValid;
    }

    @Override
    public void onAddNote() {
        if (isNoteValid()) {
            viewListener.showLoading();
            networkInteractor.addNote(viewListener.getActivity(), getAddNoteParam(), new ManageShopNotesRetrofitInteractor.AddNoteListener() {
                @Override
                public void onSuccess() {
                    viewListener.finishLoading();
                    viewListener.onSuccessEdit();
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
    }

    @Override
    public void onDestroyView() {
        networkInteractor.unsubscribe();
    }

    private HashMap<String, String> getShopNoteDetailParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("shop_id", SessionHandler.getShopID(viewListener.getActivity()));
        params.put("shop_domain", SessionHandler.getShopDomain(viewListener.getActivity()));
        params.put("note_id", viewListener.getArguments().getParcelable(ManageShopNotesActivity.PARAM_SHOP_NOTE) != null ?
                ((ShopNote) viewListener.getArguments().getParcelable(ManageShopNotesActivity.PARAM_SHOP_NOTE)).getNoteId() : "");
        return params;
    }

    private HashMap<String, String> getEditNoteParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("note_content", viewListener.getNoteContent().getText().toString());
        param.put("note_id", viewListener.getArguments().getParcelable(ManageShopNotesActivity.PARAM_SHOP_NOTE) != null ?
                ((ShopNote) viewListener.getArguments().getParcelable(ManageShopNotesActivity.PARAM_SHOP_NOTE)).getNoteId() : "");
        param.put("note_title", viewListener.getNoteName().getText().toString());
        param.put("terms", viewListener.getArguments().getBoolean(ManageShopNotesActivity.PARAM_IS_RETURNABLE_POLICY) ? "1" : "");
        return param;
    }

    private HashMap<String, String> getAddNoteParam() {
        HashMap<String, String> param = new HashMap<>();
        param.put("note_content", viewListener.getNoteContent().getText().toString());
        param.put("note_title", viewListener.getNoteName().getText().toString());
        param.put("terms", viewListener.getArguments().getBoolean(ManageShopNotesActivity.PARAM_IS_RETURNABLE_POLICY) ? "1" : "");
        return param;
    }
}
