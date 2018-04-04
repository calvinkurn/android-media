package com.tokopedia.tkpd.home.fragment;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.InAppMessageAdapter;
import com.tokopedia.tkpd.home.model.InAppMessageItemModel;
import com.tokopedia.tkpd.home.model.InAppMessageModel;

import java.util.ArrayList;

/**
 * Created by ashwanityagi on 30/03/18.
 */

public class InappMessageDialogFragment extends DialogFragment implements InAppMessageAdapter.InappAdapterLisner {

    private static final String ARG_PARAM_EXTRA_MESSAGES_LIST = "ARG_PARAM_EXTRA_MESSAGES_LIST";
    private static final String ARG_PARAM_EXTRA_MESSAGES_TITLE = "ARG_PARAM_EXTRA_MESSAGES_LIST_TITLE";
    private static final String ARG_PARAM_EXTRA_MESSAGES_DESC = "ARG_PARAM_EXTRA_MESSAGES_DESC";
    private static final String ARG_PARAM_EXTRA_MESSAGES_TYPE = "ARG_PARAM_EXTRA_MESSAGES_TYPE";

    InAppMessageModel inAppMessageModel;


    public static InappMessageDialogFragment newInstance(InAppMessageModel inAppMessageModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_PARAM_EXTRA_MESSAGES_LIST,
                (Parcelable) inAppMessageModel);

        InappMessageDialogFragment inappMessageDialogFragment = new InappMessageDialogFragment();
        inappMessageDialogFragment.setArguments(bundle);

        return inappMessageDialogFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_PARAM_EXTRA_MESSAGES_LIST,
                (Parcelable)  inAppMessageModel);

        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            inAppMessageModel = getArguments().getParcelable(ARG_PARAM_EXTRA_MESSAGES_LIST);

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return inflater.inflate(R.layout.inapp_message_dialog, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (inAppMessageModel == null){
            dismiss();
        }

        RecyclerView rv = view.findViewById(R.id.rv_inapp);
        if ("1".equalsIgnoreCase(inAppMessageModel.type)) {
            GridLayoutManager gridLayoutManagerVertical =
                    new GridLayoutManager(getActivity(),
                            2, //The number of Columns in the grid
                            LinearLayoutManager.VERTICAL,
                            false);
            rv.setLayoutManager(gridLayoutManagerVertical);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(layoutManager);
        }

        TextView tvTitle =  view.findViewById(R.id.tv_inapp_title);
        TextView tvDesc =  view.findViewById(R.id.tv_inapp_desc);
        tvTitle.setText(inAppMessageModel.title);
        tvDesc.setText(inAppMessageModel.description);

        InAppMessageAdapter inAppMessageAdapter = new InAppMessageAdapter(getActivity(), (ArrayList<InAppMessageItemModel>) inAppMessageModel.messageList, this);
        rv.setAdapter(inAppMessageAdapter);

        view.findViewById(R.id.img_inapp_cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRowItemClick() {
        dismiss();
    }
}
