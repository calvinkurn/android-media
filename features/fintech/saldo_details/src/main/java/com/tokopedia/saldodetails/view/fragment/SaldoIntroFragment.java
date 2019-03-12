package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
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

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.saldodetails.R;
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants;

public class SaldoIntroFragment extends TkpdBaseV4Fragment {

    private TextView viewMore;
    private Button gotoSaldoPage;
    private Context context;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.context = getContext();
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
                public void onClick(@NonNull View view) {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW,
                            SaldoDetailsConstants.SALDO_HELP_URL));
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(R.color.green_250));
                }
            }, startIndexOfLink, startIndexOfLink + getResources().getString(R.string.saldo_help_text).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewMore.setMovementMethod(LinkMovementMethod.getInstance());
            viewMore.setText(spannableString);
        }

        gotoSaldoPage.setOnClickListener(v -> {
            RouteManager.route(context, ApplinkConst.DEPOSIT);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }
}

