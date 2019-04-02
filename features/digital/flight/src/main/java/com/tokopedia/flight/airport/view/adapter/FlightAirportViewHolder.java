package com.tokopedia.flight.airport.view.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.viewmodel.FlightAirportViewModel;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

public class FlightAirportViewHolder extends AbstractViewHolder<FlightAirportViewModel> {
    @LayoutRes
    public static int LAYOUT = R.layout.item_flight_airport;

    private TextView cityTextView;
    private TextView airportTextView;

    private FlightAirportClickListener filterTextListener;
    private ForegroundColorSpan boldColor;


    public FlightAirportViewHolder(View itemView, FlightAirportClickListener filterTextListener) {
        super(itemView);
        cityTextView = (TextView) itemView.findViewById(R.id.city);
        airportTextView = (TextView) itemView.findViewById(R.id.airport);
        this.filterTextListener = filterTextListener;
        boldColor = new ForegroundColorSpan(ContextCompat.getColor(itemView.getContext(), R.color.font_black_primary_70));

    }

    @Override
    public void bind(final FlightAirportViewModel airport) {
        Context context = itemView.getContext();
        String filterText = filterTextListener.getFilterText();

        String cityStr = context.getString(R.string.flight_label_city,
                airport.getCityName(), airport.getCountryName());
        cityTextView.setText(getSpandableBoldText(cityStr, filterText));

        if (!TextUtils.isEmpty(airport.getAirportCode())) {
            String airportString = context.getString(R.string.flight_label_airport,
                    airport.getAirportCode(), airport.getAirportName());
            airportTextView.setText(getSpandableBoldText(airportString, filterText));
        } else {
            String airportString = context.getString(R.string.flight_labe_all_airport);
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
        int indexStartBold = -1;
        int indexEndBold = -1;
        if (TextUtils.isEmpty(stringToBold)) {
            return strToPut;
        }
        String strToPutLowerCase = strToPut.toLowerCase();
        String strToBoldLowerCase = stringToBold.toLowerCase();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(strToPut);
        indexStartBold = strToPutLowerCase.indexOf(strToBoldLowerCase);
        if (indexStartBold != -1) {
            indexEndBold = indexStartBold + stringToBold.length();

            if (indexEndBold >= strToPut.length()) {
                indexEndBold = strToPut.length() - 1;
            }
        }
        if (indexStartBold == -1) {
            return spannableStringBuilder;
        } else {
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    indexStartBold, indexEndBold, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(boldColor, indexStartBold, indexEndBold,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannableStringBuilder;
        }

    }
}
