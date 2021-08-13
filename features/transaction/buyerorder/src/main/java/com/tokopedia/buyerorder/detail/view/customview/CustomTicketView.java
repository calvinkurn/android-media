package com.tokopedia.buyerorder.detail.view.customview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.IntDef;
import androidx.core.content.ContextCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import com.tokopedia.buyerorder.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.graphics.Bitmap.Config.ALPHA_8;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.graphics.PorterDuff.Mode.SRC_IN;


public class CustomTicketView extends View {

    public static final String TAG = CustomTicketView.class.getSimpleName();


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CornerType.NORMAL, CornerType.ALLROUNDED})
    public @interface CornerType {
        int NORMAL = 0;
        int ALLROUNDED = 1;
        int TOPROUNDEDBOTTOMSCALLOP = 2;
        int BOTTOMROUNDEDTOPSCALLOP = 3;
        int ALLSCALLOP = 4;
    }


    private Paint mBackgroundPaint = new Paint();
    private Paint mBorderPaint = new Paint();
    private Path mPath = new Path();
    private Path mPathBorder = new Path();
    private boolean mDirty = true;

    private RectF mRoundedCornerArc = new RectF();
    private RectF mScallopCornerArc = new RectF();

    private int mBackgroundColor;
    private boolean mShowBorder;
    private boolean mShowTopShadow;
    private boolean mShowBottomShadow;
    private int mBorderWidth;
    private int mBorderColor;
    private int mScallopRadius;
    private int mCornerType;
    private int mCornerRadius;
    private Bitmap mShadow;
    private final Paint mShadowPaint = new Paint(ANTI_ALIAS_FLAG);
    private float mShadowBlurRadius = 0f;

    public CustomTicketView(Context context) {
        super(context);
        init(null);
    }

    public CustomTicketView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomTicketView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDirty) {
            doLayout();
        }
        if (mShadowBlurRadius > 0f && !isInEditMode()) {
            canvas.drawBitmap(mShadow, 0f, mShadowBlurRadius / 2f, null);
        }
        canvas.drawPath(mPath, mBackgroundPaint);
        if (mShowBorder) {
            canvas.drawPath(mPathBorder, mBorderPaint);
        }
    }


    private void doLayout() {
        float left = getPaddingLeft() + mShadowBlurRadius;
        float right = getWidth() - getPaddingRight() - mShadowBlurRadius;

        float top;
        if (mShowTopShadow)
            top = getPaddingTop() + (mShadowBlurRadius / 2);
        else
            top = getPaddingTop();

        float bottom;
        if (mShowBottomShadow)
            bottom = getHeight() - getPaddingBottom() - mShadowBlurRadius - (mShadowBlurRadius / 2);
        else
            bottom = getHeight() - getPaddingBottom();

        mPath.reset();

        if (mCornerType == CornerType.ALLROUNDED) {
            topRoundedCorners(left, right, top);
            bottomRoundedCorners(left, right, bottom);

        } else if (mCornerType == CornerType.ALLSCALLOP) {
            topScallopCorners(left, right, top);
            bottomScallopCorners(left, right, bottom);

        } else if (mCornerType == CornerType.TOPROUNDEDBOTTOMSCALLOP) {
            topRoundedCorners(left, right, top);
            bottomScallopCorners(left, right, bottom);

        } else if (mCornerType == CornerType.BOTTOMROUNDEDTOPSCALLOP) {
            topScallopCorners(left, right, top);
            bottomRoundedCorners(left, right, bottom);

        } else {
            mPath.moveTo(left, top);
            mPath.lineTo(right, top);
        }

        generateShadow();
        mDirty = false;
    }

    private void topRoundedCorners(float left, float right, float top) {
        mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false);
        mPath.lineTo(left + mCornerRadius, top);
        mPath.lineTo(right - mCornerRadius, top);
        mPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false);
        mPathBorder.addPath(mPath);

    }

    private void bottomRoundedCorners(float left, float right, float bottom) {

        mPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false);
        mPath.lineTo(right - mCornerRadius, bottom);

        mPath.lineTo(left + mCornerRadius, bottom);
        mPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false);
    }

    private void topScallopCorners(float left, float right, float top) {
        mPath.arcTo(getTopLeftCornerScallopArc(top, left), 90.0f, -90.0f, false);
        mPath.lineTo(left + mScallopRadius, top);
        mPath.lineTo(right - mScallopRadius, top);
        mPath.arcTo(getTopRightCornerScallopArc(top, right), 180.0f, -90.0f, false);

    }

    private void bottomScallopCorners(float left, float right, float bottom) {

        mPath.arcTo(getBottomRightCornerScallopArc(bottom, right), 270.0f, -90.0f, false);
        mPath.lineTo(right - mScallopRadius, bottom);

        mPath.lineTo(left + mScallopRadius, bottom);
        mPath.arcTo(getBottomLeftCornerScallopArc(left, bottom), 0.0f, -90.0f, false);
    }


    private void generateShadow() {
        if (isJellyBeanAndAbove() && !isInEditMode()) {
            if (mShadowBlurRadius == 0f) return;

            if (mShadow == null) {
                mShadow = Bitmap.createBitmap(getWidth(), getHeight(), ALPHA_8);
            } else {
                mShadow.eraseColor(TRANSPARENT);
            }
            Canvas c = new Canvas(mShadow);
            c.drawPath(mPath, mShadowPaint);
            if (mShowBorder) {
                c.drawPath(mPath, mShadowPaint);
            }
            RenderScript rs = RenderScript.create(getContext());
            ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8(rs));
            Allocation input = Allocation.createFromBitmap(rs, mShadow);
            Allocation output = Allocation.createTyped(rs, input.getType());
            blur.setRadius(mShadowBlurRadius);
            blur.setInput(input);
            blur.forEach(output);
            output.copyTo(mShadow);
            input.destroy();
            output.destroy();
            blur.destroy();
        }
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTicketView);
            mBackgroundColor = typedArray.getColor(R.styleable.CustomTicketView_backgroundColor, getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0));
            mScallopRadius = typedArray.getDimensionPixelSize(R.styleable.CustomTicketView_scallopRadius, dpToPx(20f, getContext()));
            mShowBorder = typedArray.getBoolean(R.styleable.CustomTicketView_showBorder, false);
            mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.CustomTicketView_borderWidth, dpToPx(2f, getContext()));
            mBorderColor = typedArray.getColor(R.styleable.CustomTicketView_borderColor, getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_N700));
            mCornerType = typedArray.getInt(R.styleable.CustomTicketView_cornerType, CornerType.NORMAL);
            mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.CustomTicketView_cornerRadius, dpToPx(4f, getContext()));
            mShowTopShadow = typedArray.getBoolean(R.styleable.CustomTicketView_showTopShadow, false);
            mShowBottomShadow = typedArray.getBoolean(R.styleable.CustomTicketView_showBottomShadow, false);

            float elevation = 0f;
            if (typedArray.hasValue(R.styleable.CustomTicketView_ticketElevation)) {
                elevation = typedArray.getDimension(R.styleable.CustomTicketView_ticketElevation, elevation);
            } else if (typedArray.hasValue(R.styleable.CustomTicketView_android_elevation)) {
                elevation = typedArray.getDimension(R.styleable.CustomTicketView_android_elevation, elevation);
            }
            if (elevation > 0f) {
                setShadowBlurRadius(elevation);
            }

            typedArray.recycle();
        }

        mShadowPaint.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(), com.tokopedia.unifyprinciples.R.color.Unify_G900), SRC_IN));
        mShadowPaint.setAlpha(51); // 20%

        initElements();

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initElements() {


        setBackgroundPaint();
        setBorderPaint();
        mDirty = true;
        invalidate();
    }

    private void setBackgroundPaint() {
        mBackgroundPaint.setAlpha(0);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void setBorderPaint() {
        mBorderPaint.setAlpha(0);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
    }


    private RectF getTopLeftCornerRoundedArc(float top, float left) {
        mRoundedCornerArc.set(left, top, left + mCornerRadius * 2, top + mCornerRadius * 2);
        return mRoundedCornerArc;
    }

    private RectF getTopRightCornerRoundedArc(float top, float right) {
        mRoundedCornerArc.set(right - mCornerRadius * 2, top, right, top + mCornerRadius * 2);
        return mRoundedCornerArc;
    }

    private RectF getBottomLeftCornerRoundedArc(float left, float bottom) {
        mRoundedCornerArc.set(left, bottom - mCornerRadius * 2, left + mCornerRadius * 2, bottom);
        return mRoundedCornerArc;
    }

    private RectF getBottomRightCornerRoundedArc(float bottom, float right) {
        mRoundedCornerArc.set(right - mCornerRadius * 2, bottom - mCornerRadius * 2, right, bottom);
        return mRoundedCornerArc;
    }

    private RectF getTopLeftCornerScallopArc(float top, float left) {
        mScallopCornerArc.set(left - mScallopRadius, top - mScallopRadius, left + mScallopRadius, top + mScallopRadius);
        return mScallopCornerArc;
    }

    private RectF getTopRightCornerScallopArc(float top, float right) {
        mScallopCornerArc.set(right - mScallopRadius, top - mScallopRadius, right + mScallopRadius, top + mScallopRadius);
        return mScallopCornerArc;
    }

    private RectF getBottomLeftCornerScallopArc(float left, float bottom) {
        mScallopCornerArc.set(left - mScallopRadius, bottom - mScallopRadius, left + mScallopRadius, bottom + mScallopRadius);
        return mScallopCornerArc;
    }

    private RectF getBottomRightCornerScallopArc(float bottom, float right) {
        mScallopCornerArc.set(right - mScallopRadius, bottom - mScallopRadius, right + mScallopRadius, bottom + mScallopRadius);
        return mScallopCornerArc;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.mBackgroundColor = backgroundColor;
        initElements();
    }

    public boolean isShowBorder() {
        return mShowBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.mShowBorder = showBorder;
        initElements();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        initElements();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        this.mBorderColor = borderColor;
        initElements();
    }

    public int getScallopRadius() {
        return mScallopRadius;
    }

    public void setScallopRadius(int scallopRadius) {
        this.mScallopRadius = scallopRadius;
        initElements();
    }

    public int getCornerType() {
        return mCornerType;
    }

    public void setCornerType(int cornerType) {
        this.mCornerType = cornerType;
        initElements();
    }

    public int getCornerRadius() {
        return mCornerRadius;
    }

    public void setCornerRadius(int cornerRadius) {
        this.mCornerRadius = cornerRadius;
        initElements();
    }

    public void setTicketElevation(float elevation) {
        if (!isJellyBeanAndAbove()) {
            Log.w(TAG, "Ticket elevation only works with Android Jelly Bean and above");
            return;
        }
        setShadowBlurRadius(elevation);
        initElements();
    }

    private void setShadowBlurRadius(float elevation) {
        if (!isJellyBeanAndAbove()) {
            Log.w(TAG, "Ticket elevation only works with Android Jelly Bean and above");
            return;
        }
        float maxElevation = dpToPx(24f, getContext());
        mShadowBlurRadius = Math.min(25f * (elevation / maxElevation), 25f);
    }

    private boolean isJellyBeanAndAbove() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    private int dpToPx(float dp, Context context) {
        return dpToPx(dp, context.getResources());
    }

    private int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}