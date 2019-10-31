package com.tokopedia.saldodetails.design;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import androidx.annotation.NonNull;
import android.text.Html;
import android.widget.TextView;

import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.saldodetails.R;

public class UserStatusInfoBottomSheet extends BottomSheetView {

    public UserStatusInfoBottomSheet(@NonNull Context context) {
        super(context);
    }

    private TextView titleTV;
    private TextView bodyTV;
    private TextView actionButtonTV;

    @Override
    protected int getLayout() {
        return com.tokopedia.saldodetails.R.layout.user_info_bottom_sheet;
    }

    @Override
    protected void init(Context context) {
        if (context instanceof Activity) layoutInflater = ((Activity) context).getLayoutInflater();
        else layoutInflater = ((Activity) ((ContextWrapper) context)
                .getBaseContext()).getLayoutInflater();

        bottomSheetView = layoutInflater.inflate(getLayout(), null);
        setContentView(bottomSheetView);


        titleTV = bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.title_text_view);
        bodyTV = bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.body_text_view);
        actionButtonTV = bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.action_button_text_view);

        bottomSheetView.findViewById(com.tokopedia.saldodetails.R.id.action_button_text_view).setOnClickListener(view -> dismiss());
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
