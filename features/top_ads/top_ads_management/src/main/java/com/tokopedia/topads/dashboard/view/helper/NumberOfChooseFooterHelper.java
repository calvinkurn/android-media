package com.tokopedia.topads.dashboard.view.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.topads.R;

import java.util.Locale;

/**
 * @author normansyahputa on 2/16/17.
 */
public class NumberOfChooseFooterHelper {

    public static final String numberOfChoosenFormat = "%d produk terpilih";
    private static final Locale locale = new Locale("in", "ID");
    private View view;
    private TextView btSelectionNumber;
    private TextView btSelectionDescription;
    private ImageView imageBtSelection;

    public NumberOfChooseFooterHelper(View view) {
        this.view = view;

        initViews();
    }

    public static String getNumberOfChoose(int numberOfChoosen) {
        return String.format(locale, numberOfChoosenFormat, numberOfChoosen);
    }

    private void initViews() {
        btSelectionNumber = (TextView) view.findViewById(R.id.bt_selection_number);
        btSelectionDescription = (TextView) view.findViewById(R.id.bt_selection_description);
        imageBtSelection = (ImageView) view.findViewById(R.id.image_bt_selection);
    }

    public void bindData(int numberOfChoosen, View.OnClickListener onExpandClickListener) {
        btSelectionNumber.setText(
                getNumberOfChoose(numberOfChoosen)
        );

        if (onExpandClickListener != null)
            view.setOnClickListener(onExpandClickListener);
    }

    public void setSelectionNumber(int numberOfChoosen) {
        btSelectionNumber.setText(
                getNumberOfChoose(numberOfChoosen)
        );
    }

    public void rotate(float degree) {
        imageBtSelection.setRotation(degree);
    }
}
