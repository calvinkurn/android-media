package com.tokopedia.tkpd.home.fragment;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.core.gcm.Constants;

import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;
import com.tokopedia.tkpd.home.adapter.InAppMessageAdapter;
import com.tokopedia.tkpd.home.model.InAppMessageItemModel;
import com.tokopedia.tkpd.home.model.InAppMessageModel;
import com.tokopedia.tkpd.R;
import java.util.ArrayList;

/**
 * Created by ashwanityagi on 30/03/18.
 */

public class InappMessageDialogFragment extends DialogFragment implements InAppMessageAdapter.InappAdapterLisner {

    private static final String ARG_PARAM_EXTRA_MESSAGES_LIST = "ARG_PARAM_EXTRA_MESSAGES_LIST";
    private InAppMessageModel inAppMessageModel;
    private RecyclerView rv;
    private ImageView closeBtn;
    private TextView tvTitle;
    private TextView tvDesc;
    private LinearLayout headerLayout;
    private final String LEFT = "left";
    private final String RIGHT = "right";
    private final String CENTER = "center";
    private final String type_2="2";
    private final String type_3="3";
    private final String CERCLE="c";

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
                (Parcelable) inAppMessageModel);

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

        if (inAppMessageModel == null) {
            dismiss();
        }

        rv = view.findViewById(R.id.rv_inapp);
        headerLayout = view.findViewById(R.id.header_layout);
        tvTitle = view.findViewById(R.id.tv_inapp_title);
        tvDesc = view.findViewById(R.id.tv_inapp_desc);
        tvTitle.setText(inAppMessageModel.title);
        tvDesc.setText(inAppMessageModel.description);
        closeBtn = view.findViewById(R.id.img_inapp_cross);

        renderView(view);

    }

    private void renderView(View view) {

        try {
            if (!TextUtils.isEmpty(inAppMessageModel.colorTitle)) {
                tvTitle.setTextColor(Color.parseColor(inAppMessageModel.colorTitle));
            }
            if (!TextUtils.isEmpty(inAppMessageModel.colorDesc)) {
                tvDesc.setTextColor(Color.parseColor(inAppMessageModel.colorDesc));
            }
            if (!TextUtils.isEmpty(inAppMessageModel.headerBackgroundColor)) {
                headerLayout.setBackgroundColor(Color.parseColor(inAppMessageModel.headerBackgroundColor));
            }
        } catch (Exception e) {
            tvTitle.setTextColor(Color.BLACK);
            tvDesc.setTextColor(Color.BLACK);
        }

        if (!TextUtils.isEmpty(inAppMessageModel.headerTextAlignment)) {

            switch (inAppMessageModel.headerTextAlignment) {

                case LEFT:
                    tvTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    tvDesc.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    break;
                case RIGHT:
                    tvTitle.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    tvDesc.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                    break;
                case CENTER:
                    tvTitle.setTextAlignment((View.TEXT_ALIGNMENT_CENTER));
                    tvDesc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    break;

                default:
                    tvTitle.setTextAlignment((View.TEXT_ALIGNMENT_CENTER));
                    tvDesc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            }
        }

        if (type_2.equalsIgnoreCase(inAppMessageModel.type)) {
            renderGridViewLayout();
        } else if (type_3.equalsIgnoreCase(inAppMessageModel.type)) {
            renderListAndButtonViewLayout(view);
        } else {
            renderListViewLayout();
        }
        if (getString(R.string.yes).equalsIgnoreCase(inAppMessageModel.closeButtonShow)) {
            closeBtn.setVisibility(View.VISIBLE);
            if(CERCLE.equalsIgnoreCase(inAppMessageModel.closeButtonShape)){
                closeBtn.setBackgroundResource(R.drawable.shape_bg_circle_black);
            }else{
                closeBtn.setBackgroundColor(getResources().getColor(R.color.black));
            }

            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            closeBtn.setVisibility(View.GONE);
        }

        if (getString(R.string.yes).equalsIgnoreCase(inAppMessageModel.headerVisibile)) {
            headerLayout.setVisibility(View.VISIBLE);
        } else {
            headerLayout.setVisibility(View.GONE);
        }

        InAppMessageAdapter inAppMessageAdapter = new InAppMessageAdapter(getActivity(), (ArrayList<InAppMessageItemModel>) inAppMessageModel.messageList, this);
        rv.setAdapter(inAppMessageAdapter);
    }

    private void renderGridViewLayout() {
        GridLayoutManager gridLayoutManagerVertical =
                new GridLayoutManager(getActivity(),
                        2,
                        LinearLayoutManager.VERTICAL,
                        false);
        rv.setLayoutManager(gridLayoutManagerVertical);
    }

    private void renderListViewLayout() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
    }

    private void renderListAndButtonViewLayout(View view) {
        tvTitle.setVisibility(View.GONE);
        tvDesc.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        TextView actionButtonLeft = view.findViewById(R.id.btn_action_1);
        TextView actionButtonRight = view.findViewById(R.id.btn_action_2);
        view.findViewById(R.id.ll_action_button).setVisibility(View.VISIBLE);

        actionButtonLeft.setText(inAppMessageModel.actionBtnText1);
        actionButtonRight.setText(inAppMessageModel.actionBtnText2);

        actionButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeepLink(Uri.parse(inAppMessageModel.actionDeeplink1));
                dismiss();
            }
        });

        actionButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeepLink(Uri.parse(inAppMessageModel.actionDeeplink2));
                dismiss();
            }
        });
    }

    private void handleDeepLink(Uri deepLinkUri) {
        Intent intent;
        if (deepLinkUri.getScheme().equals(Constants.Schemes.HTTP) || deepLinkUri.getScheme().equals(Constants.Schemes.HTTPS)) {
            intent = new Intent(getActivity(), DeepLinkActivity.class);
            intent.setData(Uri.parse(deepLinkUri.toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        } else if (deepLinkUri.getScheme().equals(Constants.Schemes.APPLINKS)) {
            intent = new Intent(getActivity(), DeeplinkHandlerActivity.class);
            intent.setData(Uri.parse(deepLinkUri.toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        }
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
    public void onRowItemClick(Uri deeplinkUri) {
        handleDeepLink(deeplinkUri);
        dismiss();
    }
}
