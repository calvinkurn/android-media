package com.tokopedia.sellerapp.welcome.view;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.tokopedia.sellerapp.R;

/**
 * Created by stevenfredian on 11/11/16.
 */

public class InfoWelcomeDialogFragment extends DialogFragment {

    public static final String TAG = "DialogWelcome";

    TextView buyerButton;
    View sellerButton;

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
        View rootView = inflater.inflate(R.layout.dialog_info_welcome, container, false);
        buyerButton = rootView.findViewById(R.id.buyer_button);
        sellerButton = rootView.findViewById(R.id.seller_button);
        initView(rootView);
        return rootView;
    }

    private void initView(View rootView) {
        buyerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.tokopedia.tkpd"));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) { //google play app is not installed
                    goToBrowser();
                }
            }
        });

        sellerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoWelcomeDialogFragment.this.dismiss();
            }
        });
        String sourceString = "Belanja sebagai Pembeli";

        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf("Pembeli")
                , sourceString.length()
                , 0);

        buyerButton.setText(spannable, TextView.BufferType.SPANNABLE);
        buyerButton.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void goToBrowser() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.tokopedia.tkpd"));
        startActivity(intent);
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
    }
}
