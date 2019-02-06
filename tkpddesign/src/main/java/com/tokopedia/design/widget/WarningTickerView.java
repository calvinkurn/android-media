package com.tokopedia.design.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * @author by nisie on 14/11/18.
 */
public class WarningTickerView extends FrameLayout {

    public interface LinkClickListener {
        void onClickLink();
    }

    private TextView tvDescription;

    public WarningTickerView(@NonNull Context context) {
        super(context);
        init();
    }

    public WarningTickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WarningTickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_verification_warning,
                this, false);
        tvDescription = (TextView) view.findViewById(R.id.tv_description);
        this.addView(view);
    }

    public void setDescription(String descriptionString) {
        if (!TextUtils.isEmpty(descriptionString)) {
            tvDescription.setText(fromHtml(descriptionString));
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
    }

    public void setDescriptionWithLink(String descriptionString, String highlightText,
                                       LinkClickListener listener) {
        if (!TextUtils.isEmpty(descriptionString) && !TextUtils.isEmpty(highlightText)) {
            tvDescription.setText(getSpannableText(descriptionString, highlightText, listener));
            tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
            tvDescription.setVisibility(View.VISIBLE);
        } else {
            tvDescription.setVisibility(View.GONE);
        }
    }

    public SpannableString getSpannableText(String fullText, String highlightedText,
                                            LinkClickListener
                                                    listener) {
        final int color = getContext().getResources().getColor(R.color.green_500);
        int startIndex = fullText.indexOf(highlightedText);
        int stopIndex = fullText.length();
        SpannableString description = new SpannableString(fullText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                listener.onClickLink();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };
        description.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return description;
    }

    private Spanned fromHtml(String text) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableStringBuilder("");
        }
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }


    public void setJustificationMode(int justificationMode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            tvDescription.setJustificationMode(justificationMode);
        }
    }
}
