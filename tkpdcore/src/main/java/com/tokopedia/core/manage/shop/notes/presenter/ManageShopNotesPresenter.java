package com.tokopedia.core.manage.shop.notes.presenter;

import com.tokopedia.core.manage.shop.notes.model.ShopNote;

/**
 * Created by nisie on 10/26/16.
 */

public interface ManageShopNotesPresenter {

    void initData();

    void onRefresh();

    void onDestroyView();

    void onDeleteNote(ShopNote shopNote);

    void addNote();

    void addReturnablePolicy();
}
