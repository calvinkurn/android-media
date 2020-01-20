package com.tokopedia.events.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.events.di.scope.EventScope;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.contractor.EventFavouriteContract;
import com.tokopedia.events.view.contractor.EventFilterContract;
import com.tokopedia.events.view.contractor.EventReviewTicketsContractor;
import com.tokopedia.events.view.contractor.EventSearchContract;
import com.tokopedia.events.view.contractor.EventsContract;
import com.tokopedia.events.view.contractor.EventsDetailsContract;
import com.tokopedia.events.view.contractor.ScanCodeContract;
import com.tokopedia.events.view.contractor.SeatSelectionContract;
import com.tokopedia.events.view.customview.SelectEventDateBottomSheet;
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

    EventFilterContract.EventFilterPresenter getEventFilterPresenter();

    EventBookTicketContract.BookTicketPresenter getEventBookTicketPresenter();

    EventsDetailsContract.EventDetailPresenter getEventDetailsPresenter();

    EventFavouriteContract.EventFavoritePresenter getEventFavoritePresenter();

    EventSearchContract.EventSearchPresenter getEventSearchPresenter();

    EventsContract.EventHomePresenter getEventHomePresenter();

    EventReviewTicketsContractor.EventReviewTicketPresenter getReviewTicketPresenter();

    SeatSelectionContract.SeatSelectionPresenter getSeatSelectionPresenter();

    ScanCodeContract.ScanPresenter getScanCodePresenter();

}
