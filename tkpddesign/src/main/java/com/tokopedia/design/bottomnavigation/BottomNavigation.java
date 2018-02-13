package com.tokopedia.design.bottomnavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by by yu on 2016/11/10
 * Modified by meyta on 1/23/18.
 */

public class BottomNavigation extends BottomNavigationView {

    private static String INIT_BACKSTACK = "0";

    private int mShiftAmount;
    private float mScaleUpFactor;
    private float mScaleDownFactor;
    private boolean animationRecord;
    private float mLargeLabelSize;
    private float mSmallLabelSize;
    private boolean visibilityTextSizeRecord;
    private boolean visibilityHeightRecord;
    private int mItemHeight;

    private ViewPager mViewPager;
    private MyOnNavigationItemSelectedListener mMyOnNavigationItemSelectedListener;
    private BottomNavigationViewExOnPageChangeListener mPageChangeListener;
    private ViewPagerPageChangeListener viewPagerPageChangeListener;
    private BottomNavigationMenuView mMenuView;
    private BottomNavigationItemView[] mButtons;

    private static boolean isNavigationItemClicking = false;

    private ArrayList<String> backStack = new ArrayList<>(); // handle backstack
    boolean isHandleBackStackEvent = true;

    public BottomNavigation(Context context) {
        super(context);
        setDefaultStyle();
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultStyle();
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaultStyle();
    }

    private void setDefaultStyle() {
        this.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bottomnav_bg));
        this.setItemIconTintList(ContextCompat.getColorStateList(getContext(), R.color.selector_bottomnav));
        this.setItemTextColor(ContextCompat.getColorStateList(getContext(), R.color.selector_bottomnav));
        this.enableAnimation(false);
        this.enableShiftingMode(false);
        this.enableItemShiftingMode(false);
    }

    public void setViewPagerPageChangeListener(ViewPagerPageChangeListener viewPagerPageChangeListener) {
        this.viewPagerPageChangeListener = viewPagerPageChangeListener;
    }

    public interface ViewPagerPageChangeListener {
        void onPageScrollStateChanged(final int state);
        void onPageScrolled(final int position, final float positionOffset,
                            final int positionOffsetPixels);
        void onPageSelected(final int position);
    }

    /**
     * change the visibility of icon
     *
     * @param visibility
     */
    @SuppressLint("RestrictedApi")
    public void setIconVisibility(boolean visibility) {
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        for (BottomNavigationItemView button : mButtons) {
            ImageView mIcon = getField(button.getClass(), button, "mIcon");
            assert mIcon != null;
            mIcon.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
        }

        if (!visibility) {
            if (!visibilityHeightRecord) {
                visibilityHeightRecord = true;
                mItemHeight = getItemHeight();
            }

            BottomNavigationItemView button = mButtons[0];
            if (null != button) {
                final ImageView mIcon = getField(button.getClass(), button, "mIcon");
                if (null != mIcon) {
                    mIcon.post(new Runnable() {
                        @Override
                        public void run() {
                            setItemHeight(mItemHeight - mIcon.getMeasuredHeight());
                        }
                    });
                }
            }
        } else {
            if (!visibilityHeightRecord)
                return;
            setItemHeight(mItemHeight);
        }

        mMenuView.updateMenuView();
    }

    /**
     * change the visibility of text
     *
     * @param visibility
     */
    @SuppressLint("RestrictedApi")
    public void setTextVisibility(boolean visibility) {
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();

        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");

            if (mLargeLabel == null)
                return;
            if (mSmallLabel == null)
                return;

            if (!visibility) {
                // if not record the font size, record it
                if (!visibilityTextSizeRecord && !animationRecord) {
                    visibilityTextSizeRecord = true;
                    mLargeLabelSize = mLargeLabel.getTextSize();
                    mSmallLabelSize = mSmallLabel.getTextSize();
                }

                // if not visitable, set font size to 0
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);
                mSmallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, 0);

            } else {
                // if not record the font size, we need do nothing.
                if (!visibilityTextSizeRecord)
                    break;

                // restore it
                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
                mSmallLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);
            }
        }

        if (!visibility) {
            // if not record mItemHeight
            if (!visibilityHeightRecord) {
                visibilityHeightRecord = true;
                mItemHeight = getItemHeight();
            }

            setItemHeight(mItemHeight - getFontHeight(mSmallLabelSize));

        } else {
            // if not record the mItemHeight, we need do nothing.
            if (!visibilityHeightRecord)
                return;
            // restore mItemHeight
            setItemHeight(mItemHeight);
        }

        mMenuView.updateMenuView();
    }

    /**
     * get text height by font size
     *
     * @param fontSize
     * @return
     */
    private static int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    /**
     * enable or disable click item animation(text scale and icon move animation in no item shifting mode)
     *
     * @param enable It means the text won't scale and icon won't move when active it in no item shifting mode if false.
     */
    @SuppressLint("RestrictedApi")
    public void enableAnimation(boolean enable) {

        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");

            // if disable animation, need animationRecord the source value
            if (!enable) {
                if (!animationRecord) {
                    animationRecord = true;
                    mShiftAmount = getField(button.getClass(), button, "mShiftAmount");
                    mScaleUpFactor = getField(button.getClass(), button, "mScaleUpFactor");
                    mScaleDownFactor = getField(button.getClass(), button, "mScaleDownFactor");

                    mLargeLabelSize = mLargeLabel.getTextSize();
                    mSmallLabelSize = mSmallLabel.getTextSize();

                }
                // disable
                setField(button.getClass(), button, "mShiftAmount", 0);
                setField(button.getClass(), button, "mScaleUpFactor", 1);
                setField(button.getClass(), button, "mScaleDownFactor", 1);

                // let the mLargeLabel font size equal to mSmallLabel
                if (mLargeLabel != null) {
                    mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);
                }

            } else {
                // haven't change the value. It means it was the first call this method. So nothing need to do.
                if (!animationRecord)
                    return;
                // enable animation
                setField(button.getClass(), button, "mShiftAmount", mShiftAmount);
                setField(button.getClass(), button, "mScaleUpFactor", mScaleUpFactor);
                setField(button.getClass(), button, "mScaleDownFactor", mScaleDownFactor);
                // restore
                if (mLargeLabel != null) {
                    mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
                }
            }
        }
        mMenuView.updateMenuView();
    }

    /**
     * enable the shifting mode for navigation
     *
     * @param enable It will has a shift animation if true. Otherwise all items are the same width.
     */
    @SuppressLint("RestrictedApi")
    public void enableShiftingMode(boolean enable) {
        /*
        1. get field in this class
        private final BottomNavigationMenuView mMenuView;
        2. change field mShiftingMode value in mMenuView
        private boolean mShiftingMode = true;
         */
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. change field mShiftingMode value in mMenuView
        setField(mMenuView.getClass(), mMenuView, "mShiftingMode", enable);

        mMenuView.updateMenuView();
    }

    /**
     * enable the shifting mode for each item
     *
     * @param enable It will has a shift animation for item if true. Otherwise the item text always be shown.
     */
    @SuppressLint("RestrictedApi")
    public void enableItemShiftingMode(boolean enable) {
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // 3. change field mShiftingMode value in mButtons
        for (BottomNavigationItemView button : mButtons) {
            setField(button.getClass(), button, "mShiftingMode", enable);
        }
        mMenuView.updateMenuView();
    }

    /**
     * get the current checked item position
     *
     * @return index of item, start from 0.
     */
    public int getCurrentItem() {
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        Menu menu = getMenu();
        for (int i = 0; i < mButtons.length; i++) {
            if (menu.getItem(i).isChecked()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * get menu item position in menu
     *
     * @param item
     * @return position if success, -1 otherwise
     */
    public int getMenuItemPosition(MenuItem item) {
        // get item id
        int itemId = item.getItemId();
        // get meunu
        Menu menu = getMenu();
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            if (menu.getItem(i).getItemId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * set the current checked item
     *
     * @param item start from 0.
     */
    public void setCurrentItem(int item) {
        // check bounds
        if (item < 0 || item >= getMaxItemCount()) {
            throw new ArrayIndexOutOfBoundsException("item is out of bounds, we expected 0 - "
                    + (getMaxItemCount() - 1) + ". Actually " + item);
        }
        // 1. get mMenuView
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get mButtons
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        // get mOnClickListener
        View.OnClickListener mOnClickListener = getField(mMenuView.getClass(), mMenuView, "mOnClickListener");

        if (mOnClickListener != null) {
            mOnClickListener.onClick(mButtons[item]);
        }

    }

    /**
     * get OnNavigationItemSelectedListener
     *
     * @return
     */
    public OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        // private OnNavigationItemSelectedListener mListener;
        return getField(BottomNavigationView.class, this, "mSelectedListener");
    }

    @Override
    public void setOnNavigationItemSelectedListener(@Nullable OnNavigationItemSelectedListener listener) {
        // if not set up with view pager, the same with father
        if (null == mMyOnNavigationItemSelectedListener) {
            super.setOnNavigationItemSelectedListener(listener);
            return;
        }

        mMyOnNavigationItemSelectedListener.setOnNavigationItemSelectedListener(listener);
    }

    /**
     * get private mMenuView
     *
     * @return
     */
    private BottomNavigationMenuView getBottomNavigationMenuView() {
        if (null == mMenuView)
            mMenuView = getField(BottomNavigationView.class, this, "mMenuView");
        return mMenuView;
    }

    /**
     * get private mButtons in mMenuView
     *
     * @return
     */
    public BottomNavigationItemView[] getBottomNavigationItemViews() {
        if (null != mButtons)
            return mButtons;
        /*
         * 1 private final BottomNavigationMenuView mMenuView;
         * 2 private BottomNavigationItemView[] mButtons;
         */
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons");
        return mButtons;
    }

    /**
     * get private mButton in mMenuView at position
     *
     * @param position
     * @return
     */
    public BottomNavigationItemView getBottomNavigationItemView(int position) {
        return getBottomNavigationItemViews()[position];
    }

    /**
     * get icon at position
     *
     * @param position
     * @return
     */
    public ImageView getIconAt(int position) {
        BottomNavigationItemView mButtons = getBottomNavigationItemView(position);
        return getField(BottomNavigationItemView.class, mButtons, "mIcon");
    }

    /**
     * get small label at position
     * Each item has tow label, one is large, another is small.
     *
     * @param position
     * @return
     */
    public TextView getSmallLabelAt(int position) {
        BottomNavigationItemView mButtons = getBottomNavigationItemView(position);
        return getField(BottomNavigationItemView.class, mButtons, "mSmallLabel");
    }

    /**
     * get large label at position
     * Each item has tow label, one is large, another is small.
     *
     * @param position
     * @return
     */
    public TextView getLargeLabelAt(int position) {
        BottomNavigationItemView mButtons = getBottomNavigationItemView(position);
        return getField(BottomNavigationItemView.class, mButtons, "mLargeLabel");
    }

    /**
     * return item count
     *
     * @return
     */
    public int getItemCount() {
        BottomNavigationItemView[] bottomNavigationItemViews = getBottomNavigationItemViews();
        if (null == bottomNavigationItemViews)
            return 0;
        return bottomNavigationItemViews.length;
    }

    /**
     * set all item small TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    @SuppressLint("RestrictedApi")
    public void setSmallTextSize(float sp) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getSmallLabelAt(i).setTextSize(sp);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set all item large TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal.
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    @SuppressLint("RestrictedApi")
    public void setLargeTextSize(float sp) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTextSize(sp);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set all item large and small TextView size
     * Each item has tow label, one is large, another is small.
     * Small one will be shown when item state is normal
     * Large one will be shown when item checked.
     *
     * @param sp
     */
    public void setTextSize(float sp) {
        setLargeTextSize(sp);
        setSmallTextSize(sp);
    }

    /**
     * set item ImageView size which at position
     *
     * @param position position start from 0
     * @param width    in dp
     * @param height   in dp
     */
    @SuppressLint("RestrictedApi")
    public void setIconSizeAt(int position, float width, float height) {
        ImageView icon = getIconAt(position);
        // update size
        ViewGroup.LayoutParams layoutParams = icon.getLayoutParams();
        layoutParams.width = dp2px(getContext(), width);
        layoutParams.height = dp2px(getContext(), height);
        icon.setLayoutParams(layoutParams);

        mMenuView.updateMenuView();
    }

    /**
     * set all item ImageView size
     *
     * @param width  in dp
     * @param height in dp
     */
    public void setIconSize(float width, float height) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            setIconSizeAt(i, width, height);
        }
    }

    /**
     * set menu item height
     *
     * @param height in px
     */
    @SuppressLint("RestrictedApi")
    public void setItemHeight(int height) {
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. set private final int mItemHeight in mMenuView
        setField(mMenuView.getClass(), mMenuView, "mItemHeight", height);

        mMenuView.updateMenuView();
    }

    /**
     * get menu item height
     *
     * @return in px
     */
    public int getItemHeight() {
        // 1. get mMenuView
        final BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        // 2. get private final int mItemHeight in mMenuView
        return getField(mMenuView.getClass(), mMenuView, "mItemHeight");
    }

    /**
     * dp to px
     *
     * @param context
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * set Typeface for all item TextView
     *
     * @attr ref android.R.styleable#TextView_typeface
     * @attr ref android.R.styleable#TextView_textStyle
     */
    @SuppressLint("RestrictedApi")
    public void setTypeface(Typeface typeface, int style) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTypeface(typeface, style);
            getSmallLabelAt(i).setTypeface(typeface, style);
        }
        mMenuView.updateMenuView();
    }

    /**
     * set Typeface for all item TextView
     *
     * @attr ref android.R.styleable#TextView_typeface
     */
    @SuppressLint("RestrictedApi")
    public void setTypeface(Typeface typeface) {
        int count = getItemCount();
        for (int i = 0; i < count; i++) {
            getLargeLabelAt(i).setTypeface(typeface);
            getSmallLabelAt(i).setTypeface(typeface);
        }
        mMenuView.updateMenuView();
    }

    /**
     * get private filed in this specific object
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param <T>
     * @return field if success, null otherwise.
     */
    private <T> T getField(Class targetClass, Object instance, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * change the field value
     *
     * @param targetClass
     * @param instance    the filed owner
     * @param fieldName
     * @param value
     */
    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will link the given ViewPager and this BottomNavigationViewEx together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks.
     *
     * @param viewPager
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager) {
        setupWithViewPager(viewPager, false);
    }

    /**
     * This method will link the given ViewPager and this BottomNavigationViewEx together so that
     * changes in one are automatically reflected in the other. This includes scroll state changes
     * and clicks.
     *
     * @param viewPager
     * @param smoothScroll whether ViewPager changed with smooth scroll animation
     */
    public void setupWithViewPager(@Nullable final ViewPager viewPager, boolean smoothScroll) {
        if (mViewPager != null) {
            // If we've already been setup with a ViewPager, remove us from it
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            }
        }

        if (null == viewPager) {
            mViewPager = null;
            super.setOnNavigationItemSelectedListener(null);
            return;
        }

        mViewPager = viewPager;

        // Add our custom OnPageChangeListener to the ViewPager
        if (mPageChangeListener == null) {
            mPageChangeListener = new BottomNavigationViewExOnPageChangeListener(this);
        }
        viewPager.addOnPageChangeListener(mPageChangeListener);

        // Now we'll add a navigation item selected listener to set ViewPager's current item
        OnNavigationItemSelectedListener listener = getOnNavigationItemSelectedListener();
        mMyOnNavigationItemSelectedListener = new MyOnNavigationItemSelectedListener(viewPager, this, smoothScroll, listener);
        super.setOnNavigationItemSelectedListener(mMyOnNavigationItemSelectedListener);
    }

    /**
     * A {@link ViewPager.OnPageChangeListener} class which contains the
     * necessary calls back to the provided {@link BottomNavigation} so that the tab position is
     * kept in sync.
     * <p>
     * <p>This class stores the provided BottomNavigationViewEx weakly, meaning that you can use
     * {@link ViewPager#addOnPageChangeListener(ViewPager.OnPageChangeListener)
     * addOnPageChangeListener(OnPageChangeListener)} without removing the listener and
     * not cause a leak.
     */
    public class BottomNavigationViewExOnPageChangeListener implements ViewPager.OnPageChangeListener {

        private final WeakReference<BottomNavigation> mBnveRef;

        public BottomNavigationViewExOnPageChangeListener(BottomNavigation bnve) {
            mBnveRef = new WeakReference<>(bnve);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            if (viewPagerPageChangeListener != null)
                viewPagerPageChangeListener.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset,
                                   final int positionOffsetPixels) {
            if (viewPagerPageChangeListener != null)
                viewPagerPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(final int position) {
            final BottomNavigation bnve = mBnveRef.get();
            if (null != bnve && !isNavigationItemClicking)
                bnve.setCurrentItem(position);

            if (viewPagerPageChangeListener != null)
                viewPagerPageChangeListener.onPageSelected(position);

            if (isHandleBackStackEvent)
                configureBackStackEvent(position);
        }
    }

    /**
     * Decorate OnNavigationItemSelectedListener for setupWithViewPager
     */
    private class MyOnNavigationItemSelectedListener implements OnNavigationItemSelectedListener {
        private OnNavigationItemSelectedListener listener;
        private final WeakReference<ViewPager> viewPagerRef;
        private boolean smoothScroll;
        private SparseIntArray items;// used for change ViewPager selected item
        private int previousPosition = -1;


        MyOnNavigationItemSelectedListener(ViewPager viewPager, BottomNavigation bnve, boolean smoothScroll, OnNavigationItemSelectedListener listener) {
            this.viewPagerRef = new WeakReference<>(viewPager);
            this.listener = listener;
            this.smoothScroll = smoothScroll;

            // create items
            Menu menu = bnve.getMenu();
            int size = menu.size();
            items = new SparseIntArray(size);
            for (int i = 0; i < size; i++) {
                int itemId = menu.getItem(i).getItemId();
                items.put(itemId, i);
            }
        }

        public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int position = items.get(item.getItemId());
            // only set item when item changed
            if (previousPosition == position) {
                return true;
            }
            // user listener
            if (null != listener) {
                boolean bool = listener.onNavigationItemSelected(item);
                // if the selected is invalid, no need change the view pager
                if (!bool)
                    return false;
            }

            // change view pager
            ViewPager viewPager = viewPagerRef.get();
            if (null == viewPager)
                return false;

            // use isNavigationItemClicking flag to avoid `ViewPager.OnPageChangeListener` trigger
            isNavigationItemClicking = true;
            viewPager.setCurrentItem(items.get(item.getItemId()), smoothScroll);
            isNavigationItemClicking = false;

            // update previous position
            previousPosition = position;

            return true;
        }
    }

    @SuppressLint("RestrictedApi")
    public void enableShiftingMode(int position, boolean enable) {
        getBottomNavigationItemView(position).setShiftingMode(enable);
    }

    @SuppressLint("RestrictedApi")
    public void setItemBackground(int position, int background) {
        getBottomNavigationItemView(position).setItemBackground(background);
    }

    @SuppressLint("RestrictedApi")
    public void setIconTintList(int position, ColorStateList tint) {
        getBottomNavigationItemView(position).setIconTintList(tint);
    }

    @SuppressLint("RestrictedApi")
    public void setTextTintList(int position, ColorStateList tint) {
        getBottomNavigationItemView(position).setTextColor(tint);
    }

    /**
     * set margin top for all icons
     *
     * @param marginTop in px
     */
    public void setIconsMarginTop(int marginTop) {
        for (int i = 0; i < getItemCount(); i++) {
            setIconMarginTop(i, marginTop);
        }
    }

    /**
     * set margin top for icon
     *
     * @param position
     * @param marginTop in px
     */
    @SuppressLint("RestrictedApi")
    public void setIconMarginTop(int position, int marginTop) {
        /*
        1. BottomNavigationItemView
        2. private final int mDefaultMargin;
         */
        BottomNavigationItemView itemView = getBottomNavigationItemView(position);
        setField(BottomNavigationItemView.class, itemView, "mDefaultMargin", marginTop);
        mMenuView.updateMenuView();
    }

    /**
     * Handle back stack event
     */
    public void setHandleBackStackEvent(boolean handleBackStackEvent) {
        isHandleBackStackEvent = handleBackStackEvent;
    }

    private void configureBackStackEvent(int position) {
        String state = String.valueOf(position);
        if (indexOf(state) > 0)
            backStack.remove(indexOf(state));
        backStack.add(state);
    }

    private int indexOf(String position) {
        for (int i = backStack.size()-1; i >= 0; i--) {
            if ((backStack.get(i).equals(position)))
                return i;
        }
        return 0;
    }

    public void initBackStack() {
        backStack.clear();
        backStack.add(INIT_BACKSTACK);
    }

    public ArrayList<String> getBackStacks() {
        return backStack;
    }

    public int getBackStackEntryCount() {
        return backStack.size();
    }

    public void handleBackPressed() {
        if (backStack.size() > 0) {
            backStack.remove(backStack.size()-1);

            int item = Integer.parseInt(backStack.get(backStack.size()-1));
            mViewPager.setCurrentItem(item);

            if (backStack.size() > 1)
                if (backStack.get(1).equals(INIT_BACKSTACK))
                    backStack.remove(1);
        }
    }
}