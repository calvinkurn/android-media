package com.tokopedia.contactus.inboxticket2.view.presenter;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.inboxticket2.domain.TicketDetailResponse;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketDetailUseCase;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxDetailContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;

import java.lang.reflect.Type;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by pranaymohapatra on 02/07/18.
 */

public class InboxDetailPresenterImpl
        implements InboxDetailContract.InboxDetailPresenter, CustomEditText.Listener {

    private InboxDetailContract.InboxDetailView mView;
    private Tickets mTicketDetail;
    private GetTicketDetailUseCase mUsecase;

    public InboxDetailPresenterImpl(GetTicketDetailUseCase useCase) {
        mUsecase = useCase;
    }

    @Override
    public void attachView(InboxBaseContract.InboxBaseView view) {
        mView = (InboxDetailContract.InboxDetailView) view;
        getTicketDetails(mView.getActivity().getIntent().getStringExtra("TICKET_ID"));
    }

    @Override
    public void detachView() {

    }

    @Override
    public CustomEditText.Listener getSearchListener() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public String getSCREEN_NAME() {
        return null;
    }

    @Override
    public BottomSheetDialogFragment getBottomFragment() {
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            mView.toggleSearch(View.VISIBLE);
            return true;
        }
        return false;
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }

    private void getTicketDetails(String id) {
        mView.showProgressBar();
        mUsecase.setTicketId(id);
        mUsecase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.d("ONERROR", e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<TicketDetailResponse>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                DataResponse ticketListResponse = res1.getData();
                TicketDetailResponse ticketDetailResponse = (TicketDetailResponse) ticketListResponse.getData();
                if (ticketDetailResponse != null && ticketDetailResponse.getTickets() != null) {
                    mTicketDetail = ticketDetailResponse.getTickets();
                    mView.renderMessageList(mTicketDetail);
                    mView.hideProgressBar();
                }
            }
        });

    }
}
