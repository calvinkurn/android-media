package com.tokopedia.gamification.cracktoken.fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.signature.ObjectKey;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.gamification.GamificationEventTracking;
import com.tokopedia.gamification.R;
import com.tokopedia.gamification.applink.ApplinkUtil;
import com.tokopedia.gamification.cracktoken.activity.CrackTokenActivity;
import com.tokopedia.gamification.cracktoken.compoundview.WidgetRewardCrackResult;
import com.tokopedia.gamification.cracktoken.contract.CrackEmptyTokenContract;
import com.tokopedia.gamification.cracktoken.presenter.CrackEmptyTokenPresenter;
import com.tokopedia.gamification.cracktoken.util.TokenMarginUtil;
import com.tokopedia.gamification.data.entity.HomeSmallButton;
import com.tokopedia.gamification.data.entity.TokenDataEntity;
import com.tokopedia.gamification.di.GamificationComponent;
import com.tokopedia.gamification.di.GamificationComponentInstance;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import javax.inject.Inject;

import static android.view.Gravity.CENTER_HORIZONTAL;


/**
 * Created by nabillasabbaha on 4/3/18.
 */

public class CrackEmptyTokenFragment extends BaseDaggerFragment implements CrackEmptyTokenContract.View {

    private static final String TOKEN_DATA_EXTRA = "token_data";

    private ImageView tokenEmptyImage;
    private Button getMoreTokenBtn;
    private View rootView;
    private TokenDataEntity tokenData;
    private TextView title;
    private ImageView ivContainer;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private WidgetRewardCrackResult widgetRewards;
    @Inject
    CrackEmptyTokenPresenter crackEmptyTokenPresenter;
    private FrameLayout dailyPrizeLayout;
    private ImageView ivDailyPrize;


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
        toolbar = rootView.findViewById(R.id.toolbar);
        toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setText(getString(R.string.toko_points_title));
        widgetRewards = rootView.findViewById(R.id.widget_rewards);
        dailyPrizeLayout = rootView.findViewById(R.id.fl_daily_prize);
        ivDailyPrize = rootView.findViewById(R.id.empty_daily_prize);
        setUpToolBar();
        return rootView;
    }

    private void setUpToolBar() {
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        setDrawableColorFilter(toolbar.getNavigationIcon(), ContextCompat.getColor(getActivity(), R.color.black));
    }

    private void setDrawableColorFilter(Drawable drawable, int color) {
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        crackEmptyTokenPresenter.getRewardsCount();
        if (getArguments() != null)
            tokenData = getArguments().getParcelable(TOKEN_DATA_EXTRA);
        if (tokenData == null)
            return;

        HomeSmallButton homeSmallButton = tokenData.getHome().getHomeSmallButton();
        if (homeSmallButton != null && !TextUtils.isEmpty(homeSmallButton.getImageURL())) {
            dailyPrizeLayout.setVisibility(View.VISIBLE);
            ImageHandler.loadImageAndCache(ivDailyPrize, homeSmallButton.getImageURL());
            ivDailyPrize.setOnClickListener(v -> {
                String applink = homeSmallButton.getAppLink();
                if (!TextUtils.isEmpty(applink)) {
                    boolean isSupported = RouteManager.route(getActivity(), applink);
                    if (!isSupported) {
                        ApplinkUtil.navigateToAssociatedPage(getActivity(), homeSmallButton.getAppLink(), homeSmallButton.getUrl(), CrackTokenActivity.class);
                    }
                } else {
                    ApplinkUtil.navigateToAssociatedPage(getActivity(), homeSmallButton.getAppLink(), homeSmallButton.getUrl(), CrackTokenActivity.class);
                }

            });
        }
        if (!TextUtils.isEmpty(tokenData.getHome().getEmptyState().getTitle())) {
            title.setVisibility(View.VISIBLE);
            title.setText(tokenData.getHome().getEmptyState().getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        getMoreTokenBtn.setText(tokenData.getHome().getEmptyState().getButtonText());

        String backgroundUrl = tokenData.getHome().getEmptyState().getBackgroundImgUrl();
        String imageUrl = tokenData.getHome().getEmptyState().getImageUrl();
        ObjectKey signature = new ObjectKey(String.valueOf(tokenData.getHome().getEmptyState().getVersion()));

        ImageHandler.loadImageWithSignature(ivContainer, backgroundUrl, signature);
        ImageHandler.loadImageWithSignature(tokenEmptyImage, imageUrl, signature);

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
                TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                        GamificationEventTracking.Event.CLICK_LUCKY_EGG,
                        GamificationEventTracking.Category.EMPTY_PAGE,
                        GamificationEventTracking.Action.CLICK,
                        getMoreTokenBtn.getText().toString()
                ));

                String applink = tokenData.getHome().getEmptyState().getButtonApplink();
                if (!TextUtils.isEmpty(applink)) {
                    boolean isSupported = RouteManager.route(getActivity(), applink);
                    if (!isSupported) {
                        navigateViaApplinkUtil();
                    }
                } else {
                    navigateViaApplinkUtil();
                }
            }
        });
    }

    private void navigateViaApplinkUtil() {
        ApplinkUtil.navigateToAssociatedPage(getActivity(),
                tokenData.getHome().getEmptyState().getButtonApplink(),
                tokenData.getHome().getEmptyState().getButtonURL(),
                CrackTokenActivity.class);

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getScreenHeightWithoutStatusBar() {
        int statusBarHeight = getStatusBarHeight();
        return getActivity().getResources().getDisplayMetrics().heightPixels - statusBarHeight;
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


    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        GamificationComponent gamificationComponent =
                GamificationComponentInstance.getComponent(getActivity().getApplication());
        gamificationComponent.inject(this);
        crackEmptyTokenPresenter.attachView(this);
    }

    @Override
    public void updateRewards(int points, int coupons, int loyalty) {
        widgetRewards.setRewards(points, coupons, loyalty);
    }
}
