package com.tokopedia.tkpd.home;

import com.tokopedia.core.network.entity.homeMenu.CategoryMenuModel;

import java.util.ArrayList;

/**
 * @author Kulomady on 10/4/16.
 */

public interface HomeCatMenuView {

    void showGetCatMenuLoading();

    void dismmisGetCatMenuLoading();

    void showGetCatMenuErrorMessage(int stringId);

    void showGetCatMenuUnknownHostMessage(int stringId);

    void showGetCatMenuSocketTimeoutExceptionMessage(int stringId);

    void showGetCatMenuEmptyMessage(int stringId);

    void renderHomeCatMenu(ArrayList<CategoryMenuModel> categoryMenuModels);

    void showGetCatMenuFromDbErrorMessage(int stringId);
}
