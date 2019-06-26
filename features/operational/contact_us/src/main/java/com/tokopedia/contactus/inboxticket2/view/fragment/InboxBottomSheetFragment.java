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
import com.tokopedia.contactus.common.analytics.ContactUsTracking;
import com.tokopedia.contactus.common.analytics.InboxTicketTracking;
import com.tokopedia.contactus.inboxticket2.view.activity.InboxDetailActivity;
import com.tokopedia.contactus.inboxticket2.view.contract.InboxBaseContract;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public abstract class InboxBottomSheetFragment extends BottomSheetDialogFragment {

    private TextView title;

    private static String RESID = "RES_ID";

    private int layoutID;


    public InboxBottomSheetFragment() {
    }

    abstract public void setAdapter(RecyclerView.Adapter adapter);
    abstract public void setPresenter(InboxBaseContract.InboxBasePresenter presenter);

    public static InboxBottomSheetFragment getBottomSheetFragment(int resID) {
        InboxBottomSheetFragment fragment = null;
        if (resID == R.layout.layout_bottom_sheet_fragment) {
            fragment = new BottomSheetListFragment();
        } else if (resID == R.layout.layout_bad_csat) {
            fragment = new BottomSheetButtonsFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt(RESID, resID);
        if (fragment != null)
            fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutID = getArguments().getInt(RESID, R.layout.layout_bottom_sheet_fragment);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(layoutID, container, false);
        title = contentView.findViewById(R.id.tv_bottom_sheet_title);
        title.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (getActivity(), R.drawable.ic_close_x_black), null, null , null);
        getDialog().setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            View bottomSheetInternal = d.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeBottomSheet();
            }
        });

        return contentView;

    }

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
