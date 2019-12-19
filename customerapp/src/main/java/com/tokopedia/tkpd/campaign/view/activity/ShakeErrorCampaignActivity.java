package com.tokopedia.tkpd.campaign.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.campaign.view.model.TCLandingModel;

/**
 * Created by Rizky on 15/04/18.
 */

public class ShakeErrorCampaignActivity extends BaseSimpleActivity {

    public static final String EXTRA_TC_LANDING_MODEL = "EXTRA_TC_LANDING_MODEL";

    private static final String DEFAULT_APPLINK = "tokopedia://home";
    private static final String DEFAULT_BUTTON = "Kembali ke Beranda";

    @DeepLink(ApplinkConst.TC_LANDING)
    public static Intent getApplinkCallingIntent(Context context, Bundle extras) {
        String title = extras.getString(TCLandingModel.TITLE);
        String errorMessage = extras.getString(TCLandingModel.ERROR_MESSAGE);
        String message = extras.getString(TCLandingModel.MESSAGE);
        String applink = extras.getString(TCLandingModel.APPLINK);
        String button = extras.getString(TCLandingModel.BUTTON);

        if (TextUtils.isEmpty(applink)) {
            applink = DEFAULT_APPLINK;
        }
        if (TextUtils.isEmpty(button)) {
            button = DEFAULT_BUTTON;
        }

        TCLandingModel tcLandingModel = new TCLandingModel.Builder()
                .title(title)
                .errorMessage(errorMessage)
                .message(message)
                .applink(applink)
                .button(button)
                .build();

        return new Intent(context, ShakeErrorCampaignActivity.class)
                .putExtra(EXTRA_TC_LANDING_MODEL, tcLandingModel);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TCLandingModel tcLandingModel;

        String title = null;
        String message = null;
        String applink = null;
        String button = null;

        if (getIntent().getExtras() != null) {
            tcLandingModel = getIntent().getExtras().getParcelable(EXTRA_TC_LANDING_MODEL);
            if (tcLandingModel != null) {
                title = tcLandingModel.getTitle();
                message = tcLandingModel.getMessage();
                applink = tcLandingModel.getApplink();
                button = tcLandingModel.getButton();
            }
        }

        TextView textTitle = findViewById(R.id.text_title);
        if (!TextUtils.isEmpty(title)) {
            textTitle.setText(title);
        }

        TextView textErrorMessage = findViewById(R.id.text_error_message);
        if (!TextUtils.isEmpty(message)) {
            textErrorMessage.setText(message);
        }

        Button buttonReturn = findViewById(R.id.button_return);
        buttonReturn.setText(button);
        final String finalApplink = applink;
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((TkpdCoreRouter) getApplication()).isSupportedDelegateDeepLink(finalApplink)) {
                    RouteManager.route(ShakeErrorCampaignActivity.this, finalApplink);
                }
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shake_error_campaign;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

}
