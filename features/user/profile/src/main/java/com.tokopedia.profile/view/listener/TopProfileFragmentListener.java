package com.tokopedia.profile.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.profile.view.viewmodel.TopProfileViewModel;

/**
 * @author by alvinatin on 28/02/18.
 */

public interface TopProfileFragmentListener {

    interface View extends CustomerView{
        Context getContext();

        void renderData(TopProfileViewModel topProfileViewModel);
    }
}
