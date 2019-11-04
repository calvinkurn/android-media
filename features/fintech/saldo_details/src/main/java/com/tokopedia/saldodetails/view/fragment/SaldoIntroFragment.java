package com.tokopedia.saldodetails.view.fragment;

import android.content.Context;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants;
import com.tokopedia.saldodetails.view.activity.SaldoWebViewActivity;

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
        View view = inflater.inflate(com.tokopedia.saldodetails.R.layout.fragment_saldo_intro, container, false);
        viewMore = view.findViewById(com.tokopedia.saldodetails.R.id.si_view_more);
        gotoSaldoPage = view.findViewById(com.tokopedia.saldodetails.R.id.si_goto_balance_page);
        ((TextView) view.findViewById(com.tokopedia.saldodetails.R.id.dana_refund)).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (view.getContext(), com.tokopedia.saldodetails.R.drawable.ic_refund), null, null, null);
        ((TextView) view.findViewById(com.tokopedia.saldodetails.R.id.disbursement_fund)).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (view.getContext(), com.tokopedia.saldodetails.R.drawable.ic_refund_disbursement), null, null, null);
        ((TextView) view.findViewById(com.tokopedia.saldodetails.R.id.hasil_penjualan)).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (view.getContext(), com.tokopedia.saldodetails.R.drawable.ic_sales_report), null, null, null);
        ((TextView) view.findViewById(com.tokopedia.saldodetails.R.id.disbursement_priority_balance)).setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (view.getContext(), com.tokopedia.saldodetails.R.drawable.ic_balance_disbursement), null, null, null);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String text = getResources().getString(com.tokopedia.saldodetails.R.string.saldo_intro_help);

        SpannableString spannableString = new SpannableString(text);
        String indexOfString = getString(com.tokopedia.saldodetails.R.string.saldo_help_text);
        int startIndexOfLink = text.indexOf(indexOfString);
        if (startIndexOfLink != -1) {
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    startWebView(SaldoDetailsConstants.SALDO_HELP_URL);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(getResources().getColor(com.tokopedia.design.R.color.green_250));
                }
            }, startIndexOfLink, startIndexOfLink + getResources().getString(com.tokopedia.saldodetails.R.string.saldo_help_text).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewMore.setMovementMethod(LinkMovementMethod.getInstance());
            viewMore.setText(spannableString);
        }

        gotoSaldoPage.setOnClickListener(v -> {
            RouteManager.route(context, ApplinkConstInternalGlobal.SALDO_DEPOSIT);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    private void startWebView(String url) {
        startActivity(SaldoWebViewActivity.getWebViewIntent(context, url));
    }
}

