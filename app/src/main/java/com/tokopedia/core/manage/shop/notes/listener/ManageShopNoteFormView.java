package com.tokopedia.core.manage.shop.notes.listener;

import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.tokopedia.core.manage.people.address.listener.AddAddressFragmentView;
import com.tokopedia.core.manage.shop.notes.fragment.ManageShopNotesFormFragment;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;
import com.tokopedia.core.manage.shop.notes.model.ShopNoteDetail;
import com.tokopedia.core.myproduct.model.NoteDetailModel;

/**
 * Created by nisie on 10/26/16.
 */

public interface ManageShopNoteFormView {
    void setOnFinishActionListener(ManageShopNotesFormFragment.FinishActionListener listener);

    Activity getActivity();

    Bundle getArguments();

    void setNoteFromBundle(ShopNote shopNote);

    void showLoading();

    void finishLoading();

    void setResult(ShopNoteDetail detail);

    void showError(String message);

    EditText getNoteContent();

    EditText getNoteName();

    void onSuccessEdit();

    TextInputLayout getNoteContentLayout();

    TextInputLayout getNoteNameLayout();
}
