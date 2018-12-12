package com.tokopedia.core.customView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core2.R;

/**
 * Custom Textview for Copyable feature
 * Created by kulomady 05 on 6/24/2016.
 */
public class TextViewCopyable extends TextView implements View.OnClickListener {

    public interface OnCopiedListener {
        void onCopied(String result);
    }

    private OnCopiedListener onCopiedListener;
    private String label;

    public TextViewCopyable(Context context) {
        super(context);
        initThisView();
    }

    public TextViewCopyable(Context context, AttributeSet attrs) {
        super(context, attrs);
        initThisView();
    }

    public TextViewCopyable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initThisView();
    }

    private void initThisView() {
        this.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        String text = this.getText().toString();
        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label != null && !label.isEmpty()
                        ? getContext().getString(R.string.label_copied)
                        : label,
                text);
        clipboardManager.setPrimaryClip(clip);
        if (clipboardManager.hasPrimaryClip()) {
            onCopiedListener.onCopied(text);
        }

    }


    public void setOnCopiedListener(OnCopiedListener onCopiedListener) {
        this.onCopiedListener = onCopiedListener;
        this.setOnClickListener(this);
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
