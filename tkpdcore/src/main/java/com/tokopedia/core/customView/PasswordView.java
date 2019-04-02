package com.tokopedia.core.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.tokopedia.core2.R;

import static android.graphics.Typeface.SANS_SERIF;


/**
 * Custom version of EditText that shows and hides password onClick of the visibility icon
 */
public class PasswordView extends AppCompatEditText {

    private Drawable eye;
    private Drawable eyeWithStrike;
    private final static int VISIBILITY_ENABLED = (int) (255 * .54f); // 54%
    private final static int VISIBLITY_DISABLED = (int) (255 * .38f); // 38%
    private boolean visible = false;
    private boolean useStrikeThrough = false;
    private Typeface typeface;
    boolean isError = false;

    public PasswordView(Context context) {
        super(context);
        init(null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.PasswordView,
                    0, 0);
            try {
                useStrikeThrough = a.getBoolean(R.styleable.PasswordView_useStrikeThrough, true);
            } finally {
                a.recycle();
            }
        }

        // Make sure to mutate so that if there are multiple password fields, they can have
        // different visibilities.
        eye = ContextCompat.getDrawable(getContext(), R.drawable.ic_unify_icon_password_hidden_eye_opened).mutate();
        eyeWithStrike = ContextCompat.getDrawable(getContext(), R.drawable.ic_unify_icon_password_hidden_eye_closed).mutate();
        eyeWithStrike.setAlpha(VISIBLITY_DISABLED);
        eye.setAlpha(VISIBLITY_DISABLED);
        setup();
    }

    protected void setup() {
        setInputType(InputType.TYPE_CLASS_TEXT | (visible ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : InputType.TYPE_TEXT_VARIATION_PASSWORD));
        Log.d("steven selection", String.valueOf(getSelectionStart()) + " " + String.valueOf(getSelectionEnd()));
        setSelection(getText().length());
        Drawable drawable = useStrikeThrough && !visible ? eyeWithStrike : eye;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawable, drawables[3]);
//        eye.setAlpha(visible && !useStrikeThrough ? VISIBILITY_ENABLED : VISIBLITY_DISABLED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            final int x = (int) event.getX();
            if(getCompoundDrawables()[2] == null){
                setup();
            }
            int iconWidth = getCompoundDrawables()[2].getBounds().width();
            if (x >= (getWidth() - getPaddingLeft()) - iconWidth
                    && x <= getWidth() + iconWidth - getPaddingRight()
                    && !isError) {
                visible = !visible;
                setup();
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setInputType(int type) {
        this.typeface = Typeface.create(SANS_SERIF, Typeface.NORMAL);
        super.setInputType(type);
        setTypeface(typeface);
    }

    public void setUseStrikeThrough(boolean useStrikeThrough) {
        this.useStrikeThrough = useStrikeThrough;
    }

    public void setPasswordError(String string) {
        if (string == null) {
            isError = false;
            setError(null);
        } else {
            isError = true;
            setError(string);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        isError = false;
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
}