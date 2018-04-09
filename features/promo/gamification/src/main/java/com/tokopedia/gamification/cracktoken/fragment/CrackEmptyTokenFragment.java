package com.tokopedia.gamification.cracktoken.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.floating.view.model.TokenData;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * Created by nabillasabbaha on 4/3/18.
 */

public class CrackEmptyTokenFragment extends BaseDaggerFragment {

    private static final String TOKEN_DATA_EXTRA = "token_data";

    private ImageView tokenEmptyImage;
    private Button getMoreTokenBtn;
    private View rootView;
    private TokenData tokenData;
    private TextView title;

    public static Fragment newInstance(TokenData tokenData) {
        Fragment fragment = new CrackEmptyTokenFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TOKEN_DATA_EXTRA, tokenData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_crack_empty_token, container, false);
        title = rootView.findViewById(R.id.text_info_page);
        tokenEmptyImage = rootView.findViewById(R.id.image_full);
        getMoreTokenBtn = rootView.findViewById(R.id.get_more_token_button);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenData = getArguments().getParcelable(TOKEN_DATA_EXTRA);

        title.setText(tokenData.getHome().getTokenEmptyState().getTitle());
        getMoreTokenBtn.setText(tokenData.getHome().getTokenEmptyState().getButtonText());

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setPercentageTokenImage();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        getMoreTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getApplication() instanceof AbstractionRouter) {
                    ((AbstractionRouter) getActivity().getApplication())
                            .getAnalyticTracker()
                            .sendEventTracking(
                                    GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                                    GamificationEventTracking.Category.EMPTY_PAGE,
                                    GamificationEventTracking.Action.CLICK,
                                    getMoreTokenBtn.getText().toString()
                            );
                }

                ApplinkUtil.navigateToAssociatedPage(getActivity(),
                        tokenData.getHome().getTokenEmptyState().getButtonApplink(),
                        tokenData.getHome().getTokenEmptyState().getButtonURL(),
                        CrackTokenActivity.class);
            }
        });
    }

    private void setPercentageTokenImage() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = (int) (0.5 * Math.min(rootWidth, rootHeight));
        int imageHeight = imageWidth;
        int imageMarginTop = (int) (0.64 * (rootHeight)) - imageHeight;

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) tokenEmptyImage.getLayoutParams();
        ivFullLp.width = imageWidth;
        ivFullLp.height = imageHeight;
        ivFullLp.gravity = CENTER_HORIZONTAL;
        ivFullLp.topMargin = imageMarginTop;
        tokenEmptyImage.requestLayout();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
