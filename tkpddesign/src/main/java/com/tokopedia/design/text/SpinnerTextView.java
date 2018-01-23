package com.tokopedia.design.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.design.utils.ConverterUtils;

/**
 * Created by nathan on 04/05/17.
 */

public class SpinnerTextView extends BaseCustomView {

    public interface OnItemChangeListener {

        void onItemChanged(int position, String entry, String value);

    }

    public static final int DEFAULT_INDEX_NOT_SELECTED = -1;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView textAutoComplete;
    private ImageView imageViewChevron;

    private String hintText;
    private
    @StyleRes
    int hintTextAppearance;
    private CharSequence[] entries;
    private CharSequence[] values;
    private int selectionIndex;
    private float textSize;
    private boolean enabled;
    private AdapterView.OnItemClickListener onItemClickListener;

    private OnItemChangeListener onItemChangeListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public SpinnerTextView(Context context) {
        super(context);
        init();
    }

    public SpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpinnerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        init();
        TypedArray styledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.SpinnerTextView);
        try {
            hintTextAppearance = styledAttributes.getResourceId(R.styleable.SpinnerTextView_spinner_hint_appearence, 0);
            hintText = styledAttributes.getString(R.styleable.SpinnerTextView_spinner_hint);
            selectionIndex = styledAttributes.getInt(R.styleable.SpinnerTextView_spinner_selection_index, 0);
            entries = styledAttributes.getTextArray(R.styleable.SpinnerTextView_spinner_entries);
            values = styledAttributes.getTextArray(R.styleable.SpinnerTextView_spinner_values);
            enabled = styledAttributes.getBoolean(R.styleable.SpinnerTextView_spinner_enabled, true);
            textSize = styledAttributes.getDimension(R.styleable.SpinnerTextView_spinner_text_size,
                    getResources().getDimension(R.dimen.font_title));
        } finally {
            styledAttributes.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (!TextUtils.isEmpty(hintText)) {
            textInputLayout.setHint(hintText);
        }
        if (entries != null) {
            updateEntries(ConverterUtils.convertCharSequenceToString(entries), selectionIndex);
        }
        if (hintTextAppearance != 0) {
            textInputLayout.setHintTextAppearance(hintTextAppearance);
        }
        textAutoComplete.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        invalidate();
        requestLayout();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.widget_spinner_text_view, this);
        textInputLayout = (TextInputLayout) view.findViewById(R.id.text_input_layout);
        textAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.edit_text_spinner);
        imageViewChevron = (ImageView) view.findViewById(R.id.image_view_chevron_icon);
        textAutoComplete.setOnKeyListener(null);
        textAutoComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AutoCompleteTextView) view).showDropDown();
            }
        });
        textAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                selectionIndex = position;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, position, id);
                }
                updateOnItemChanged(position);
            }
        });
    }

    private void resetPosition() {
        selectionIndex = DEFAULT_INDEX_NOT_SELECTED;
        textAutoComplete.setText("");
    }

    @Override
    public void setEnabled(boolean enabled) {
        textInputLayout.setEnabled(enabled);
        textAutoComplete.setEnabled(enabled);
    }

    public void setHint(String hintText) {
        textInputLayout.setHint(hintText);
        invalidate();
        requestLayout();
    }

    public void setEntries(String[] entries) {
        setEntries(entries, selectionIndex);
    }

    public void setEntries(String[] entries, int position) {
        updateEntries(entries, position);
        invalidate();
        requestLayout();
    }

    public void setValues(String[] values) {
        this.values = values;
        invalidate();
        requestLayout();
    }

    public String getSpinnerValue() {
        if (values == null || selectionIndex < 0) {
            return String.valueOf(DEFAULT_INDEX_NOT_SELECTED);
        }
        return values[selectionIndex].toString();
    }

    public String getSpinnerEntry() {
        if (values == null || selectionIndex < 0) {
            return String.valueOf(DEFAULT_INDEX_NOT_SELECTED);
        }
        return entries[selectionIndex].toString();
    }

    public String getSpinnerValue(int position) {
        return values[position].toString();
    }

    public String getSpinnerEntry(int position) {
        return entries[position].toString();
    }

    public void setSpinnerPosition(int position) {
        if (position >= 0 && position < values.length && position != selectionIndex) {
            selectionIndex = position;
            textAutoComplete.setText(entries[position]);
            updateOnItemChanged(position);
        }
    }

    public int getSpinnerPosition() {
        return selectionIndex;
    }

    private void updateOnItemChanged(final int position) {
        if (onItemChangeListener != null && entries != null && values != null) {
            onItemChangeListener.onItemChanged(position, entries[position].toString(), values[position].toString());
        }
    }

    public void setSpinnerValue(String value) {
        if (TextUtils.isEmpty(value) || values == null) {
            resetPosition();
            return;
        }
        for (int i = 0; i < values.length; i++) {
            String tempValue = values[i].toString();
            if (tempValue.equalsIgnoreCase(value)) {
                setSpinnerPosition(i);
                return;
            }
        }
        resetPosition();
    }

    public void setSpinnerValueByEntries(String entri) {
        if (TextUtils.isEmpty(entri)) {
            resetPosition();
            return;
        }
        for (int i = 0; i < entries.length; i++) {
            String tempValue = entries[i].toString();
            if (tempValue.equalsIgnoreCase(entri)) {
                setSpinnerPosition(i);
                return;
            }
        }
        resetPosition();
    }

    public void setError(String error) {
        textInputLayout.setError(error);
    }


    private void updateEntries(String[] entries, int position) {
        if (entries != null) {
            this.entries = entries;
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_autocomplete_text, entries);
            textAutoComplete.setAdapter(adapter);
            selectionIndex = position;
            if (selectionIndex >= 0) {
                textAutoComplete.setText(entries[selectionIndex]);
                updateOnItemChanged(selectionIndex);
            }
        }
    }

    public EditText getEditText() {
        return textAutoComplete;
    }

    public void setChevronVisibility(boolean isVisible) {
        imageViewChevron.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}