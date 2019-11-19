package com.tokopedia.abstraction.base.view.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokopedia.abstraction.R;

/**
 * Created by kris on 1/26/18. Tokopedia
 */

public abstract class BaseValidationDialog extends DialogFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_validation, container, false);
        TextView titleTextView = view.findViewById(R.id.title_text);
        TextView contentTextView = view.findViewById(R.id.content_text);
        TextView negativeButton = view.findViewById(R.id.negative_button);
        TextView positiveButton = view.findViewById(R.id.positive_button);
        titleTextView.setText(setTitle());
        contentTextView.setText(setContent());
        negativeButton.setText(setNegativeButtonText());
        positiveButton.setText(setPositiveButtonText());
        setButtonAttributes(negativeButton, positiveButton);
        return view;
    }

    protected abstract String setTitle();

    protected abstract String setContent();

    protected abstract String setNegativeButtonText();

    protected abstract String setPositiveButtonText();

    protected abstract View.OnClickListener setNegativeButtonClickedListener();

    protected abstract View.OnClickListener setPositiveButtonClickedListener();

    protected void setButtonAttributes(TextView negativeButton, TextView positiveButton) {
        //Override to change button attributes
    }
}
