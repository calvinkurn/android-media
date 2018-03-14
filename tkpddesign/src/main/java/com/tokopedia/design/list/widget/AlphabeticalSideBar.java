package com.tokopedia.design.list.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SectionIndexer;

import com.tokopedia.design.R;

public class AlphabeticalSideBar extends View {

    private static final int ALPHABET_COUNT = 26;

    private String alphabetText;
    private SectionIndexer sectionIndexer = null;
    private RecyclerView recyclerView;
    private float alphabetHeight;
    private float totalHeight;
    private float totalWidth;
    private float lastSelectedY;
    private Paint contentTextPaint;
    private Paint magnifierBackPaint;
    private Paint magnifierTextPaint;
    private char lastSelectedAlphabet;
    private int itemTextSize;
    private int magnifierPadding;
    private int magnifierRadius;
    private int itemTopMargin;
    private int itemBottomMargin;
    private int touchAreaLeftOffset;
    private boolean isDragging = false;

    public AlphabeticalSideBar(Context context) {
        super(context);
        init();
    }

    public AlphabeticalSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AlphabeticalSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        alphabetText = "A.D.G.J.M.P.S.V.Z";
        itemTextSize = getContext().getResources().getDimensionPixelSize(R.dimen.sp_12);
        int magnifierTextSize = getContext().getResources().getDimensionPixelSize(R.dimen.sp_20);
        magnifierPadding = getContext().getResources().getDimensionPixelSize(R.dimen.dp_100);
        int magnifierToSidebarDistance = getContext().getResources().getDimensionPixelSize(R.dimen.dp_20);
        magnifierRadius = magnifierTextSize / 2 + magnifierPadding;
        touchAreaLeftOffset = magnifierRadius * 2 + magnifierToSidebarDistance;
        itemTopMargin = magnifierRadius;
        itemBottomMargin = magnifierRadius;

        totalWidth = itemTextSize + magnifierToSidebarDistance + magnifierRadius * 2;
        totalHeight = itemTextSize * alphabetText.length();
        alphabetHeight = totalHeight / ALPHABET_COUNT;

        contentTextPaint = new Paint();
        contentTextPaint.setTextSize(itemTextSize);
        contentTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        contentTextPaint.setTextAlign(Paint.Align.CENTER);
        contentTextPaint.setColor(getContext().getResources().getColor(R.color.tkpd_main_green));
        contentTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        magnifierBackPaint = new Paint();
        magnifierBackPaint.setColor(getContext().getResources().getColor(R.color.tkpd_main_green));
        magnifierBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        magnifierTextPaint = new Paint();
        magnifierTextPaint.setColor(getContext().getResources().getColor(R.color.white));
        magnifierTextPaint.setTextSize(magnifierTextSize);
        magnifierTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        magnifierTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) totalWidth + getPaddingRight(),
                (int) totalHeight + itemTopMargin + itemBottomMargin);
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        sectionIndexer = (SectionIndexer) recyclerView.getAdapter();
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getX() > touchAreaLeftOffset) {
            isDragging = true;
            handleGesture(event);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            isDragging = false;
            lastSelectedAlphabet = 0;
            lastSelectedY = 0;
            invalidate();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && isDragging) {
            handleGesture(event);
            return true;
        }

        return false;
    }

    private void handleGesture(MotionEvent event) {
        char selectedAlphabet = findSelectedAlphabet(event);
        if (isValidAlphabet(selectedAlphabet)) {
            int position = sectionIndexer.getPositionForSection(selectedAlphabet);
            if (position != -1) {
                recyclerView.smoothScrollToPosition(position);
            }
            lastSelectedAlphabet = selectedAlphabet;
            lastSelectedY = event.getY();
            invalidate();
        }
    }

    private char findSelectedAlphabet(MotionEvent event) {
        int offset = (int) ((event.getY() - itemTopMargin) / alphabetHeight);
        return (char) ('A' + offset);
    }

    private boolean isValidAlphabet(char selectedAlphabet) {
        if (selectedAlphabet >= 'A' && selectedAlphabet <= 'Z') {
            return true;
        } else {
            return false;
        }
    }

    protected void onDraw(Canvas canvas) {

        float itemCenterX = totalWidth - itemTextSize / 2;
        float bulletRadius = itemTextSize / 6;

        for (int i = 0; i < alphabetText.length(); i++) {
            char c = alphabetText.charAt(i);
            if (c == '.') {
                float bulletY = itemTopMargin + (i + 0.5f) * itemTextSize;
                canvas.drawCircle(itemCenterX, bulletY, bulletRadius, contentTextPaint);
            } else {
                float textY = itemTopMargin + (i + 1) * itemTextSize;
                canvas.drawText(String.valueOf(c), itemCenterX, textY, contentTextPaint);
            }
        }

        float magnifierCenterX = magnifierRadius;

        if (lastSelectedAlphabet != 0) {
            float magnifierCenterY = normalizeForMagnifierCenterY(lastSelectedY);
            canvas.drawCircle(magnifierCenterX, magnifierCenterY, magnifierRadius, magnifierBackPaint);

            float magnifierTextY = magnifierCenterY + magnifierRadius - magnifierPadding;
            canvas.drawText(String.valueOf(lastSelectedAlphabet), magnifierCenterX, magnifierTextY, magnifierTextPaint);
        }
        super.onDraw(canvas);
    }

    private float normalizeForMagnifierCenterY(float y) {
        return y > itemTopMargin ? y : itemTopMargin;
    }
}