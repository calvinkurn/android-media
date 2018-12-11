package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by nathan on 04/05/17.
 */

public class SearchInputView extends BaseCustomView {

    private static final long DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.SECONDS.toMillis(0);

    public interface Listener {

        void onSearchSubmitted(String text);

        void onSearchTextChanged(String text);

    }

    public interface ResetListener {
        void onSearchReset();
    }

    private ImageView searchImageView;
    private EditText searchTextView;
    private ImageButton closeImageButton;

    private Drawable searchDrawable;
    private String searchText;
    private String searchHint;

    private long delayTextChanged;
    private Listener listener;
    private ResetListener reset;

    public EditText getSearchTextView() {
        return searchTextView;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setResetListener(ResetListener listener) {
        this.reset = listener;
    }

    public SearchInputView(Context context) {
        super(context);
        init();
    }

    public SearchInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SearchInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SearchInputView);
        try {
            searchDrawable = styledAttributes.getDrawable(R.styleable.SearchInputView_siv_search_icon);
            searchText = styledAttributes.getString(R.styleable.SearchInputView_siv_search_text);
            searchHint = styledAttributes.getString(R.styleable.SearchInputView_siv_search_hint);
        } finally {
            styledAttributes.recycle();
        }
    }

    private void init() {
        View view = inflate(getContext(), getLayout(), this);
        searchImageView = (ImageView) view.findViewById(R.id.image_view_search);
        searchTextView = (EditText) view.findViewById(R.id.edit_text_search);
        closeImageButton = (ImageButton) view.findViewById(R.id.image_button_close);
        delayTextChanged = DEFAULT_DELAY_TEXT_CHANGED;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (searchDrawable != null) {
            searchImageView.setImageDrawable(searchDrawable);
        }
        if (!TextUtils.isEmpty(searchText)) {
            searchTextView.setText(searchText);
        }
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView.setHint(searchHint);
        }
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && listener != null) {
                    listener.onSearchSubmitted(textView.getText().toString());
                    return true;
                }
                return false;
            }
        });
        searchTextView.addTextChangedListener(getSearchTextWatcher());
        closeImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reset != null) {
                    reset.onSearchReset();
                }
                searchTextView.setText("");
            }
        });
        invalidate();
        requestLayout();
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
        searchTextView.setText(searchText);
    }
  
    public ImageView getSearchImageView() {
        return searchImageView;
    }

    public void setSearchImageView(ImageView searchImageView) {
        this.searchImageView = searchImageView;
    }

    public void setSearchTextView(EditText searchTextView) {
        this.searchTextView = searchTextView;
    }

    public ImageButton getCloseImageButton() {
        return closeImageButton;
    }

    public void setCloseImageButton(ImageButton closeImageButton) {
        this.closeImageButton = closeImageButton;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
        if (!TextUtils.isEmpty(searchHint)) {
            searchTextView.setHint(searchHint);
        }
    }

    public void setDelayTextChanged(long delayTextChanged) {
        this.delayTextChanged = delayTextChanged;
    }

    @Override
    public void setEnabled(boolean enabled) {
        searchTextView.setEnabled(enabled);
        closeImageButton.setEnabled(enabled);
    }

    public String getSearchText() {
        return searchTextView.getText().toString();
    }

    protected int getLayout() {
        return R.layout.widget_search_input_view;
    }

    protected TextWatcher getSearchTextWatcher() {
        return new TextWatcher() {
            private Timer timer = new Timer();

            public void afterTextChanged(Editable s) {
                runTimer(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
                if (TextUtils.isEmpty(searchTextView.getText().toString())) {
                    closeImageButton.setVisibility(View.GONE);
                } else {
                    closeImageButton.setVisibility(View.VISIBLE);
                }
            }

            private void runTimer(final String text) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        updateListener(text);
                    }
                }, delayTextChanged);
            }

            private void updateListener(final String text) {
                if (listener == null) {
                    return;
                }
                Handler mainHandler = new Handler(searchTextView.getContext().getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        listener.onSearchTextChanged(text);
                    }
                };
                mainHandler.post(myRunnable);
            }
        };
    }

}