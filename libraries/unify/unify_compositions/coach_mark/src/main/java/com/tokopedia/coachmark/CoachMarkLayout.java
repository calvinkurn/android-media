/*
 * Copyright 2019 Tokopedia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Hendy
 * Edited by Meyta
 */

package com.tokopedia.coachmark;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.coachmark.util.ViewHelper;

public class CoachMarkLayout extends FrameLayout {

    private int layoutRes;
    private boolean isCancelable;
    private int circleBackgroundDrawableRes;
    private int spacing;
    private int arrowMargin;
    private int arrowWidth;

    // View
    private ViewGroup viewGroup;
    private Bitmap bitmap;
    private View lastTutorialView;
    private Paint viewPaint;
    private Paint arrowPaint;

    // listener
    private CoachMarkListener showCaseListener;

    CoachMarkContentPosition coachMarkContentPosition;

    private int highlightLocX;
    private int highlightLocY;

    // determined if this is last chain
    private boolean isStart;
    private boolean isLast;

    // path for arrow
    private Path path;
    private TextView textViewTitle;
    private TextView textViewDesc;
    private TextView prevButton;
    private TextView nextButton;
    private ViewGroup viewGroupIndicator;

    public CoachMarkLayout(Context context, @Nullable CoachMarkBuilder builder) {
        super(context);
        init(context, builder);
    }

    public CoachMarkLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CoachMarkLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
    }

    public CoachMarkLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, null);
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public CoachMarkLayout(Context context,
                           AttributeSet attrs,
                           int defStyleAttr,
                           int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, null);
    }

    private void init(Context context, @Nullable CoachMarkBuilder builder) {
        setVisibility(View.GONE);

        if (isInEditMode()) {
            return;
        }

        initFrame();
        applyAttrs(context, builder);

        // setContentView
        initContent(context);

        setClickable(this.isCancelable);
        setFocusable(this.isCancelable);

        if (this.isCancelable) {
            this.setOnClickListener(v -> onNextClicked());
        }
    }

    private void onNextClicked() {
        if (showCaseListener != null) {
            if (this.isLast) {
                CoachMarkLayout.this.showCaseListener.onComplete();
            } else {
                CoachMarkLayout.this.showCaseListener.onNext();
            }
        }
    }

    public void setShowCaseListener(CoachMarkListener showCaseListener) {
        this.showCaseListener = showCaseListener;
    }

    public void showTutorial(View view,
                             String title,
                             String text,
                             int currentTutorIndex,
                             int tutorsListSize,
                             CoachMarkContentPosition coachMarkContentPosition,
                             int tintBackgroundColor,
                             final int[] customTarget, final int radius) throws Throwable {

        this.isStart = currentTutorIndex == 0;

        this.isLast = currentTutorIndex == tutorsListSize - 1;
        this.coachMarkContentPosition = coachMarkContentPosition;

        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        if (this.lastTutorialView != null) {
            this.lastTutorialView.setDrawingCacheEnabled(false);
        }

        if (TextUtils.isEmpty(title)) {
            textViewTitle.setVisibility(View.GONE);
        } else {
            textViewTitle.setText(fromHtml(title));
            textViewTitle.setVisibility(View.VISIBLE);
        }

        textViewDesc.setText(fromHtml(text));

        if (prevButton != null) {
            if (isStart || isLast) {
                prevButton.setEnabled(false);
            } else {
                prevButton.setEnabled(true);
            }
        }

        if (nextButton != null) {
            if (isLast) {
                nextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_coachmark_done));
            } else if (currentTutorIndex < tutorsListSize - 1) { // has next
                nextButton.setEnabled(true);
                nextButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_coachmark_right));
            }
        }

        makeCircleIndicator(!isStart || !isLast, currentTutorIndex, tutorsListSize);

        if (view == null) {
            this.lastTutorialView = null;
            this.bitmap = null;
            this.highlightLocX = 0;
            this.highlightLocY = 0;
            moveViewToCenter();
        } else {
            this.lastTutorialView = view;
            if (view.willNotCacheDrawing()) {
                view.setWillNotCacheDrawing(false);
            }
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
            if (tintBackgroundColor == 0) {
                this.bitmap = view.getDrawingCache();
            } else {
                Bitmap bitmapTemp = view.getDrawingCache();

                Bitmap bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                        view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                bigCanvas.drawColor(tintBackgroundColor);
                Paint paint = new Paint();
                bigCanvas.drawBitmap(bitmapTemp, 0f, 0f, paint);

                this.bitmap = bigBitmap;
            }

            //set custom target to view
            if (customTarget != null) {
                if (customTarget.length == 2) {
                    this.bitmap = ViewHelper.getCroppedBitmap(bitmap, customTarget, radius);
                } else if (customTarget.length == 4) {
                    this.bitmap = ViewHelper.getCroppedBitmap(bitmap, customTarget);
                }

                this.highlightLocX = customTarget[0] - radius;
                this.highlightLocY = customTarget[1] - radius;
            } else { // use view location as target
                final int[] location = new int[2];
                view.getLocationInWindow(location);

                this.highlightLocX = location[0];
                this.highlightLocY = location[1] - ViewHelper.getStatusBarHeight(getContext());
            }

            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(CoachMarkLayout.this.bitmap != null) {
                        moveViewBasedHighlight(CoachMarkLayout.this.highlightLocX,
                                CoachMarkLayout.this.highlightLocY,
                                CoachMarkLayout.this.highlightLocX + CoachMarkLayout.this.bitmap.getWidth(),
                                CoachMarkLayout.this.highlightLocY + CoachMarkLayout.this.bitmap.getHeight());

                        if (Build.VERSION.SDK_INT < 16) {
                            CoachMarkLayout.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            CoachMarkLayout.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        invalidate();
                    }
                }
            });
        }

        this.setVisibility(View.VISIBLE);
    }

    public void hideTutorial() {
        this.setVisibility(View.INVISIBLE);
    }

    private void makeCircleIndicator(boolean hasMoreOneCircle,
                                     int currentTutorIndex,
                                     int tutorsListSize) {
        if (this.viewGroupIndicator != null) {
            if (hasMoreOneCircle) { // has more than 1 circle
                // already has circle indicator
                if (this.viewGroupIndicator.getChildCount() == tutorsListSize) {
                    for (int i = 0; i < tutorsListSize; i++) {
                        View viewCircle = this.viewGroupIndicator.getChildAt(i);
                        if (i == currentTutorIndex) {
                            viewCircle.setSelected(true);
                        } else {
                            viewCircle.setSelected(false);
                        }
                    }
                } else { //reinitialize, the size is different
                    this.viewGroupIndicator.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    for (int i = 0; i < tutorsListSize; i++) {
                        View viewCircle = inflater.inflate(R.layout.layout_indicator,
                                viewGroupIndicator,
                                false);
                        viewCircle.setBackgroundResource(this.circleBackgroundDrawableRes);
                        if (i == currentTutorIndex) {
                            viewCircle.setSelected(true);
                        }
                        this.viewGroupIndicator.addView(viewCircle);
                    }
                }
            } else {
                this.viewGroupIndicator.removeAllViews();
            }
        }
    }

    public void closeTutorial() {
        setVisibility(View.GONE);
        if (this.bitmap != null) {
            this.bitmap.recycle();
            this.bitmap = null;
        }
        if (lastTutorialView != null) {
            this.lastTutorialView.setDrawingCacheEnabled(false);
            this.lastTutorialView = null;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleResources();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        super.onDraw(canvas);
        canvas.drawBitmap(this.bitmap, this.highlightLocX, this.highlightLocY, viewPaint);

        if (path != null && this.viewGroup.getVisibility() == View.VISIBLE ) {
            canvas.drawPath(path, arrowPaint);
        }
    }

    private void initFrame() {
        setWillNotDraw(false);

        viewPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        viewPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStyle(Paint.Style.FILL);

        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.shadow));
    }

    private void applyAttrs(Context context, @Nullable CoachMarkBuilder builder) {
        // set Default value before check builder (might be null)
        this.layoutRes = R.layout.layout_coachmark;

        this.spacing = (int) getResources().getDimension(R.dimen.spacing_lvl4);
        this.arrowMargin = this.spacing / 3;
        this.arrowWidth = (int) (1.5 * this.spacing );

        this.circleBackgroundDrawableRes = R.drawable.indicator_default;

        if (builder == null) {
            return;
        }

        this.layoutRes = builder.getLayoutRes() != 0 ?
                builder.getLayoutRes()
                : this.layoutRes;

        this.circleBackgroundDrawableRes = builder.getCircleIndicatorBackgroundDrawableRes() != 0 ?
                builder.getCircleIndicatorBackgroundDrawableRes()
                : this.circleBackgroundDrawableRes;

        this.isCancelable = true;
    }

    private void initContent(Context context) {
        this.viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(this.layoutRes, this, false);

        View viewGroupTutorContent = viewGroup.findViewById(R.id.view_group_tutor_content);
        textViewTitle = viewGroupTutorContent.findViewById(R.id.text_title);
        textViewTitle = viewGroupTutorContent.findViewById(R.id.text_title);
        textViewDesc = viewGroupTutorContent.findViewById(R.id.text_description);

        prevButton = viewGroupTutorContent.findViewById(R.id.text_previous);
        nextButton = viewGroupTutorContent.findViewById(R.id.text_next);

        viewGroupIndicator = viewGroupTutorContent.findViewById(R.id.view_group_indicator);

        if (prevButton != null) {
            prevButton.setOnClickListener(v -> {
                if (showCaseListener != null) {
                    if (CoachMarkLayout.this.isStart) {
                        CoachMarkLayout.this.showCaseListener.onComplete();
                    } else {
                        CoachMarkLayout.this.showCaseListener.onPrevious();
                    }
                }
            });
        }
        if (nextButton != null) {
            nextButton.setOnClickListener(v -> onNextClicked());
        }

        this.addView(viewGroup);
    }

    private void moveViewBasedHighlight(int highlightXstart,
                                        int highlightYstart,
                                        int highlightXend,
                                        int highlightYend) {
        if (coachMarkContentPosition == CoachMarkContentPosition.UNDEFINED) {
            int widthCenter = this.getWidth() / 2;
            int heightCenter = this.getHeight() / 2;
            if (highlightYend <= heightCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.BOTTOM;
            } else if (highlightYstart >= heightCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.TOP;
            } else if (highlightXend <= widthCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.RIGHT;
            } else if (highlightXstart >= widthCenter) {
                coachMarkContentPosition = CoachMarkContentPosition.LEFT;
            } else { // not fit anywhere
                // if bottom is bigger, put to bottom, else put it on top
                if ((this.getHeight() - highlightYend) > highlightYstart) {
                    coachMarkContentPosition = CoachMarkContentPosition.BOTTOM;
                } else {
                    coachMarkContentPosition = CoachMarkContentPosition.TOP;
                }
            }
        }

        LayoutParams layoutParams;
        switch (coachMarkContentPosition) {
            case RIGHT: {
                int expectedWidth = getWidth() - highlightXend - 2 * this.spacing;

                viewGroup.measure(MeasureSpec.makeMeasureSpec(expectedWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int viewGroupHeight = viewGroup.getMeasuredHeight();

                layoutParams = new LayoutParams(
                        expectedWidth,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.END);
                layoutParams.rightMargin = this.spacing;
                layoutParams.leftMargin = this.spacing;
                layoutParams.bottomMargin = 0;

                // calculate diff top height between object and the content;
                int hightLightHeight = (highlightYend - highlightYstart);
                int diffHeight = hightLightHeight - viewGroupHeight;

                // check top margin. top should not out of window
                int expectedTopMargin = highlightYstart + diffHeight / 2;
                checkMarginTopBottom(expectedTopMargin, layoutParams, viewGroupHeight);

                setLayoutViewGroup(layoutParams);

                if (arrowWidth == 0) {
                    path = null;
                } else {
                    int highLightCenterY = (highlightYend + highlightYstart) / 2;
                    int recalcArrowWidth = getRecalculateArrowWidth(highLightCenterY, getHeight());
                    if (recalcArrowWidth == 0) {
                        path = null;
                    } else {
                        path = new Path();
                        path.moveTo(highlightXend + this.arrowMargin, highLightCenterY);
                        path.lineTo(highlightXend + this.spacing ,
                                highLightCenterY - arrowWidth / 2);
                        path.lineTo(highlightXend + this.spacing ,
                                highLightCenterY + arrowWidth / 2);
                        path.close();
                    }
                }
            }
            break;
            case LEFT: {
                int expectedWidth = highlightXstart - 2 * this.spacing;

                viewGroup.measure(MeasureSpec.makeMeasureSpec(expectedWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int viewGroupHeight = viewGroup.getMeasuredHeight();

                layoutParams = new LayoutParams(
                        expectedWidth,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.START);
                layoutParams.leftMargin = this.spacing;
                layoutParams.rightMargin = this.spacing;
                layoutParams.bottomMargin = 0;

                // calculate diff top height between object and the content;
                int hightLightHeight = (highlightYend - highlightYstart);
                int diffHeight = hightLightHeight - viewGroupHeight;

                // check top margin. top should not out of window
                int expectedTopMargin = highlightYstart + diffHeight / 2;
                checkMarginTopBottom(expectedTopMargin, layoutParams, viewGroupHeight);

                setLayoutViewGroup(layoutParams);

                if (arrowWidth == 0) {
                    path = null;
                } else {
                    int highLightCenterY = (highlightYend + highlightYstart) / 2;
                    int recalcArrowWidth = getRecalculateArrowWidth(highLightCenterY, getHeight());
                    if (recalcArrowWidth == 0) {
                        path = null;
                    } else {
                        path = new Path();
                        path.moveTo(highlightXstart - this.arrowMargin, highLightCenterY);
                        path.lineTo(highlightXstart - this.spacing ,
                                highLightCenterY - arrowWidth / 2);
                        path.lineTo(highlightXstart - this.spacing ,
                                highLightCenterY + arrowWidth / 2);
                        path.close();
                    }
                }
            }
            break;
            case BOTTOM: {
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP);
                layoutParams.topMargin = highlightYend + this.spacing;
                layoutParams.leftMargin = this.spacing;
                layoutParams.rightMargin = this.spacing;
                layoutParams.bottomMargin = 0;

                setLayoutViewGroup(layoutParams);

                if (arrowWidth == 0) {
                    path = null;
                } else {
                    int highLightCenterX = (highlightXend + highlightXstart) / 2;
                    int recalcArrowWidth = getRecalculateArrowWidth(highLightCenterX, getWidth());
                    if (recalcArrowWidth == 0) {
                        path = null;
                    } else {
                        path = new Path();
                        path.moveTo(highLightCenterX, highlightYend + this.arrowMargin);
                        path.lineTo(highLightCenterX - recalcArrowWidth / 2,
                                highlightYend + this.spacing );
                        path.lineTo(highLightCenterX + recalcArrowWidth / 2,
                                highlightYend + this.spacing );
                        path.close();
                    }
                }
            }
            break;
            case TOP: {
                layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM);
                layoutParams.bottomMargin = getHeight() - highlightYstart + this.spacing;
                layoutParams.topMargin = 0;
                layoutParams.leftMargin = this.spacing;
                layoutParams.rightMargin = this.spacing;

                setLayoutViewGroup(layoutParams);

                if (arrowWidth == 0) {
                    path = null;
                } else {
                    int highLightCenterX = (highlightXend + highlightXstart) / 2;
                    int recalcArrowWidth = getRecalculateArrowWidth(highLightCenterX, getWidth());
                    if (recalcArrowWidth == 0) {
                        path = null;
                    } else {
                        path = new Path();
                        path.moveTo(highLightCenterX, highlightYstart - this.arrowMargin);
                        path.lineTo(highLightCenterX - recalcArrowWidth / 2,
                                highlightYstart - this.spacing );
                        path.lineTo(highLightCenterX + recalcArrowWidth / 2,
                                highlightYstart - this.spacing );
                        path.close();
                    }
                }
            }
            break;
            case UNDEFINED:
                moveViewToCenter();
                break;
        }
    }

    private void setLayoutViewGroup(LayoutParams params){
        this.viewGroup.setVisibility(View.INVISIBLE);

        this.viewGroup.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                CoachMarkLayout.this.viewGroup.setVisibility(View.VISIBLE);
                CoachMarkLayout.this.viewGroup.removeOnLayoutChangeListener(this);
            }
        });
        this.viewGroup.setLayoutParams(params);
        invalidate();
    }

    private int getRecalculateArrowWidth(int highlightCenter, int maxWidthOrHeight) {
        int recalcArrowWidth = arrowWidth;
        int safeArrowWidth = this.spacing + (arrowWidth / 2);
        if (highlightCenter < safeArrowWidth ||
                highlightCenter > (maxWidthOrHeight - safeArrowWidth)) {
            recalcArrowWidth = 0;
        }
        return recalcArrowWidth;
    }

    private void moveViewToCenter() {
        coachMarkContentPosition = CoachMarkContentPosition.UNDEFINED;

        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        layoutParams.rightMargin = this.spacing;
        layoutParams.leftMargin = this.spacing;
        layoutParams.bottomMargin = this.spacing;
        layoutParams.topMargin = this.spacing;

        setLayoutViewGroup(layoutParams);
        this.path = null;
    }

    private void checkMarginTopBottom(int expectedTopMargin,
                                      LayoutParams layoutParams,
                                      int viewHeight) {
        if (expectedTopMargin < this.spacing) {
            layoutParams.topMargin = this.spacing;
        } else {
            // check bottom margin. bottom should not out of window
            int prevActualHeight = expectedTopMargin + viewHeight + this.spacing;
            if (prevActualHeight > getHeight()) {
                int diff = prevActualHeight - getHeight();
                layoutParams.topMargin = expectedTopMargin - diff;
            } else {
                layoutParams.topMargin = expectedTopMargin;
            }
        }
    }

    private void recycleResources() {
        if (this.bitmap != null) {
            this.bitmap.recycle();
        }
        this.bitmap = null;
        if (this.lastTutorialView != null) {
            this.lastTutorialView.setDrawingCacheEnabled(false);
        }
        this.lastTutorialView = null;
        this.viewPaint = null;
    }

    private Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}