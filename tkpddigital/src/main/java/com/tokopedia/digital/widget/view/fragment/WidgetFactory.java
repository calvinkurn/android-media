package com.tokopedia.digital.widget.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.digital.widget.view.model.category.Category;

/**
 * Created by nabillasabbaha on 7/21/17.
 */
@Deprecated
public class WidgetFactory {

    public static final String STYLE_ONE = "style_1";
    public static final String STYLE_TWO = "style_2";
    public static final String STYLE_THREE = "style_3";
    public static final String STYLE_FOUR = "style_4";
    public static final String STYLE_FIVE = "style_5";
    public static final String STYLE_99 = "style_99";

    public static Fragment buildFragment(Category category, int position) {
        String operatorStyle = "";
        if (category.getAttributes() != null && category.getAttributes().getClientNumber() != null &&
                category.getAttributes().getClientNumber().getOperatorStyle() != null) {
            operatorStyle = category.getAttributes().getClientNumber().getOperatorStyle();
        }

        switch (operatorStyle) {
            case STYLE_ONE:
                return WidgetStyle1RechargeFragment.newInstance(category, position);
            case STYLE_99:
                return WidgetStyle99RechargeFragment.newInstance(category, position);
            case STYLE_TWO:
                return WidgetStyle2RechargeFragment.newInstance(category, position);
            case STYLE_THREE:
            case STYLE_FOUR:
            case STYLE_FIVE:
                return WidgetStyle3RechargeFragment.newInstance(category, position);
            default:
                return WidgetStyle1RechargeFragment.newInstance(category, position);
        }
    }
}
