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

import com.bumptech.glide.signature.StringSignature;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;
import com.tokopedia.gamification.data.entity.TokenDataEntity;

import static android.view.Gravity.CENTER_HORIZONTAL;

/**
 * Created by nabillasabbaha on 4/3/18.
 */

public class CrackEmptyTokenFragment extends BaseDaggerFragment {

    private static final String TOKEN_DATA_EXTRA = "token_data";

    private ImageView tokenEmptyImage;
    private Button getMoreTokenBtn;
    private View rootView;
    private TokenDataEntity tokenData;
    private TextView title;
    private ImageView ivContainer;

    public static Fragment newInstance(TokenDataEntity tokenData) {
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
        tokenEmptyImage = rootView.findViewById(R.id.empty_lucky_egg);
        getMoreTokenBtn = rootView.findViewById(R.id.get_more_token_button);
        ivContainer = rootView.findViewById(R.id.iv_container);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tokenData = getArguments().getParcelable(TOKEN_DATA_EXTRA);

        title.setText(tokenData.getHome().getEmptyState().getTitle());
        getMoreTokenBtn.setText(tokenData.getHome().getEmptyState().getButtonText());

        ImageHandler.loadImageWithSignature(ivContainer, tokenData.getHome().getEmptyState().getBackgroundImgUrl(),
                new StringSignature(String.valueOf(tokenData.getHome().getEmptyState().getVersion())));

        ImageHandler.loadImageWithSignature(tokenEmptyImage, tokenData.getHome().getEmptyState().getImageUrl(),
                new StringSignature(String.valueOf(tokenData.getHome().getEmptyState().getVersion())));

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
                if (getActivity() != null && getActivity().getApplication() instanceof AbstractionRouter) {
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
                        tokenData.getHome().getEmptyState().getButtonApplink(),
                        tokenData.getHome().getEmptyState().getButtonURL(),
                        CrackTokenActivity.class);
            }
        });
    }

    private void setPercentageTokenImage() {
        int rootWidth = rootView.getWidth();
        int rootHeight = rootView.getHeight();
        int imageWidth = TokenMarginUtil.getEggWidth(rootWidth, rootHeight);
        int imageHeight = imageWidth;
        int imageMarginBottom = TokenMarginUtil.getEggMarginBottom(rootHeight);
        int imageMarginTop = imageMarginBottom - imageHeight;

        FrameLayout.LayoutParams ivFullLp = (FrameLayout.LayoutParams) tokenEmptyImage.getLayoutParams();
        ivFullLp.width = imageWidth;
        ivFullLp.height = imageHeight;
        ivFullLp.gravity = CENTER_HORIZONTAL;
        ivFullLp.topMargin = imageMarginTop;
        tokenEmptyImage.requestLayout();

        int dimen = 0;
        if (getActivity() != null) {
            dimen = getActivity().getResources().getDimensionPixelOffset(R.dimen.dp_112);
        }

        int titleMarginTop = imageMarginTop - dimen;
        FrameLayout.LayoutParams titleLp = (FrameLayout.LayoutParams) title.getLayoutParams();
        ivFullLp.gravity = CENTER_HORIZONTAL;
        titleLp.topMargin = titleMarginTop;
        title.requestLayout();
        title.setVisibility(View.VISIBLE);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
