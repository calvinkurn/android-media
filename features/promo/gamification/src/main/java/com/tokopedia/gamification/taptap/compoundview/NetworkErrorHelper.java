package com.tokopedia.gamification.taptap.compoundview;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.gamification.R;

public class NetworkErrorHelper {

    public interface ErrorButtonsListener {
        void onRetryClicked();

        void onHomeClick();

    }

    public static void showEmptyState(Context context, final View rootview, int errorImage, String errorText, final ErrorButtonsListener listener) {
        try {
            rootview.findViewById(R.id.main_retry).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            params.weight = 1.0f;
            View retryLoad = inflater.inflate(R.layout.widget_error_page_tap_tap, (ViewGroup) rootview);
            View retryButon = retryLoad.findViewById(R.id.button_retry);
            View homeButton = retryLoad.findViewById(R.id.button_home);
            ImageView imageError = retryLoad.findViewById(R.id.image_error);
            imageError.setImageResource(errorImage);
            TextView textError = retryLoad.findViewById(R.id.text_error);
            textError.setText(errorText);
            if (listener != null) {
                retryButon.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
                        listener.onRetryClicked();
                    }
                });
            }
            if (listener != null) {
                homeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        rootview.findViewById(R.id.main_retry).setVisibility(View.GONE);
                        listener.onHomeClick();
                    }
                });
            }
        }
    }



    public static void showErrorSnackBar(String text, Context context, View coordinatorLayout, boolean homeToast) {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = LayoutInflater.from(context);
        View snackView = inflater.inflate(R.layout.gf_tap_tap_custom_snackbar, null);
        TextView tvmsg = snackView.findViewById(R.id.tv_msg);
        if (homeToast) {
            snackView.findViewById(R.id.main_content).setBackgroundColor(context.getResources().getColor(R.color.red_toast_bg_color));
        }
        tvmsg.setText(text);

        TextView okbtn = snackView.findViewById(R.id.snack_ok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        layout.addView(snackView, 0);
        layout.setPadding(0, 0, 0, 0);
        snackbar.show();
    }
}
