package com.tokopedia.events.view.contractor;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 16/05/18.
 */

public class EventFavouriteContract {
    public interface EventFavouriteView extends CustomerView {
        void showMessage(String message);

        Activity getActivity();

        void navigateToActivityRequest(Intent intent, int requestCode);

        void renderFavourites(List<CategoryItemsViewModel> categoryItemsViewModels);

        void showProgressBar();

        void hideProgressBar();

        android.view.View getRootView();

        void toggleEmptyLayout(int visibility);

    }

    public interface Presenter extends CustomerPresenter<EventFavouriteContract.EventFavouriteView> {

        public String getSCREEN_NAME();

        void setEventLike(CategoryItemsViewModel model, int position);

        void shareEvent(CategoryItemsViewModel model);
    }
}
