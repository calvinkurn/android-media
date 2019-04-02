package com.tokopedia.flight.booking.view.fragment;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.flight.R;

public class FlightBookingNewPriceDialogFragment extends DialogFragment {
    private static final String EXTRA_NEW_PRICE = "EXTRA_NEW_PRICE";
    private static final String EXTRA_OLD_PRICE = "EXTRA_OLD_PRICE";
    private boolean isProgramaticallyDismissed = false;

    private AppCompatTextView tvOldPrice;
    private AppCompatTextView tvNewPrice;
    private AppCompatTextView tvContinueProcess;
    private AppCompatTextView tvFindAnotherFlight;

    private String newPrice, oldPrice;


    public FlightBookingNewPriceDialogFragment() {
        // Required empty public constructor
    }

    public static FlightBookingNewPriceDialogFragment newInstance(String newPrice, String oldPrice) {
        FlightBookingNewPriceDialogFragment fragment = new FlightBookingNewPriceDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_NEW_PRICE, newPrice);
        bundle.putString(EXTRA_OLD_PRICE, oldPrice);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newPrice = getArguments().getString(EXTRA_NEW_PRICE);
            oldPrice = getArguments().getString(EXTRA_OLD_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_booking_new_price_dialog, container, false);
        tvOldPrice = (AppCompatTextView) view.findViewById(R.id.tv_old_price);
        tvNewPrice = (AppCompatTextView) view.findViewById(R.id.tv_new_price);
        tvContinueProcess = (AppCompatTextView) view.findViewById(R.id.tv_continue_process);
        tvFindAnotherFlight = (AppCompatTextView) view.findViewById(R.id.tv_find_another_flight);
        tvContinueProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProgramaticallyDismissed = true;

                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_OK,
                        null);
                dismiss();
            }
        });

        tvFindAnotherFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isProgramaticallyDismissed = true;
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_CANCELED,
                        null);
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvOldPrice.setText(String.valueOf(MethodChecker.fromHtml(getString(R.string.flight_booking_new_price_strike_label, oldPrice))));
        // add strikethrough to old price
        tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvNewPrice.setText(String.valueOf(newPrice));
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void dismiss() {
        if (isProgramaticallyDismissed) {
            super.dismiss();
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_CANCELED,
                    null);

            super.dismiss();
        }
    }
}
