package com.tokopedia.events.view.contractor;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.CategoryItemsViewModel;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;

/**
 * Created by ashwanityagi on 23/11/17.
 */

public class EventsDetailsContract {
    public interface EventDetailsView extends EventBaseContract.EventBaseView {

        void renderFromHome(CategoryItemsViewModel homedata);

        void renderSeatLayout(String url);

        void renderSeatmap(String url);

        void renderFromCloud(EventsDetailsViewModel data);

        void setHolder(int resID, String label, ImageTextViewHolder holder);
    }

    public interface EventDetailPresenter extends CustomerPresenter<EventDetailsView> {

        void initialize();

        void onDestroy();

        public String getSCREEN_NAME();


    }

}
