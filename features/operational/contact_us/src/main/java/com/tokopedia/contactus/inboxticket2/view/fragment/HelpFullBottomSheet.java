package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.contactus.R;


public class HelpFullBottomSheet extends FrameLayout implements View.OnClickListener {

    private final CloseSHelpFullBottomSheet closeBottomSheetListener;

    public HelpFullBottomSheet(@NonNull Context context,CloseSHelpFullBottomSheet closeBottomSheetListener) {
        super(context);
        this.closeBottomSheetListener = closeBottomSheetListener;
        init();
    }

    private void init() {

        View helpfullView = LayoutInflater.from(getContext()).inflate(R.layout.helpfull_bottom_sheet_layout, this, true);
        TextView noButton = helpfullView.findViewById(R.id.tv_no_button);
        TextView yesButton = helpfullView.findViewById(R.id.tv_yes_button);

        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_no_button){
            closeBottomSheetListener.onClick(false);
        }else if(view.getId()==R.id.tv_yes_button){
            closeBottomSheetListener.onClick(true);
        }

    }


    public interface CloseSHelpFullBottomSheet {
        void onClick(boolean agreed);
    }
}
