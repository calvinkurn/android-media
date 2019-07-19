package com.tokopedia.contactus.inboxticket2.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.contactus.ContactUsModuleRouter;
import com.tokopedia.contactus.R;



public class ServicePrioritiesBottomSheet extends FrameLayout implements View.OnClickListener {

    public static final String LEARN_MORE_TEXT = "Pelajari Selengkapnya";
    private Context mContext;
    private final CloseServicePrioritiesBottomSheet closeServicePrioritiesBottomSheet;

    public ServicePrioritiesBottomSheet(@NonNull Context context, CloseServicePrioritiesBottomSheet closeServicePrioritiesBottomSheet) {
        super(context);
        mContext = context;
        this.closeServicePrioritiesBottomSheet = closeServicePrioritiesBottomSheet;
        init();
    }

    private void init() {

        View helpfullView = LayoutInflater.from(getContext()).inflate(R.layout.service_priorities_bottom_sheet_layout, this, true);
        TextView link  = helpfullView.findViewById(R.id.tv_service_priorities_officialstore_desc);
        TextView close = helpfullView.findViewById(R.id.txt_close);
        String text = mContext.getString(R.string.service_priorities_officialstore_desc);
        SpannableString spannableString = new SpannableString(text);
        int startIndexOfLink = text.indexOf(LEARN_MORE_TEXT);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(((ContactUsModuleRouter) (mContext.getApplicationContext()))
                        .getWebviewActivityWithIntent(mContext,
                                mContext.getString(R.string.learn_more_link)));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.green_250)); // specific color for this link
            }
        }, startIndexOfLink, startIndexOfLink + LEARN_MORE_TEXT.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        link.setHighlightColor(Color.TRANSPARENT);
        link.setMovementMethod(LinkMovementMethod.getInstance());

        link.setText(spannableString, TextView.BufferType.SPANNABLE);

        close.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        closeServicePrioritiesBottomSheet.onClickClose();
    }

    public interface CloseServicePrioritiesBottomSheet {
        void onClickClose();
    }
}
