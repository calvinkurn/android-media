package com.tkpd.library.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Tkpd_Eka on 4/10/2015.
 */
public class CustomSearchView extends EditText{

    public interface OnQuerySendListener{
        void onQuerySend(String query);
    }

    private OnQuerySendListener listener;

    public CustomSearchView(Context context) {
        super(context);
        initialize();
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        setSingleLine(true);
        setMaxLines(1);
        setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(android.R.drawable.ic_menu_search), null);
    }

    public void setOnQuerySendListener(final OnQuerySendListener listener){
        this.listener = listener;
        setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (isSendingQuery(actionId)) {
                    listener.onQuerySend(textView.getText().toString());

                    DropKeyboard(getContext(), CustomSearchView.this);
                }

                return false;
            }
        });
    }

    private boolean isSendingQuery(int actionId){
        return (actionId == EditorInfo.IME_ACTION_SEARCH);
    }

    public String getQuery(){
        return getText().toString();
    }

    private void DropKeyboard(Context context, EditText view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
