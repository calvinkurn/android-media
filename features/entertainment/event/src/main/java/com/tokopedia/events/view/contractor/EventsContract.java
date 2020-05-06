package com.tokopedia.events.view.contractor;

import com.tokopedia.abstraction.base.view.widget.TouchViewPager;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.CategoryViewModel;

import java.util.List;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class EventsContract {
    public interface EventHomeView extends EventBaseContract.EventBaseView {
        void renderCategoryList(List<CategoryViewModel> categoryList);

        void hideSearchButton();

        void showSearchButton();

        void showLoginSnackbar(String message);

    }

    public interface EventHomePresenter extends EventBaseContract.EventBasePresenter {

        void startBannerSlide(TouchViewPager viewPager);

        void onBannerSlide(int page);

        boolean onOptionMenuClick(int id);

        void showEventDetails(CategoryItemsViewModel model);

        void setEventLike(CategoryItemsViewModel model, int position);

        void shareEvent(CategoryItemsViewModel model);

        void onActivityResult(int requestCode);

        void onClickEventCalendar();

        void setupCallback(AdapterCallbacks callbacks);

        String getSCREEN_NAME();
    }

    public interface AdapterCallbacks {
        void notifyDatasetChanged(int position);

    }
}
