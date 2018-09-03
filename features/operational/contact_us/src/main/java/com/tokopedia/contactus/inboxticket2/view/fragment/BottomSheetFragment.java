package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView;
import com.tokopedia.contactus.R;
import com.tokopedia.contactus.R2;
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    @BindView(R2.id.rv_filter)
    VerticalRecyclerView rvBottomSheet;
    @BindView(R2.id.tv_bottom_sheet_title)
    TextView title;

    private int titleID;

    RecyclerView.Adapter mAdapter;


    public BottomSheetFragment() {
    }

    public void setAdapter(RecyclerView.Adapter adapter, int titleid) {
        mAdapter = adapter;
        titleID = titleid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.layout_bottom_sheet_fragment, container, false);
        ButterKnife.bind(this, contentView);
        rvBottomSheet.setAdapter(mAdapter);
        title.setText(titleID);
        getDialog().setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        });
        return contentView;

    }

    @OnClick(R2.id.tv_bottom_sheet_title)
    void closeBottomSheet() {
        if (getActivity() instanceof InboxDetailActivity) {
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickReason,
                    "Closing Reason Pop Up");
        } else {
            ContactUsTracking.sendGTMInboxTicket("",
                    InboxTicketTracking.Category.EventInboxTicket,
                    InboxTicketTracking.Action.EventClickFilter,
                    "Closing Status Pop Up");
        }
        dismiss();
    }
}
