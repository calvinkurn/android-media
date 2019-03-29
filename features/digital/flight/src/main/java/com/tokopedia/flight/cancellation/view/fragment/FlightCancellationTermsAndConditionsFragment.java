package com.tokopedia.flight.cancellation.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.TextViewCompat;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;

import static com.tokopedia.flight.common.constant.FlightUrl.TNC_LINK;

/**
 * @author by furqan on 16/04/18.
 */

public class FlightCancellationTermsAndConditionsFragment extends BaseDaggerFragment {

    private TextViewCompat txtTerms;
    private AppCompatTextView txtDescription;
    private AppCompatButton btnSelengkapnya;
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
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_terms_and_conditions, container, false);
        txtTerms = view.findViewById(R.id.txt_cancellation_tnc);
        txtDescription = view.findViewById(R.id.tv_description);
        btnSelengkapnya = view.findViewById(R.id.btn_next);
        checkBox = view.findViewById(R.id.checkbox);

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
        txtTerms.setText(MethodChecker.fromHtml(getString(R.string.flight_cancellation_terms_and_cancellation_text)));
    }

    private void setDescText() {
        txtDescription.setText(MethodChecker.fromHtml(getString(R.string.flight_cancellation_review_description)));
    }

    private void navigateToWebview() {
        if (getActivity().getApplication() instanceof FlightModuleRouter) {
            startActivity(((FlightModuleRouter) getActivity().getApplication())
                    .getWebviewActivity(getActivity(), TNC_LINK));
        }
    }

    private void requestCancellation() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }
}
