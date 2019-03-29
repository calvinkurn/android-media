package com.tokopedia.flight.common.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.flight.R;

/**
 * Created by User on 11/16/2017.
 */

public class DepartureArrivalHeaderView extends BaseCustomView {

    private TextView tvDepAirportCode;
    private TextView tvArrAirportCode;
    private TextView tvArrAirportName;
    private TextView tvDepAirportName;

    public DepartureArrivalHeaderView(@NonNull Context context) {
        super(context);
        init();
    }

    public DepartureArrivalHeaderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DepartureArrivalHeaderView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View view = inflate(getContext(), R.layout.include_header_flight_detail, this);
        tvDepAirportCode = (TextView) view.findViewById(R.id.departure_airport_code);
        tvDepAirportName = (TextView) view.findViewById(R.id.departure_airport_name);

        tvArrAirportCode = (TextView) view.findViewById(R.id.arrival_airport_code);
        tvArrAirportName = (TextView) view.findViewById(R.id.arrival_airport_name);
    }

    public void setDeparture(String depCode, String depName){
        tvDepAirportCode.setText(depCode);
        tvDepAirportName.setText(depName);
    }

    public void setArrival(String arrCode, String arrName){
        tvArrAirportCode.setText(arrCode);
        tvArrAirportName.setText(arrName);
    }

}
