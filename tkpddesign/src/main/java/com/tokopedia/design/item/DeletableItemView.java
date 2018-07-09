package com.tokopedia.design.item;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by henrypriyono on 8/18/17.
 */

public class DeletableItemView extends BaseCustomView {

    private View rootView;
    private TextView textView;
    private View buttonView;
    private ImageView imageView;

    private OnDeleteListener onDeleteListener;
    private OnTextClickListener onTextClickListener;

    @LayoutRes
    private int layoutRef = R.layout.widget_deletable_item_view;

    public DeletableItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DeletableItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DeletableItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.DeletableItemView);
        try {
            layoutRef = styledAttributes.getResourceId(R.styleable.DeletableItemView_layout, R.layout.widget_deletable_item_view);
        } finally {
            styledAttributes.recycle();
        }
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, layoutRef, this);
        textView = (TextView) rootView.findViewById(R.id.item_name);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTextClickListener != null) {
                    onTextClickListener.onClick();
                }
            }
        });
        imageView = (ImageView) rootView.findViewById(R.id.item_image);
        buttonView = rootView.findViewById(R.id.delete_button);
        buttonView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete();
                }
            }
        });
    }

    public void setItemName(String itemName) {
        if (imageView != null) {
            imageView.setVisibility(GONE);
        }
        if (textView != null) {
            textView.setText(itemName);
            textView.setVisibility(VISIBLE);
            textView.requestLayout();
        }
    }

    public void setItemDrawable(int resId) {
        if (textView != null) {
            textView.setVisibility(GONE);
        }
        if (imageView != null) {
            imageView.setImageResource(resId);
            imageView.setVisibility(VISIBLE);
            imageView.requestLayout();
        }
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        this.onTextClickListener = onTextClickListener;
    }

    public interface OnDeleteListener {
        void onDelete();
    }

    public interface OnTextClickListener {
        void onClick();
    }
}
