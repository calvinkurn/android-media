package com.tokopedia.topchat.chatroom.view.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.ReasonAdapter;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by nisie on 6/11/18.
 */
public class ReasonBottomSheet extends BottomSheetDialog {

    @Inject
    ReasonAdapter adapter;
    private Context context;
    private ArrayList<String> reasonList;
    private RecyclerView reasonRecyclerView;
    private ImageView closeIcon;


    public ReasonBottomSheet(@NonNull Context context) {
        super(context);
    }

    public ReasonBottomSheet(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ReasonBottomSheet(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ReasonBottomSheet(@NonNull Context context, @NonNull ArrayList<String> reasonList) {
        super(context);
        this.context = context;
        this.reasonList = reasonList;
        init();
    }

    private void init() {
        View bottomSheetView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .bottom_sheet_reason, null);
        setContentView(bottomSheetView);

        reasonRecyclerView = bottomSheetView.findViewById(R.id.reason_rv);
        closeIcon = bottomSheetView.findViewById(R.id.close_icon);

        reasonRecyclerView.setFocusable(false);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReasonBottomSheet.this.dismiss();
            }
        });

    }

}
