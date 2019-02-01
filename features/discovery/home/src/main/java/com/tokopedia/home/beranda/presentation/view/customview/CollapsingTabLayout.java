package com.tokopedia.home.beranda.presentation.view.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.home.R;

import java.util.ArrayList;
import java.util.List;

public class CollapsingTabLayout extends TabLayout {

    private static final long DEFAULT_ANIMATION_DURATION = 300;
    private static final long TAB_AUTO_SCROLL_DELAY_DURATION = 300;
    private static final int NONE = -1;
    private static final int MAX_TAB_COLLAPSE_SCROLL_RANGE = 200;

    private String tabTitles[] = new String[] { "For You", "Promo Akhir Tahun", "Populer Minggu Ini" };
    private int[] imageResId = { R.drawable.background_tab_feed_1, R.drawable.background_tab_feed_2, R.drawable.background_tab_feed_3 };

    private List<TabItemData> tabItemDataList = new ArrayList<>();

    private int tabMaxHeight;
    private int tabMinHeight;
    private int lastTabSelectedPosition = NONE;

    TabIndicatorAnimator tabIndicatorExpandAnimator;
    TabIndicatorAnimator tabIndicatorCollapseAnimator;
    ValueAnimator tabHeightCollapseAnimator;
    private int lastPageScrolledPosition = NONE;
    private float lastTabCollapseFraction = 0f;

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

    public void setup(ViewPager viewPager, List<TabItemData> tabItemDataList) {
        this.tabItemDataList.clear();
        this.tabItemDataList.addAll(tabItemDataList);
        initResources();
        initAnimator();
        setSmoothScrollingEnabled(true);
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
            tab.setCustomView(getTabView(getContext(), i));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                startTabHeightExpandAnimation();

                if (position != lastPageScrolledPosition) {

                    tabIndicatorCollapseAnimator.setCurrentFraction(1);
                    tabIndicatorExpandAnimator.setCurrentFraction(0);

                    tabIndicatorCollapseAnimator.setTabIndicator(
                            (ExpandingLineView) getTabAt(position)
                                    .getCustomView().findViewById(R.id.tabIndicator)
                    );

                    if (position + 1 < getTabCount()) {
                        tabIndicatorExpandAnimator.setTabIndicator(
                                (ExpandingLineView) getTabAt(position + 1)
                                        .getCustomView().findViewById(R.id.tabIndicator)
                        );
                    } else {
                        tabIndicatorExpandAnimator.setTabIndicator(null);
                    }
                }

                tabIndicatorCollapseAnimator.setCurrentFraction(positionOffset);
                tabIndicatorExpandAnimator.setCurrentFraction(positionOffset);

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

    private void initResources() {
        tabMaxHeight = getResources().getDimensionPixelSize(R.dimen.tab_home_feed_max_height);
        tabMinHeight = getResources().getDimensionPixelSize(R.dimen.tab_home_feed_min_height);
    }

    private void initAnimator() {
        tabIndicatorExpandAnimator = new TabIndicatorAnimator(true, DEFAULT_ANIMATION_DURATION);
        tabIndicatorCollapseAnimator = new TabIndicatorAnimator(false, DEFAULT_ANIMATION_DURATION);

        tabHeightCollapseAnimator = ValueAnimator.ofInt(tabMaxHeight, tabMinHeight);
        tabHeightCollapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.d("tess", Float.toString(valueAnimator.getAnimatedFraction()));
                if (isFullyCollapsed(valueAnimator.getAnimatedFraction())) {
                    updateAllTabTextToSingleLine();
                } else {
                    updateAllTabTextToDoubleLines();
                }
                adjustTabLayoutHeight((int) valueAnimator.getAnimatedValue());
            }
        });
        tabHeightCollapseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                lastTabCollapseFraction = tabHeightCollapseAnimator.getAnimatedFraction();
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
        }
    }

    private void startTabHeightCollapseAnimation() {
        if (tabHeightCollapseAnimator.getAnimatedFraction() < 1 && !tabHeightCollapseAnimator.isStarted()) {
            tabHeightCollapseAnimator.start();
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

    public void scrollActiveTabToLeftScreen() {
        setScrollEnabled(true);
        int scrollTargetX = ((ViewGroup) getChildAt(0)).getChildAt(getSelectedTabPosition()).getLeft();
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

        if(lastTabSelectedPosition != NONE) {
            getTabAt(lastTabSelectedPosition)
                    .getCustomView().findViewById(R.id.tabIndicator).setVisibility(View.VISIBLE);
        }

        getTabAt(currentSelectedPosition)
                .getCustomView().findViewById(R.id.tabIndicator).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showAllTabIndicator();
            }
        }, TAB_AUTO_SCROLL_DELAY_DURATION);

        lastTabSelectedPosition = currentSelectedPosition;
    }

    private void hideAllTabIndicator() {
        for (int i = 0; i < getTabCount(); i++) {
            getTabAt(i)
                    .getCustomView().findViewById(R.id.tabIndicator).setVisibility(View.INVISIBLE);
        }
    }

    private void showAllTabIndicator() {
        for (int i = 0; i < getTabCount(); i++) {
            getTabAt(i)
                    .getCustomView().findViewById(R.id.tabIndicator).setVisibility(View.VISIBLE);
        }
    }

    private void updateAllTabTextToDoubleLines() {
        for (int i = 0; i < getTabCount(); i++) {
            TabLayout.Tab tab = getTabAt(i);
            ((TextView) tab.getCustomView().findViewById(R.id.tabTitle)).setMaxLines(2);
        }
    }

    private void updateAllTabTextToSingleLine() {
        for (int i = 0; i < getTabCount(); i++) {
            TabLayout.Tab tab = getTabAt(i);
            ((TextView) tab.getCustomView().findViewById(R.id.tabTitle)).setMaxLines(1);
        }
    }

    public void adjustTabCollapseOnScrolled(int dy, int totalScrollY) {

        if (dy < 0 && totalScrollY > MAX_TAB_COLLAPSE_SCROLL_RANGE) {
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
        tabHeightCollapseAnimator.setCurrentFraction(tabCollapseFraction);
        lastTabCollapseFraction = tabCollapseFraction;
    }

    private View getTabView(Context context, int position) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.tab_home_feed_layout, null);
        TextView textView = (TextView) rootView.findViewById(R.id.tabTitle);
        textView.setText(tabItemDataList.get(position).getTitle());
        ImageView imageView = (ImageView) rootView.findViewById(R.id.tabBackgroundImage);
        imageView.setImageResource(imageResId[position % 3]);
        int dp16 = rootView.getResources().getDimensionPixelSize(R.dimen.dp_16);
        if (position == 0) {
            rootView.setPadding(dp16, 0, 0, 0);
        } else if (position == getTabCount() - 1) {
            rootView.setPadding(0, 0, dp16, 0);
        } else {
            rootView.setPadding(0, 0, 0, 0);
        }
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

        public TabItemData(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}

