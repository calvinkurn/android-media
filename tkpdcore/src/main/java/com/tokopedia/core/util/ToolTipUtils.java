package com.tokopedia.core.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.tokopedia.core2.R;

/**
 * Created by ricoharisin on 8/4/15.
 */
public class ToolTipUtils {

    private Context context;
    private View contentView;
    private View anchorView;
    private LayoutInflater inflater;

    public ToolTipUtils(Context context, View view) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.anchorView = view;
    }

    public void setLayout(int resID) {
        contentView = inflater.inflate(resID, null);
    }

    public View getView() {
        return contentView;
    }

    public void showToolTip() {
        final PopupWindow pw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setFocusable(false);
        pw.showAsDropDown(anchorView);
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pw.dismiss();
                return true;
            }
        });

    }

    public static void injectToolTip(Context activity, View view) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        final PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.default_tooltip, null), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setFocusable(false);
        pw.showAsDropDown(view);
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pw.dismiss();
                return true;
            }
        });

    }

    public interface ToolTipListener {
        void setView(View view);
        void setListener();
    }

    public static View setToolTip(Context context, int  layout, ToolTipListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);
        listener.setView(view);
        listener.setListener();
        return view;
    }

    public static void showToolTip(View view, View anchorView) {
        final PopupWindow pw = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setFocusable(false);
        pw.showAsDropDown(anchorView);
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                pw.dismiss();
                return true;
            }
        });
    }
}
