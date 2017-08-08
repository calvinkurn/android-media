package com.tokopedia.tkpd.home.tokocash;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.tkpd.R;

/**
 * Created by kris on 6/21/17. Tokopedia
 */

public class BottomSheetTokoCash extends BottomSheetDialog {

    private static final String TOKO_CASH_URL = "url";

    private Context context;

    private TextView cashBackText;
    private TextView goToCashBackButton;

    private String activationUrl;
    private String amount;

    private static final String tokoCashText = "Anda mendapatkan cashback TokoCash sebesar XXX " +
            "dari transaksi terakhir";

    public BottomSheetTokoCash(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public BottomSheetTokoCash(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        initView(context);
    }

    protected BottomSheetTokoCash(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        View viewBottomSheetTokoCash = ((Activity) context).getLayoutInflater().inflate(R.layout.toko_cash_bottom_sheet, null);
        setContentView(viewBottomSheetTokoCash);
        TextView closeButton = (TextView) viewBottomSheetTokoCash.findViewById(R.id.button_bottom_sheet_close_cash_back);
        goToCashBackButton = (TextView) viewBottomSheetTokoCash.findViewById(R.id.button_bottom_sheet_to_cash_back);
        cashBackText = (TextView) viewBottomSheetTokoCash.findViewById(R.id.body_bottom_sheet_cash_back);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setActivationUrl(final String activationUrl) {
        //this.activationUrl = activationUrl;
        goToCashBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String seamlessUrl;
                seamlessUrl = URLGenerator.generateURLSessionLogin((Uri.encode(activationUrl)),
                        getContext());
                openTokoCashWebView(seamlessUrl);
            }
        });
    }

    public void setCashBackText(String amount) {
        cashBackText.setText(tokoCashText.replace("XXX", amount));
    }

    private void openTokoCashWebView(String redirectURL) {
        if (context instanceof Activity) {
            if (((Activity) context).getApplication() instanceof TkpdCoreRouter) {
                ((TkpdCoreRouter) ((Activity) context).getApplication())
                        .goToWallet(context, redirectURL);
                dismiss();
            }
        }
    }
}
