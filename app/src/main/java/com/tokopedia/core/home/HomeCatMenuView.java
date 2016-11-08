/*
 * Created By Kulomady on 10/4/16 11:03 AM
 * Copyright (c) 2016. All Rights Reserved
 *
 * Last Modified 10/4/16 11:03 AM
 */

package com.tokopedia.core.home;

import com.tokopedia.core.home.model.homeMenu.CategoryMenuModel;

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
