package com.tokopedia.contactus.inboxticket2.view.presenter;

import android.content.Intent;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RestResponse;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.home.view.ContactUsHomeActivity;
import com.tokopedia.contactus.inboxticket2.domain.TicketListResponse;
import com.tokopedia.contactus.inboxticket2.domain.TicketsItem;
import com.tokopedia.contactus.inboxticket2.domain.usecase.GetTicketListUseCase;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;
import com.tokopedia.contactus.inboxticket2.view.adapter.InboxFilterAdapter;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxListContract;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.fragment.InboxBottomSheetFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

public class InboxListPresenterImpl
        implements InboxListContract.InboxListPresenter, CustomEditText.Listener {

    private InboxListContract.InboxListView mView;
    private GetTicketListUseCase mUseCase;
    private List<String> filterList;
    private List<TicketsItem> originalList;
    private boolean isLoading;
    private boolean isLastPage;
    private InboxFilterAdapter filterAdapter;
    private boolean fromFilter;
    private String nextUrl;

    public InboxListPresenterImpl(GetTicketListUseCase useCase) {
        mUseCase = useCase;
        originalList = new ArrayList<>();
    }

    @Override
    public void attachView(InboxBaseContract.InboxBaseView view) {
        mView = (InboxListContract.InboxListView) view;
        filterList = new ArrayList<>(Arrays.asList(mView.getActivity().getResources().getStringArray(R.array.filterarray)));
        getTicketList();
    }

    private void getTicketList() {
        mView.showProgressBar();
        mUseCase.setUrl("");
        mUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                Type token = new TypeToken<DataResponse<TicketListResponse>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                DataResponse ticketListResponse = res1.getData();
                TicketListResponse ticketListResponse1 = (TicketListResponse) ticketListResponse.getData();
                if (ticketListResponse1 != null && !ticketListResponse1.getTickets().isEmpty()) {
                    mView.toggleEmptyLayout(View.GONE);
                    originalList.clear();
                    originalList.addAll(ticketListResponse1.getTickets());
                    nextUrl = ticketListResponse1.getNextPage();
                    isLastPage = !(nextUrl != null && !nextUrl.isEmpty() && nextUrl.length() > 0);
                    mView.renderTicketList(originalList);
                    mView.showFilter();
                } else if (fromFilter) {
                    originalList.clear();
                    mView.toggleEmptyLayout(View.VISIBLE);
                    mView.showFilter();
                    fromFilter = false;
                } else {
                    mView.toggleEmptyLayout(View.VISIBLE);
                    mView.hideFilter();
                }
                mView.hideProgressBar();
            }
        });
    }

    @Override
    public void detachView() {

    }

    @Override
    public void onClickFilter() {
        mView.showBottomFragment();
    }

    @Override
    public void setFilter(int position) {
        final int ALL = 0;
        final int UNREAD = 1;
        final int NEEDRATING = 2;
        final int INPROGRESS = 3;
        final int READ = 4;
        final int CLOSED = 5;
        String selectedFilter = "";
        fromFilter = true;
        switch (position) {
            case ALL:
                mUseCase.setQueryMap(0, 0, 0);
                getTicketList();
                selectedFilter = filterList.get(ALL);
                filterAdapter.setSelected(ALL);
                break;
            case UNREAD:
                mUseCase.setQueryMap(0, 1, 0);
                selectedFilter = filterList.get(UNREAD);
                getTicketList();
                break;
            case NEEDRATING:
                mUseCase.setQueryMap(2, 0, 1);
                selectedFilter = filterList.get(NEEDRATING);
                getTicketList();
                break;
            case INPROGRESS:
                mUseCase.setQueryMap(1, 0, 0);
                selectedFilter = filterList.get(INPROGRESS);
                getTicketList();
                break;
            case READ:
                mUseCase.setQueryMap(0, 2, 0);
                selectedFilter = filterList.get(READ);
                getTicketList();
                break;
            case CLOSED:
                mUseCase.setQueryMap(2, 0, 2);
                selectedFilter = filterList.get(CLOSED);
                getTicketList();
                break;
        }
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickFilter,
                selectedFilter);
        mView.hideBottomFragment();
    }

    @Override
    public void onClickTicket(int index, boolean isOfficalStore) {
        Intent detailIntent = new Intent(mView.getActivity(), InboxDetailActivity.class);
        detailIntent.putExtra(InboxDetailActivity.PARAM_TICKET_ID, originalList.get(index).getId());
        detailIntent.putExtra(InboxDetailActivity.IS_OFFICIAL_STORE, isOfficalStore);
        mView.navigateToActivityRequest(detailIntent, InboxListContract.InboxListView.REQUEST_DETAILS);
        ContactUsTracking.sendGTMInboxTicket("",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventTicketClick,
                originalList.get(index).getStatus());
    }

    @Override
    public void scrollList() {
        mView.scrollRv();
    }

    @Override
    public void onRecyclerViewScrolled(LinearLayoutManager layoutManager) {
        checkIfToLoad(layoutManager);
    }

    @Override
    public CustomEditText.Listener getSearchListener() {
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == InboxBaseContract.InboxBaseView.REQUEST_DETAILS) {
            if (resultCode == InboxBaseContract.InboxBaseView.RESULT_FINISH) {
                mView.navigateToActivityRequest(new Intent(mView.getActivity(), ContactUsHomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 100);
                mView.getActivity().finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        mUseCase.unsubscribe();
    }

    private InboxFilterAdapter getFilterAdapter() {
        if (filterAdapter == null)
            filterAdapter = new InboxFilterAdapter(filterList, mView.getActivity(), this);
        return filterAdapter;
    }

    @Override
    public BottomSheetDialogFragment getBottomFragment(int resID) {
        InboxBottomSheetFragment bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID);
        bottomFragment.setAdapter(getFilterAdapter());
        return bottomFragment;
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
    public void reAttachView() {
        getTicketList();
    }

    @Override
    public void clickCloseSearch() {
        if (mView.isSearchMode()) {
            mView.toggleSearch(View.GONE);
        } else {
            mView.clearSearch();
        }
    }

    private void checkIfToLoad(LinearLayoutManager layoutManager) {
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading && !isLastPage) {
            int PAGE_SIZE = 10;
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                loadMoreItems();
            } else {
                mView.addFooter();
            }
        } else {
            mView.removeFooter();
        }
    }

    private void loadMoreItems() {
        isLoading = true;
        mUseCase.setUrl(nextUrl);
        mUseCase.execute(new Subscriber<Map<Type, RestResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                isLoading = false;
            }

            @Override
            public void onNext(Map<Type, RestResponse> typeRestResponseMap) {
                isLoading = false;
                mView.removeFooter();
                Type token = new TypeToken<DataResponse<TicketListResponse>>() {
                }.getType();
                RestResponse res1 = typeRestResponseMap.get(token);
                DataResponse ticketListResponse = res1.getData();
                TicketListResponse ticketListResponse1 = (TicketListResponse) ticketListResponse.getData();
                if (ticketListResponse1 != null && !ticketListResponse1.getTickets().isEmpty()) {
                    originalList.addAll(ticketListResponse1.getTickets());
                    nextUrl = ticketListResponse1.getNextPage();
                    isLastPage = !(nextUrl != null && !nextUrl.isEmpty() && nextUrl.length() > 0);
                    mView.updateDataSet();
                }
            }
        });
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {
        if (text.length() > 0) {
            mView.toggleSearch(View.VISIBLE);
        }
    }


}
