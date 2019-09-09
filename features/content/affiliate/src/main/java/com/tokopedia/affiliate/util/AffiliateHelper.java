package com.tokopedia.affiliate.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.affiliate.R;

/**
 * @author by yfsx on 25/01/19.
 */
public class AffiliateHelper {
    private static final String KEY_AFFILIATE = "KEY_AFFILIATE";
    private static final String KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME = "KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME";

    public static boolean isFirstTimeOpenProfileFromExplore(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context.getApplicationContext(), KEY_AFFILIATE);
        return cache.getBoolean(KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME, true);
    }

    public static void setFirstTimeOpenProfileFromExplore(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context.getApplicationContext(), KEY_AFFILIATE);
        cache.putBoolean(KEY_AFFILIATE_EXPLORE_OPEN_PROFILE_FIRSTTIME, false);
        cache.applyEditor();
    }

    public static void showEmptyState(
            Context context,
            final View rootview,
            @DrawableRes int imageRes,
            String title,
            String subtitle,
            String btnTitle,
            final NetworkErrorHelper.RetryClickedListener listener) {
        try {
            rootview.findViewById(R.id.cl_empty_layout).setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            if (context != null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (inflater != null) {
                    View retryLoad = inflater.inflate(R.layout.af_unify_retry, (ViewGroup) rootview);
                    TextView emptyTitle = retryLoad.findViewById(R.id.tv_empty_title);
                    TextView emptySubtitle = retryLoad.findViewById(R.id.tv_empty_subtitle);
                    View retryButton = retryLoad.findViewById(R.id.ll_empty_action);
                    TextView retryTitle = retryLoad.findViewById(R.id.tv_action_title);
                    ImageView image = retryLoad.findViewById(R.id.iv_empty_image);
                    emptyTitle.setText(title);
                    emptySubtitle.setText(subtitle);
                    image.setImageResource(imageRes);
                    retryTitle.setText(btnTitle);
                    if (listener != null) {
                        retryButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                hideEmptyState(rootview);
                                listener.onRetryClicked();
                            }
                        });
                    }
                }
            }
        }

    }

    public static void hideEmptyState(final View rootview) {
        try {
            rootview.findViewById(R.id.cl_empty_layout).setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
