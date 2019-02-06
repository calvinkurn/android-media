package com.tokopedia.loyalty.view.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.loyalty.R;
import com.tokopedia.loyalty.common.PopUpNotif;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.util.PromoTrackingUtil;

/**
 * @author anggaprasetiyo on 07/12/17.
 */

public class LoyaltyNotifFragmentDialog extends DialogFragment {
    public static final String ARG_EXTRA_POP_UP_NOTIFICATION = "ARG_EXTRA_POP_UP_NOTIFICATION";

    TextView tvTitle;
    TextView tvDesc;
    TextView tvAction;
    ImageView ivPic;

    private PromoTrackingUtil promoTrackingUtil;

    public static DialogFragment newInstance(PopUpNotif popUpNotifData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_EXTRA_POP_UP_NOTIFICATION, popUpNotifData);
        DialogFragment fragment = new LoyaltyNotifFragmentDialog();
        fragment.setArguments(bundle);
        return fragment;
    }

    private PopUpNotif popUpNotifData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getActivity().getApplication() instanceof LoyaltyModuleRouter) {
            promoTrackingUtil = new PromoTrackingUtil((LoyaltyModuleRouter)getActivity().getApplication());
        }
        this.popUpNotifData = getArguments().getParcelable(ARG_EXTRA_POP_UP_NOTIFICATION);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_dialog_notify_loyalty, container, false);
        if(promoTrackingUtil!= null){
            promoTrackingUtil.eventViewTokopointPopup(getActivity());
        }
        tvTitle = view.findViewById(R.id.tv_tokopoint_notif_title);
        tvDesc = view.findViewById(R.id.tv_tokopoint_notif_desc);
        ivPic = view.findViewById(R.id.iv_tokopoint_notif_image);
        tvAction = view.findViewById(R.id.tv_tokopoint_notif_action);
        tvTitle.setText(popUpNotifData.getTitle());

        tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
        tvDesc.setText(Html.fromHtml(popUpNotifData.getText()));
        ImageHandler.loadImageThumbs(getActivity(), ivPic, popUpNotifData.getImageUrl());
        tvAction.setText(popUpNotifData.getButtonText());
        tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoTrackingUtil.eventClickTokoPointPopup(getActivity());
                dismiss();

                if (!TextUtils.isEmpty(popUpNotifData.getAppLink())) {
                    RouteManager.route(getActivity(), popUpNotifData.getAppLink());
                } else if (!TextUtils.isEmpty(popUpNotifData.getButtonUrl())) {
                    if (getActivity().getApplication() instanceof LoyaltyModuleRouter) {
                        ((LoyaltyModuleRouter) getActivity().getApplication())
                                .actionOpenGeneralWebView(getActivity(), popUpNotifData.getButtonUrl());
                    }
                }
            }
        });
        return view;
    }
}
