package com.tokopedia.events.view.contractor;

import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 16/05/18.
 */

public class EventFavouriteContract {
    public interface EventFavouriteView extends EventBaseContract.EventBaseView {
        void renderFavourites(List<CategoryItemsViewModel> categoryItemsViewModels);

        void toggleEmptyLayout(int visibility);

    }

    public interface EventFavoritePresenter extends EventBaseContract.EventBasePresenter {

        public String getSCREEN_NAME();

        void removeEventLike(CategoryItemsViewModel model, int position);

        void shareEvent(CategoryItemsViewModel model);
    }
}
