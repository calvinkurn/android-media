package com.tokopedia.events.view.contractor;

import android.support.v4.app.Fragment;

import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.fragment.FragmentAddTickets;
import com.tokopedia.events.view.utils.ImageTextViewHolder;
import com.tokopedia.events.view.viewmodel.EventsDetailsViewModel;
import com.tokopedia.events.view.viewmodel.LocationDateModel;
import com.tokopedia.events.view.viewmodel.PackageViewModel;
import com.tokopedia.events.view.viewmodel.SchedulesViewModel;

import java.util.List;

/**
 * Created by pranaymohapatra on 28/11/17.
 */

public class EventBookTicketContract {

    public interface EventBookTicketView extends EventBaseContract.EventBaseView {

        void renderFromDetails(EventsDetailsViewModel homedata);

        void showPayButton(int ticketQuantity, int price, String type);

        void hidePayButton();

        void renderSeatmap(String url);

        void hideSeatmap();

        int getButtonLayoutHeight();

        int getRequestCode();

        void setLocationDate(String location, String date, SchedulesViewModel datas);
    }

    public interface BookTicketPresenter extends EventBaseContract.EventBasePresenter {

        void validateSelection();

        void payTicketsClick(String title);

        String getSCREEN_NAME();

        List<LocationDateModel> getLocationDateModels();

        void onClickLocationDate(LocationDateModel model, int index);

        void addTickets(int index, PackageViewModel packageVM, AddTicketAdapter.TicketViewHolder ticketViewHolder);

        void removeTickets();

        void resetViewHolders();

        void setChildFragment(FragmentAddTickets fragment);
    }
}
