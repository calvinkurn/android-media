package com.tokopedia.gamification.taptap.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationRouter;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.taptap.data.entiity.BackButton;

public class BackPopupDialogFragment extends DialogFragment {

    private static String KEY_BTN="backbtn";

    public static BackPopupDialogFragment createDialog(BackButton backButton){

        BackPopupDialogFragment backPopupDialogFragment=new BackPopupDialogFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(KEY_BTN, backButton);
        backPopupDialogFragment.setArguments(bundle);
        return backPopupDialogFragment;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.gf_popup_exit_game, container, false);

        BackButton backButton=getArguments().getParcelable(KEY_BTN);

        TextView textHeader = view.findViewById(R.id.tv_header);
        TextView textSubHeader = view.findViewById(R.id.tv_subheader);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnOkay = view.findViewById(R.id.btn_ok);
        ImageView imagePopupHeader = view.findViewById(R.id.image_popup_header);
        textHeader.setText(backButton.getTitle());
        textSubHeader.setText(backButton.getText());
        btnOkay.setText(backButton.getYesText());
        btnCancel.setText(backButton.getCancelText());
        ImageHandler.loadImage(getContext(), imagePopupHeader, backButton.getImageURL(), R.color.grey_1100, R.color.grey_1100);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null) {
                    ((GamificationRouter) getContext().getApplicationContext()).goToHome(getContext());
                }
            }
        });
        return view;
    }
}
