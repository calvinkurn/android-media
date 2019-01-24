package com.tokopedia.home.account.presentation.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.text.Html;
import android.widget.TextView;

import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.home.account.R;

public class SaldoInfoBottomSheet extends BottomSheetView {

    public SaldoInfoBottomSheet(@NonNull Context context) {
        super(context);
    }

    private TextView titleTV;
    private TextView bodyTV;
    private TextView actionButtonTV;

    @Override
    protected int getLayout() {
        return R.layout.saldo_info_bottom_sheet;
    }

    @Override
    protected void init(Context context) {
        if (context instanceof Activity) layoutInflater = ((Activity) context).getLayoutInflater();
        else layoutInflater = ((Activity) ((ContextWrapper) context)
                .getBaseContext()).getLayoutInflater();

        bottomSheetView = layoutInflater.inflate(getLayout(), null);
        setContentView(bottomSheetView);


        titleTV = bottomSheetView.findViewById(R.id.title_text_view);
        bodyTV = bottomSheetView.findViewById(R.id.body_text_view);
        actionButtonTV = bottomSheetView.findViewById(R.id.action_button_text_view);

        bottomSheetView.findViewById(R.id.action_button_text_view).setOnClickListener(view -> dismiss());
    }

    public void setTitle(String title) {
        if (titleTV == null) {
            return;
        }
        titleTV.setText(Html.fromHtml(title));
    }

    public void setBody(String body) {
        if (bodyTV == null) {
            return;
        }

        bodyTV.setText(Html.fromHtml(body));
    }

    public void setButtonText(String text) {
        if (actionButtonTV == null) {
            return;
        }

        actionButtonTV.setText(Html.fromHtml(text));
    }
}
