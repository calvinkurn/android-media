package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.contactus.R;

public class CloseComplainBottomSheet extends FrameLayout implements View.OnClickListener {

    private final CloseComplainBottomSheetListner closeBottomSheetListener;

    public CloseComplainBottomSheet(@NonNull Context context, CloseComplainBottomSheetListner closeComplainBottomSheetListner) {
        super(context);
        this.closeBottomSheetListener = closeComplainBottomSheetListner;
        init();
    }

    private void init() {

        View helpfullView = LayoutInflater.from(getContext()).inflate(R.layout.close_complain_bottom_sheet_layout, this, true);
        TextView noButton = helpfullView.findViewById(R.id.tv_no_button);
        TextView yesButton = helpfullView.findViewById(R.id.tv_yes_button);

        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_no_button){
            closeBottomSheetListener.onClickComplain(false);
        }else if(view.getId()==R.id.tv_yes_button){
            closeBottomSheetListener.onClickComplain(true);
        }

    }


    public interface CloseComplainBottomSheetListner {
        void onClickComplain(boolean agreed);
    }
}
