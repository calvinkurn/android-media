package com.tokopedia.tkpd.home.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.adapter.InAppMessageAdapter;
import com.tokopedia.tkpd.home.model.InAppMessageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashwanityagi on 30/03/18.
 */

public class InappMessageDialogFragment extends DialogFragment {

    private static final String ARG_PARAM_EXTRA_MESSAGES_LIST_DATA =
            "ARG_PARAM_EXTRA_MESSAGES_LIST_DATA";
    private List<InAppMessageModel> messagesList;


    public static InappMessageDialogFragment newInstance(List<InAppMessageModel> messagesList){
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_PARAM_EXTRA_MESSAGES_LIST_DATA,
                (ArrayList<? extends Parcelable>) messagesList);
        InappMessageDialogFragment inappMessageDialogFragment=new InappMessageDialogFragment();
        inappMessageDialogFragment.setArguments(bundle);

        return inappMessageDialogFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ARG_PARAM_EXTRA_MESSAGES_LIST_DATA,
                (ArrayList<? extends Parcelable>) messagesList);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
          messagesList = getArguments().getParcelableArrayList(ARG_PARAM_EXTRA_MESSAGES_LIST_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.inapp_message_dialog, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv= (RecyclerView) view.findViewById(R.id.rv_inapp);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        InAppMessageAdapter inAppMessageAdapter=new InAppMessageAdapter(getActivity(),(ArrayList<InAppMessageModel>) messagesList);
        rv.setAdapter(inAppMessageAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
