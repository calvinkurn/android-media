package com.tokopedia.topchat.chatroom.view.customview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.tokopedia.topchat.R;
import com.tokopedia.topchat.chatroom.view.adapter.ReasonAdapter;

import java.util.ArrayList;

/**
 * @author by nisie on 6/11/18.
 */
public class ReasonBottomSheet extends BottomSheetDialog {

    private Context context;
    private ArrayList<String> reasonList;
    private RecyclerView reasonRecyclerView;
    private ImageView closeIcon;
    private ReasonAdapter.OnReasonClickListener listener;

    public ReasonBottomSheet(@NonNull Context context, @NonNull ArrayList<String> reasonList,
                             ReasonAdapter.OnReasonClickListener listener) {
        super(context);
        this.context = context;
        this.reasonList = reasonList;
        this.listener = listener;
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

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager
                .VERTICAL, false);
        reasonRecyclerView.setLayoutManager(mLayoutManager);

        ReasonAdapter adapter = new ReasonAdapter(listener);
        adapter.addList(reasonList);
        reasonRecyclerView.setAdapter(adapter);

    }

    public static ReasonBottomSheet createInstance(Activity activity,
                                                   ArrayList<String> reasons,
                                                   ReasonAdapter.OnReasonClickListener listener) {
        return new ReasonBottomSheet(activity, reasons, listener);
    }
}
