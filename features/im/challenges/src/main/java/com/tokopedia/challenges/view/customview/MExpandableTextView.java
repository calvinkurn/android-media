package com.tokopedia.challenges.view.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.challenges.R;

import java.lang.reflect.Field;

/**
 * @author lalit.singh
 */
public class MExpandableTextView extends FrameLayout {

    private static final String mEllipsis = "\u2026";
    private static final String MAXIMUM_VAR_NAME = "mMaximum";
    private TextView mTxtDescription;
    private OnExpandListener listener;

    private boolean mIsExpanded;
    private Integer mMaxLine = 2;
    private CharSequence mOriginalText = "";
    private ViewTreeObserver mViewTreeObserver;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;

    public MExpandableTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public Boolean isExpanded() {
        return mIsExpanded;
    }

    public void collapse() {

        if (listener != null) {
            listener.onCollapsed();
        }

        mIsExpanded = false;
        mTxtDescription.setMaxLines(mMaxLine);
        postInvalidate();
    }

    public void expand() {
        if (listener != null) {
            listener.onExpand();
        }

        mIsExpanded = true;
        if (mMaxLine == null) {
            storeMaxLine();
        }
        mTxtDescription.setEllipsize(null);
        mTxtDescription.setMaxLines(Integer.MAX_VALUE);
        mTxtDescription.setText(mOriginalText);
        postInvalidate();
    }

    public void setText(CharSequence text) {
        mTxtDescription.setText(text);
        if (TextUtils.isEmpty(text)) {
            mTxtDescription.setVisibility(View.GONE);
            mOriginalText = "";
        } else {
            mTxtDescription.setMaxLines(mMaxLine);
            mIsExpanded = false;
            mOriginalText = text;
        }
        mTxtDescription.setText(mOriginalText);
        mTxtDescription.requestLayout();
        mTxtDescription.setOnClickListener(null);
        requestLayout();
        layoutHelper();
    }

    public void setImgOnClickListener() {
        mTxtDescription.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void showMore(boolean expand) {
        if (expand) {
            mTxtDescription.append(Html.fromHtml("<font color='#42b549'>Lebih</font>"));
        } else {
            mTxtDescription.append(Html.fromHtml("<font color='#42b549'> </font>"));
        }

    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MExpandableTextView, defStyle, 0);
        int maxLines = 2;
        try {
            maxLines = a.getInt(R.styleable.MExpandableTextView_etw_maxLines, maxLines);
        } finally {
            a.recycle();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_expandable_text, this);
        mTxtDescription = (TextView) view.findViewById(R.id.txt_description);
        mTxtDescription.setMaxLines(maxLines);
        if (mViewTreeObserver == null) {
            mViewTreeObserver = this.mTxtDescription.getViewTreeObserver();
            mViewTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layoutHelper();
                }
            });
        }
        expand();
        collapse();
    }

    private void layoutHelper() {
        if (mTxtDescription.getLineCount() > mMaxLine && !isExpanded()) {
            int lineEndIndex = mTxtDescription.getLayout().getLineEnd(mMaxLine - 1);
            String text = mTxtDescription.getText().subSequence(0, lineEndIndex - 3) + mEllipsis;
            mTxtDescription.setText(text);
            showMore(true);
            setImgOnClickListener();
        }
        if (mTxtDescription.getLineCount() < mMaxLine) {
            mTxtDescription.setOnClickListener(null);
        }
    }

    public final void toggle() {
        if (isExpanded()) {
            showMore(true);
            collapse();
        } else {
            showMore(false);
            expand();
        }
    }

    private void storeMaxLine() {
        Field f;
        try {
            f = mTxtDescription.getClass().getDeclaredField(MAXIMUM_VAR_NAME);
            f.setAccessible(true);
            mMaxLine = f.getInt(mTxtDescription);
            f.setAccessible(false);
        } catch (SecurityException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

    public TextView getTxtDescription() {
        return mTxtDescription;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mTxtDescription != null) {
            ViewTreeObserver obs = mTxtDescription.getViewTreeObserver();
            if (mGlobalLayoutListener != null) {
                obs.removeGlobalOnLayoutListener(mGlobalLayoutListener);
            }
        }
        super.onDetachedFromWindow();
    }

    public void setListener(OnExpandListener onExpandListener) {
        this.listener = onExpandListener;
    }

    public interface OnExpandListener {
        void onExpand();

        void onCollapsed();
    }
}