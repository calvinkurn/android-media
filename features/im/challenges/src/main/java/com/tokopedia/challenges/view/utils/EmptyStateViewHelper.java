package com.tokopedia.challenges.view.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.challenges.R;

/**
 * Created by Ashwani Tyagi on 13/09/18.
 */

public class EmptyStateViewHelper {

    public static void showEmptyState(Context context, final View rootview,
                                      @Nullable String titleMessage,
                                      @Nullable String subTitleMessage,
                                      @Nullable String titleRetry,
                                      @DrawableRes int iconRes,
                                      @Nullable final EmptyStateViewHelper.RetryClickedListener listener) {
        try {

            if (rootview == null) {
                return;
            }
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.gravity = Gravity.CENTER;
            params.weight = 1.0f;
            View retryLoad = rootview.findViewById(R.id.empty_view);
            if (retryLoad == null) {
                retryLoad = inflater.inflate(R.layout.empty_view, (ViewGroup) rootview);
            } else {
                retryLoad.setVisibility(View.VISIBLE);
            }
            TextView retryButon = retryLoad.findViewById(R.id.tv_button);
            TextView tvTitleMessage = retryLoad.findViewById(R.id.tv_error_title);
            TextView tvSubTitleMessage = retryLoad.findViewById(R.id.tv_error_desc);
            ImageView ivIcon = retryLoad.findViewById(R.id.img_error);
            if (subTitleMessage != null) tvSubTitleMessage.setText(subTitleMessage);
            if (titleMessage != null) tvTitleMessage.setText(titleMessage);
            if (titleRetry != null && !TextUtils.isEmpty(titleRetry)) {
                retryButon.setText(titleRetry);
            } else {
                retryButon.setVisibility(View.GONE);
            }
            if (iconRes != 0) {
                //noinspection deprecation
                ivIcon.setImageDrawable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ?
                        context.getDrawable(iconRes) : context.getResources().getDrawable(iconRes));
            }
            if (listener != null) {
                retryButon.setOnClickListener(v -> {
                    rootview.findViewById(R.id.empty_view);
                    listener.onRetryClicked();
                });
            }
        } catch (Exception e) {

        }
    }

    public static void hideEmptyState(final View rootview) {
        try {
            if (rootview != null && rootview.findViewById(R.id.empty_view) != null) {
                rootview.findViewById(R.id.empty_view).setVisibility(View.GONE);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public interface RetryClickedListener {
        void onRetryClicked();
    }

}
