package com.tokopedia.unifyprinciples;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class Typography extends AppCompatTextView {

    private static final int HEADING_1 = 1;
    private static final int HEADING_2 = 2;
    private static final int HEADING_3 = 3;
    private static final int HEADING_4 = 4;
    private static final int HEADING_5 = 5;
    private static final int HEADING_6 = 6;

    private static final int BODY_1 = 7;
    private static final int BODY_2 = 8;
    private static final int BODY_3 = 9;

    private static final int SMALL = 10;

    private static final int REGULER = 1;
    private static final int BOLD = 2;

    private Typeface nunitoSans = Typeface.createFromAsset(getContext().getAssets(), "fonts/NunitoSans-ExtraBold.ttf");
    private Typeface robotoRegular = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
    private Typeface robotoBold = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");

    public Typography(Context context) {
        super(context);
    }

    public Typography(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public Typography(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.Typography);

            configFontSize(attributeArray);
            configLineHeight(attributeArray);
            configLetterSpacing(attributeArray);

            attributeArray.recycle();
        }
    }

    private void configFontSize(TypedArray attributeArray) {
        int fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0);
        int weightType = attributeArray.getInteger(R.styleable.Typography_typographyWeight, REGULER);

        switch (fontType) {
            case HEADING_1 :
                this.setTextSize(COMPLEX_UNIT_SP, 28);
                this.setMinimumHeight(34);
                this.setTypeface(nunitoSans);
                break;
            case HEADING_2 :
                this.setTextSize(COMPLEX_UNIT_SP, 20);
                this.setMinimumHeight(30);
                this.setTypeface(nunitoSans);
                break;
            case HEADING_3 :
                this.setTextSize(COMPLEX_UNIT_SP, 18);
                this.setMinimumHeight(26);
                this.setTypeface(nunitoSans);
                break;
            case HEADING_4 :
                this.setTextSize(COMPLEX_UNIT_SP, 16);
                this.setMinimumHeight(24);
                this.setTypeface(nunitoSans);
                break;
            case HEADING_5 :
                this.setTextSize(COMPLEX_UNIT_SP, 14);
                this.setMinimumHeight(22);
                this.setTypeface(nunitoSans);
                break;
            case HEADING_6 :
                this.setTextSize(COMPLEX_UNIT_SP, 12);
                this.setMinimumHeight(20);
                this.setTypeface(nunitoSans);
                break;
            case BODY_1 :
                this.setTextSize(COMPLEX_UNIT_SP, 16);
                this.setMinimumHeight(22);
                configFontWeight(weightType);
                break;
            case BODY_2 :
                this.setTextSize(COMPLEX_UNIT_SP, 14);
                this.setMinimumHeight(20);
                configFontWeight(weightType);
                break;
            case BODY_3 :
                this.setTextSize(COMPLEX_UNIT_SP, 12);
                this.setMinimumHeight(18);
                configFontWeight(weightType);
                break;
            case SMALL :
                this.setTextSize(COMPLEX_UNIT_SP, 10);
                this.setMinimumHeight(16);
                configFontWeight(weightType);
                break;
        }
    }

    private void configLineHeight(TypedArray attributeArray) {
        int fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0);

        switch (fontType) {
            case HEADING_1 :
                this.setLineSpacing((float)4.0,(float)0.82);
                break;
            case HEADING_2 :
                this.setLineSpacing((float)-19.0,(float)1.25);
                break;
            case HEADING_3 :
                this.setLineSpacing((float)-17.0,(float)1.3);
                break;
            case BODY_1 :
                this.setLineSpacing((float)-4,(float)1.375);
                break;
            case BODY_2 :
                this.setLineSpacing((float)-3,(float)1.429);
                break;
            case BODY_3 :
                this.setLineSpacing((float)-5,(float)1.5);
                break;
            case SMALL :
                this.setLineSpacing((float)-5,(float)1.6);
                break;
        }
    }

    private void configFontWeight(int fontWeightState) {
        if (fontWeightState == REGULER) {
            this.setTypeface(robotoRegular);
            this.setTypeface(getTypeface(), Typeface.NORMAL);
        }else if (fontWeightState == BOLD) {
            this.setTypeface(robotoBold);
            this.setTypeface(getTypeface(), Typeface.BOLD);
        }
    }

    private void configLetterSpacing(TypedArray attributeArray) {
        int fontType = attributeArray.getInteger(R.styleable.Typography_typographyType, 0);
        float kerningValueNewVer = 0;
        float kerningValueOldVer = 0;

        if (fontType == HEADING_1) {
            kerningValueNewVer = (float)-0.013;
            kerningValueOldVer = (float)1;
        } else if (fontType == HEADING_2 || fontType == HEADING_3) {
            kerningValueNewVer = (float)-0.01;
            kerningValueOldVer = (float)1;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setLetterSpacing(kerningValueNewVer);
        } else {
            this.setTextScaleX(kerningValueOldVer);
        }
    }
}
