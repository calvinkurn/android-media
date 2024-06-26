package com.tokopedia.collapsing.tab.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.google.android.material.tabs.TabLayout;
import com.tokopedia.media.loader.JvmMediaLoader;
import com.tokopedia.media.loader.listener.MediaListener;
import com.tokopedia.media.loader.wrapper.MediaDataSource;
import com.tokopedia.unifycomponents.CardUnify2;

import java.util.ArrayList;
import java.util.List;

public class CollapsingTabLayout extends TabLayout {

    private static final long DEFAULT_ANIMATION_DURATION = 300;
    private static final long TAB_AUTO_SCROLL_DELAY_DURATION = 300;
    private static final int NONE = -1;
    private static final int MAX_TAB_COLLAPSE_SCROLL_RANGE = 200;
    private static final int SCROLL_UP_THRESHOLD_BEFORE_EXPAND = 500;
    private static final float TAB_CORNER_RADIUS = 10f;

    private List<TabItemData> tabItemDataList = new ArrayList<>();

    private int tabMaxHeight;
    private int tabMinHeight;
    private int leftmostItemPadding;
    private int lastTabSelectedPosition = NONE;

    TabIndicatorAnimator tabIndicatorExpandAnimator;
    TabIndicatorAnimator tabIndicatorCollapseAnimator;
    ValueAnimator tabHeightCollapseAnimator;
    private int lastPageScrolledPosition = NONE;
    private float lastTabCollapseFraction = 0f;
    private int totalScrollUp;

    private boolean scrollEnabled = false;

    public CollapsingTabLayout(Context context) {
        super(context);
    }

    public CollapsingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CollapsingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setup(ViewPager viewPager, List<TabItemData> tabItemDataList, boolean cardInteraction) {
        this.tabItemDataList.clear();
        this.tabItemDataList.addAll(tabItemDataList);
        resetAllState();
        initResources();
        initAnimator();
        setSmoothScrollingEnabled(true);
        clearOnTabSelectedListeners();
        viewPager.clearOnPageChangeListeners();
        setupWithViewPager(viewPager);
        addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                scrollActiveTabToLeftScreenDelayed();
                updateTabIndicatorState();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        for (int i = 0; i < getTabCount(); i++) {
            TabLayout.Tab tab = getTabAt(i);
            tab.setCustomView(getTabView(getContext(), i, tab, cardInteraction));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                startTabHeightExpandAnimation();

                if (position != lastPageScrolledPosition) {

                    setCurrentFraction(tabIndicatorCollapseAnimator, 1);
                    setCurrentFraction(tabIndicatorExpandAnimator, 0);

                    tabIndicatorCollapseAnimator.setTabIndicator(
                            (ExpandingLineView) findViewByIdFromTab(getTabAt(position), R.id.tabIndicator)
                    );

                    if (position + 1 < getTabCount()) {
                        tabIndicatorExpandAnimator.setTabIndicator(
                                (ExpandingLineView) findViewByIdFromTab(getTabAt(position + 1), R.id.tabIndicator)
                        );
                    } else {
                        tabIndicatorExpandAnimator.setTabIndicator(null);
                    }
                }

                setCurrentFraction(tabIndicatorCollapseAnimator, positionOffset);
                setCurrentFraction(tabIndicatorExpandAnimator, positionOffset);

                lastPageScrolledPosition = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollActiveTabToLeftScreen();
                }
            }
        });
    }

    private void resetAllState() {
        lastTabSelectedPosition = NONE;
        lastPageScrolledPosition = NONE;
        lastTabCollapseFraction = 0f;
        scrollEnabled = false;
        totalScrollUp = 0;
    }

    @SuppressLint("ResourcePackage")
    private void initResources() {
        tabMaxHeight = getResources().getDimensionPixelSize(R.dimen.tab_home_feed_max_height);
        tabMinHeight = getResources().getDimensionPixelSize(R.dimen.tab_home_feed_min_height);
        leftmostItemPadding = getResources().getDimensionPixelSize(com.tokopedia.abstraction.R.dimen.dp_16);
    }

    private void initAnimator() {
        tabIndicatorExpandAnimator = new TabIndicatorAnimator(true, DEFAULT_ANIMATION_DURATION);
        tabIndicatorCollapseAnimator = new TabIndicatorAnimator(false, DEFAULT_ANIMATION_DURATION);

        tabHeightCollapseAnimator = ValueAnimator.ofInt(tabMaxHeight, tabMinHeight);
        tabHeightCollapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isFullyCollapsed(valueAnimator.getAnimatedFraction())) {
                    updateAllTabTextToSingleLine();
                } else {
                    updateAllTabTextToDoubleLines();
                }
                adjustTabLayoutHeight((int) valueAnimator.getAnimatedValue());
            }
        });
        tabHeightCollapseAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        //tabHeightCollapseAnimator.setStartDelay(TAB_AUTO_SCROLL_DELAY_DURATION);
    }

    private boolean isFullyCollapsed(float fraction) {
        return fraction >= 1;
    }

    private void adjustTabLayoutHeight(int height) {
        if (getLayoutParams().height != height) {
            getLayoutParams().height = height;
            requestLayout();
        }
    }

    public void snapCollapsingTab() {
        if (tabHeightCollapseAnimator.getAnimatedFraction() < 0.5) {
            startTabHeightExpandAnimation();
        } else {
            startTabHeightCollapseAnimation();
        }
    }

    private void startTabHeightExpandAnimation() {
        if (tabHeightCollapseAnimator.getAnimatedFraction() > 0 && !tabHeightCollapseAnimator.isStarted()) {
            tabHeightCollapseAnimator.reverse();
            lastTabCollapseFraction = 0;
        }
    }

    private void startTabHeightCollapseAnimation() {
        if (tabHeightCollapseAnimator.getAnimatedFraction() < 1 && !tabHeightCollapseAnimator.isStarted()) {
            tabHeightCollapseAnimator.start();
            lastTabCollapseFraction = 1;
        }
    }

    private void scrollActiveTabToLeftScreenDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollActiveTabToLeftScreen();
            }
        }, TAB_AUTO_SCROLL_DELAY_DURATION);
    }

    @SuppressLint("Recycle")
    public void scrollActiveTabToLeftScreen() {
        if (getSelectedTabPosition() < 0) {
            return;
        }
        setScrollEnabled(true);
        int scrollTargetX = ((ViewGroup) getChildAt(0)).getChildAt(getSelectedTabPosition()).getLeft() - leftmostItemPadding;
        Animator animator = ObjectAnimator.ofInt(this, "scrollX",  scrollTargetX).setDuration(DEFAULT_ANIMATION_DURATION);
        animator.addListener(
                new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        setScrollEnabled(false);
                    }
                }
        );
        animator.start();
    }

    private void updateTabIndicatorState() {
        int currentSelectedPosition = getSelectedTabPosition();
        if (lastTabSelectedPosition == currentSelectedPosition) {
            return;
        }

        hideAllTabIndicator();

        View lastSelectedTabIndicator = findViewByIdFromTab(getTabAt(lastTabSelectedPosition), R.id.tabIndicator);
        if(lastTabSelectedPosition != NONE && lastSelectedTabIndicator != null) {
            lastSelectedTabIndicator.setVisibility(View.VISIBLE);
        }

        View currentSelectedTabIndicator = findViewByIdFromTab(getTabAt(currentSelectedPosition), R.id.tabIndicator);
        if (currentSelectedTabIndicator != null) {
            currentSelectedTabIndicator.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAllTabIndicator();
            }
        }, DEFAULT_ANIMATION_DURATION);

        lastTabSelectedPosition = currentSelectedPosition;
    }

    private void hideAllTabIndicator() {
        for (int i = 0; i < getTabCount(); i++) {
            View tabIndicator = findViewByIdFromTab(getTabAt(i), R.id.tabIndicator);
            if (tabIndicator != null) {
                tabIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showAllTabIndicator() {
        for (int i = 0; i < getTabCount(); i++) {
            View tabIndicator = findViewByIdFromTab(getTabAt(i), R.id.tabIndicator);
            if (tabIndicator != null) {
                tabIndicator.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateAllTabTextToDoubleLines() {
        for (int i = 0; i < getTabCount(); i++) {
            TextView tabTitle = (TextView) findViewByIdFromTab(getTabAt(i), R.id.tabTitle);
            if (tabTitle != null) {
                tabTitle.setMaxLines(2);
            }
        }
    }

    private void updateAllTabTextToSingleLine() {
        for (int i = 0; i < getTabCount(); i++) {
            TextView tabTitle = (TextView) findViewByIdFromTab(getTabAt(i), R.id.tabTitle);
            if (tabTitle != null) {
                tabTitle.setMaxLines(1);
            }
        }
    }

    public void adjustTabCollapseOnScrolled(int dy, int totalScrollY) {

        if (dy == 0) {
            return;
        }

        if (dy < 0) {
            totalScrollUp -= dy;
        } else {
            totalScrollUp = 0;
        }

        if (dy < 0 && totalScrollY > MAX_TAB_COLLAPSE_SCROLL_RANGE && totalScrollUp < SCROLL_UP_THRESHOLD_BEFORE_EXPAND) {
            return;
        }

        float updatedFraction = lastTabCollapseFraction + getTabCollapseDeltaFraction(dy);

        if (updatedFraction > 1) {
            adjustTabCollapseFraction(1);
        } else if (updatedFraction < 0) {
            adjustTabCollapseFraction(0);
        } else {
            adjustTabCollapseFraction(updatedFraction);
        }
    }

    private float getTabCollapseDeltaFraction(int dy) {
        return (float) dy / MAX_TAB_COLLAPSE_SCROLL_RANGE;
    }

    private void adjustTabCollapseFraction(float tabCollapseFraction) {
        if (tabHeightCollapseAnimator.isRunning()) {
            tabHeightCollapseAnimator.cancel();
        }
        setCurrentFraction(tabHeightCollapseAnimator, tabCollapseFraction);
        lastTabCollapseFraction = tabCollapseFraction;
    }

    private View getTabView(Context context, int position, Tab tab, boolean cardInteraction) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.tab_home_feed_layout, null);
        CardUnify2 card = rootView.findViewById(R.id.card_tab);
        if (cardInteraction) {
            card.setAnimateOnPress(CardUnify2.Companion.getANIMATE_OVERLAY_BOUNCE());
        } else {
            card.setAnimateOnPress(CardUnify2.Companion.getANIMATE_OVERLAY());
        }
        card.setRadius((int) (TAB_CORNER_RADIUS * Resources.getSystem().getDisplayMetrics().density));
        card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab(tab);
            }
        });
        TextView textView = (TextView) rootView.findViewById(R.id.tabTitle);
        textView.setText(tabItemDataList.get(position).getTitle());
        ImageView imageView = (ImageView) rootView.findViewById(R.id.tabBackgroundImage);
        View shimmeringView = (View) rootView.findViewById(R.id.tabShimmeringView);
        shimmeringView.setVisibility(View.VISIBLE);

        JvmMediaLoader.loadImage(imageView, tabItemDataList.get(position).getImageUrl(),
                properties -> null,
                (bitmap, mediaDataSource,  isFirstResource) -> {
                    // on loaded
                    shimmeringView.setVisibility(View.GONE);
                    return null;
                },
                (mediaException) -> {
                    // on failed
                    shimmeringView.setVisibility(View.VISIBLE);
                    return null;
                });
        return rootView;
    }

    private void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }

    @Override
    public void scrollTo(int x, int y) {
        if (scrollEnabled) {
            super.scrollTo(x, y);
        }
    }

    public void resetCollapseState() {
        if (hasSetupWithData()) {
            adjustTabCollapseFraction(0);
        }
    }

    private boolean hasSetupWithData() {
        return !tabItemDataList.isEmpty();
    }

    private void setCurrentFraction(ValueAnimator animator, float fraction) {
        animator.setCurrentPlayTime((long) (fraction * animator.getDuration()));
    }

    private View findViewByIdFromTab(Tab tab, int id) {
        if (tab != null && tab.getCustomView() != null) {
            return tab.getCustomView().findViewById(id);
        } else {
            return null;
        }
    }

    private static class TabIndicatorAnimator extends ValueAnimator {
        private ExpandingLineView tabIndicator;
        private boolean animateToExpand;
        private long durationMillis;

        public TabIndicatorAnimator(boolean animateToExpand, long durationMillis) {
            this.animateToExpand = animateToExpand;
            this.durationMillis = durationMillis;
            init();
        }

        private void init() {
            if (animateToExpand) {
                setFloatValues(0, 1);
            } else {
                setFloatValues(1, 0);
            }
            addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (tabIndicator != null) {
                        tabIndicator.setFraction((Float) valueAnimator.getAnimatedValue());
                    }
                }
            });
            setDuration(durationMillis);
        }

        public void setTabIndicator(ExpandingLineView tabIndicator) {
            this.tabIndicator = tabIndicator;
        }
    }

    public static class TabItemData {
        private String title;
        private String imageUrl;

        public TabItemData(String title, String imageUrl) {
            this.title = title;
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}

