package com.tokopedia.saldodetails.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.saldodetails.R;

import java.util.Objects;

public class SaldoIntroFragment extends TkpdBaseV4Fragment {

    private TextView viewMore;
    private Button gotoSaldoPage;

    public static Fragment newInstance() {
        return new SaldoIntroFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saldo_intro, container, false);
        viewMore = view.findViewById(R.id.si_view_more);
        gotoSaldoPage = view.findViewById(R.id.si_goto_balance_page);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String text = getResources().getString(R.string.saldo_intro_help);

        SpannableString spannableString = new SpannableString(text);
        String indexOfString = getString(R.string.saldo_help_text);
        int startIndexOfLink = text.indexOf(indexOfString);
        if (startIndexOfLink != -1) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Go to view more page", Toast.LENGTH_LONG).show();
                    // TODO: 24/1/19 goto help page
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(R.color.green_250));
                }
            }, startIndexOfLink, startIndexOfLink + getResources().getString(R.string.saldo_help_text).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewMore.setMovementMethod(LinkMovementMethod.getInstance());
            viewMore.setText(spannableString);
        }

        gotoSaldoPage.setOnClickListener(v -> {
            RouteManager.route(Objects.requireNonNull(getContext()), ApplinkConst.DEPOSIT);
            Objects.requireNonNull(getActivity()).finish();
        });
    }
}

