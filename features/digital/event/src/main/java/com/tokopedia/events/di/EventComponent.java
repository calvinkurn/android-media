package com.tokopedia.events.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.domain.postusecase.VerifyCartUseCase;
import com.tokopedia.events.view.activity.EventBookTicketActivity;
import com.tokopedia.events.view.activity.EventDetailsActivity;
import com.tokopedia.events.view.activity.EventFavouriteActivity;
import com.tokopedia.events.view.activity.EventLocationActivity;
import com.tokopedia.events.view.activity.EventSearchActivity;
import com.tokopedia.events.view.activity.EventsHomeActivity;
import com.tokopedia.events.view.activity.ReviewTicketActivity;
import com.tokopedia.events.view.activity.SeatSelectionActivity;
import com.tokopedia.events.view.contractor.EventBaseContract;
import com.tokopedia.events.view.contractor.EventFilterContract;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.contractor.EventsLocationContract;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.presenter.EventBookTicketPresenter;
import com.tokopedia.events.view.presenter.EventFavouritePresenter;
import com.tokopedia.events.view.presenter.EventFilterPresenterImpl;
import com.tokopedia.events.view.presenter.EventHomePresenter;
import com.tokopedia.events.view.presenter.EventLocationsPresenter;
import com.tokopedia.events.view.presenter.EventReviewTicketPresenter;
import com.tokopedia.events.view.presenter.EventSearchPresenter;
import com.tokopedia.events.view.presenter.EventsDetailsPresenter;
import com.tokopedia.events.view.presenter.SeatSelectionPresenter;
import com.tokopedia.events.view.utils.VerifyCartWrapper;
import com.tokopedia.oms.di.OmsModule;
import com.tokopedia.oms.domain.postusecase.PostVerifyCartUseCase;

import dagger.Component;

/**
 * Created by ashwanityagi on 03/11/17.
 */

@EventScope
@Component(modules = {EventModule.class, OmsModule.class}, dependencies = BaseAppComponent.class)
public interface EventComponent {

    VerifyCartWrapper getVerifyCartWrapper();

    PostVerifyCartUseCase getPostVerifyCartUseCase();

    EventFilterPresenterImpl getEventFilterPresenter();

    EventBookTicketPresenter getEventBookTicketPresenter();

    EventsDetailsPresenter getEventDetailsPresenter();

    EventFavouritePresenter getEventFavoritePresenter();

    EventLocationsPresenter getEventLocationPresenter();

    EventSearchPresenter getEventSearchPresenter();

    EventHomePresenter getEventHomePresenter();

    EventReviewTicketPresenter getReviewTicketPresenter();

    SeatSelectionPresenter getSeatSelectionPresenter();

    void inject(EventsHomeActivity activity);

    void inject(EventLocationActivity activity);

    void inject(EventDetailsActivity activity);

    void inject(EventBookTicketActivity activity);

    void inject(ReviewTicketActivity activity);

    void inject(FragmentAddTickets fragment);

    void inject(EventSearchActivity activity);

    void inject(SeatSelectionActivity activity);

    void inject(EventFavouriteActivity activity);
}
