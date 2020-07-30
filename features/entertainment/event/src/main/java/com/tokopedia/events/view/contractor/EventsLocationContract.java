package com.tokopedia.events.view.contractor;

import com.tokopedia.events.view.viewmodel.EventLocationViewModel;

import java.util.List;

/**
 * Created by ashwanityagi on 03/11/17.
 */

public class EventsLocationContract {
    public interface EventLocationsView extends EventBaseContract.EventBaseView {
        void renderLocationList(List<EventLocationViewModel> eventLocationViewModels);

    }

    public interface EventLocationsPresenter extends EventBaseContract.EventBasePresenter {

    }
}
