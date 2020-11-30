package com.tokopedia.flight.cancellation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.unifycomponents.UnifyButton;

/**
 * @author by furqan on 16/04/18.
 */

public class FlightCancellationTermsAndConditionsFragment extends BaseDaggerFragment {

    private AppCompatTextView txtTerms;
    private AppCompatTextView txtDescription;
    private UnifyButton btnSelengkapnya;
    private AppCompatCheckBox checkBox;

    public static FlightCancellationTermsAndConditionsFragment createInstance() {
        FlightCancellationTermsAndConditionsFragment fragment = new FlightCancellationTermsAndConditionsFragment();
        return fragment;
    }

    public FlightCancellationTermsAndConditionsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_cancellation_terms_and_conditions, container, false);
        txtTerms = view.findViewById(com.tokopedia.flight.R.id.txt_cancellation_tnc);
        txtDescription = view.findViewById(com.tokopedia.flight.R.id.tv_description);
        btnSelengkapnya = view.findViewById(com.tokopedia.flight.R.id.btn_next);
        checkBox = view.findViewById(com.tokopedia.flight.R.id.checkbox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnSelengkapnya.setEnabled(true);
                } else {
                    btnSelengkapnya.setEnabled(false);
                }
            }
        });

        btnSelengkapnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCancellation();
            }
        });

        setTncText();
        setDescText();

        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    private void setTncText() {
        txtTerms.setText(MethodChecker.fromHtml(getString(com.tokopedia.flight.R.string.flight_cancellation_terms_and_cancellation_text)));
    }

    private void setDescText() {
        txtDescription.setText(MethodChecker.fromHtml(getString(com.tokopedia.flight.R.string.flight_cancellation_review_description)));
    }

    private void requestCancellation() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
