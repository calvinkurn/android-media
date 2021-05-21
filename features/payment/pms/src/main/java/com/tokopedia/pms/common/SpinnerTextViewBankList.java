package com.tokopedia.pms.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;

/**
 * Created by zulfikarrahman on 7/9/18.
 */

public class SpinnerTextViewBankList extends SpinnerTextView {
    private ListenerOnClick listenerOnClick;

    public SpinnerTextViewBankList(Context context) {
        super(context);
    }

    public SpinnerTextViewBankList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerTextViewBankList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListenerOnClick(ListenerOnClick listenerOnClick){
        this.listenerOnClick = listenerOnClick;
    }

    @Override
    public void showDropDown(AutoCompleteTextView view) {
       listenerOnClick.onClickTextAutoComplete(view);
    }

    public interface ListenerOnClick {
        void onClickTextAutoComplete(View view);
    }
}
