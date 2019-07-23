package com.tokopedia.events.view.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.events.R;
import com.tokopedia.events.view.adapter.AddTicketAdapter;
import com.tokopedia.events.view.contractor.EventBookTicketContract;
import com.tokopedia.events.view.viewmodel.PackageViewModel;

import java.util.List;

public class FragmentAddTickets extends Fragment {
    private static final String ARG_PARAM1 = "typecount";

    private List<PackageViewModel> mPackages;

    private EventBookTicketContract.BookTicketPresenter mPresenter;
    private SpaceItemDecoration dividerItemDecoration;
    private RecyclerView scrollView;
    private AddTicketAdapter ticketAdapter;

    public FragmentAddTickets() {
        // Required empty public constructor
    }

    public static FragmentAddTickets newInstance(int typecount) {
        FragmentAddTickets fragment = new FragmentAddTickets();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, typecount);
        fragment.setArguments(args);
        return fragment;
    }

    public void setData(List<PackageViewModel> packages, EventBookTicketContract.BookTicketPresenter presenter) {
        this.mPresenter = presenter;
        this.mPackages = packages;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ticketAdapter = new AddTicketAdapter(getActivity(), mPackages, mPresenter);
        scrollView = (RecyclerView) inflater.inflate(R.layout.fragment_add_tickets, container, false);
        scrollView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        scrollView.setAdapter(ticketAdapter);
        scrollView.setHasFixedSize(true);
        dividerItemDecoration = new SpaceItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getContext().getResources().getDrawable(R.drawable.recycler_view_divider));
        scrollView.addItemDecoration(dividerItemDecoration);

        return scrollView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.setChildFragment(this);
    }

    class SpaceItemDecoration extends DividerItemDecoration {

        /**
         * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
         * {@link LinearLayoutManager}.
         *
         * @param context     Current context, it will be used to access resources.
         * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
         */
        private int mSpace;

        public SpaceItemDecoration(Context context, int orientation) {
            super(context, orientation);
        }

        public void setSpace(int space) {
            mSpace = space;
        }

        public int getSpace() {
            return mSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = mSpace;
                outRect.top = 0; //don't forget about recycling...
            }
        }
    }

    public void setDecorationHeight(int buttonHeight) {
        if (dividerItemDecoration.getSpace() != buttonHeight) {
            dividerItemDecoration.setSpace(buttonHeight);
            scrollView.invalidateItemDecorations();
        }
    }

    public void scrollToLast() {
        scrollView.smoothScrollToPosition(scrollView.getBottom());
    }

    public void resetAdapter() {
        if (ticketAdapter == null)
            ticketAdapter = new AddTicketAdapter(getActivity(), mPackages, mPresenter);
        ticketAdapter.setData(mPackages);
        ticketAdapter.notifyDataSetChanged();
    }

}
