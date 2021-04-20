package com.tokopedia.flight.airport.view.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.core.content.ContextCompat;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.airport.view.model.FlightAirportModel;
import com.tokopedia.unifyprinciples.Typography;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportViewHolder extends AbstractViewHolder<FlightAirportModel> {
    @LayoutRes
    public static int LAYOUT = com.tokopedia.flight.R.layout.item_flight_airport;

    private Typography cityTextView;
    private Typography airportTextView;

    private FlightAirportClickListener filterTextListener;
    private ForegroundColorSpan boldColor;


    public FlightAirportViewHolder(View itemView, FlightAirportClickListener filterTextListener) {
        super(itemView);
        cityTextView = itemView.findViewById(com.tokopedia.flight.R.id.city);
        airportTextView = itemView.findViewById(com.tokopedia.flight.R.id.airport);
        this.filterTextListener = filterTextListener;
        boldColor = new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(),
                com.tokopedia.unifyprinciples.R.color.Unify_N700_96));

    }

    @Override
    public void bind(final FlightAirportModel airport) {
        Context context = itemView.getContext();
        String filterText = filterTextListener.getFilterText();

        if (filterText.length() > 0) {
            cityTextView.setWeight(Typography.REGULAR);
            airportTextView.setWeight(Typography.REGULAR);
        }

        String cityStr = context.getString(com.tokopedia.flight.R.string.flight_label_city,
                airport.getCityName(), airport.getCountryName());
        cityTextView.setText(getSpandableBoldText(cityStr, filterText));

        if (!TextUtils.isEmpty(airport.getAirportCode())) {
            String airportString = context.getString(com.tokopedia.flight.R.string.flight_label_airport,
                    airport.getAirportCode(), airport.getAirportName());
            airportTextView.setText(getSpandableBoldText(airportString, filterText));
        } else {
            String airportString = context.getString(com.tokopedia.flight.R.string.flight_labe_all_airport);
            airportTextView.setText(airportString);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlightAirportViewHolder.this.filterTextListener.airportClicked(airport);
            }
        });
    }

    private CharSequence getSpandableBoldText(String strToPut, String stringToBold) {
        int indexStartBold = 0;
        int indexEndBold = strToPut.length();
        if (TextUtils.isEmpty(stringToBold)) {
            return strToPut;
        }
        String strToPutLowerCase = strToPut.toLowerCase();
        String strToBoldLowerCase = stringToBold.toLowerCase();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strToPut);
        try {
            if (strToPutLowerCase.contains(strToBoldLowerCase)) {
                indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase) + stringToBold.length();
            }
            if (indexStartBold < strToPut.length()) {
                spannableStringBuilder.setSpan(new android.text.style.StyleSpan(Typeface.BOLD),
                        indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(boldColor, indexStartBold, indexEndBold,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableStringBuilder;
            } else {
                return spannableStringBuilder;
            }
        } catch (Throwable t) {
            t.printStackTrace();
            return spannableStringBuilder;
        }
    }
}
