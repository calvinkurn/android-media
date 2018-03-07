package com.tkpd.library.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;

import com.tokopedia.design.component.EditTextCompat;

/**
 * Created by stevenfredian on 10/18/16.
 */

public class MaterialSpinner<T> extends EditTextCompat {
    CharSequence mHint;

    OnItemSelectedListener<T> onItemSelectedListener;
    ListAdapter mSpinnerAdapter;
    View layout;

    public MaterialSpinner(Context context) {
        super(context);

        mHint = getHint();
    }

    public MaterialSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        mHint = getHint();
    }

    public MaterialSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mHint = getHint();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setFocusable(false);
        setClickable(true);
    }

    public void setAdapter(ListAdapter adapter) {
        mSpinnerAdapter = adapter;
        configureOnClickListener();
    }


    public void setViewSpinner(MaterialSpinner<T> spinner) {
        layout = spinner;
        configureOnClickListener();
    }

    private void configureOnClickListener() {

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.setTitle(mHint);
//                builder.setAdapter(mSpinnerAdapter, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int selectedIndex) {
//                        if (onItemSelectedListener != null) {
//                            onItemSelectedListener.onItemSelectedListener((T)mSpinnerAdapter.getItem(selectedIndex), selectedIndex);
//                        }
//                    }
//                });
//                builder.setPositiveButton(android.R.string.cancel, null);
//                builder.create().show();

            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener<T> onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener<T> {
        void onItemSelectedListener(T item, int selectedIndex);
    }
}
