package com.tokopedia.core.welcome.view;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.core.R;
import com.tokopedia.core.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by stevenfredian on 11/11/16.
 */

public class InfoWelcomeDialogFragment extends DialogFragment {

    public static final String TAG = "DialogWelcome";

    @BindView(R2.id.buyer_button)
    TextView buyerButton;
    private Unbinder unbinder;

    public static InfoWelcomeDialogFragment newInstance() {
        InfoWelcomeDialogFragment fragment = new InfoWelcomeDialogFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.dialog_info_welcome, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        String sourceString = "Belanja sebagai Pembeli";

        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(getResources().getColor(R2.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf("Pembeli")
                , sourceString.length()
                ,0);

        buyerButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R2.id.buyer_button)
    public void moveToMainApp(){
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.tokopedia.tkpd"));
            startActivity(intent);
        } catch (Exception e) { //google play app is not installed
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.tokopedia.tkpd"));
            startActivity(intent);
        }
    }

    @OnClick(R2.id.seller_button)
    public void dismissDialog(){
        this.dismiss();
    }
}
