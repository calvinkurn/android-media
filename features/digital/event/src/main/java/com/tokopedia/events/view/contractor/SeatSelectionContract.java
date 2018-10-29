package com.tokopedia.events.view.contractor;

import com.tokopedia.events.view.viewmodel.SeatLayoutViewModel;

import java.util.List;

/**
 * Created by naveengoyal on 1/25/18.
 */

public class SeatSelectionContract {

    public interface SeatSelectionView extends EventBaseContract.EventBaseView {

        void renderSeatSelection(int price, int maxTickets, SeatLayoutViewModel viewModel);

        void showPayButton(int ticketQuantity, int price);

        void hidePayButton();


        void setTicketPrice(int numOfTickets);

        void setSelectedSeatText();

        void initializeSeatLayoutModel(List<String> selectedSeatText, List<String> rowIds, List<String> actualSeat);

        void setEventTitle(String text);

        void setSelectedSeatModel();

    }

    public interface SeatSelectionPresenter extends EventBaseContract.EventBasePresenter {

        void validateSelection();

        void onActivityResult(int requestCode);

        public String getSCREEN_NAME();
    }
}
