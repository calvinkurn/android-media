package com.tokopedia.core.manage.shop.notes.listener;

import android.app.Activity;

import com.tokopedia.core.manage.shop.notes.adapter.ShopNotesAdapter;
import com.tokopedia.core.manage.shop.notes.fragment.ManageShopNotesFragment;
import com.tokopedia.core.manage.shop.notes.model.ShopNotesResult;

/**
 * Created by nisie on 10/26/16.
 */

public interface ManageShopNotesView {
    void showLoading();

    Activity getActivity();

    void finishLoading();

    void setResult(ShopNotesResult data);

    void showError(String error);

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessDeleteNote(int position);

    void showErrorSnackbar(String message);

    ShopNotesAdapter getAdapter();

    void setOnActionShopNoteListener(ManageShopNotesFragment.OnActionShopNoteListener listener);

    ManageShopNotesFragment.OnActionShopNoteListener getActionListener();

    void refresh();

    void showRefreshing();

    void setViewEnabled(boolean isEnabled);

    void showEmpty();
}
