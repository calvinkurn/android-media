package com.tokopedia.opportunity.common.showcase;

import com.tokopedia.opportunity.R;
import com.tokopedia.showcase.ShowCaseBuilder;
import com.tokopedia.showcase.ShowCaseDialog;

public class ShowCaseDialogFactory {
    public static ShowCaseDialog createTkpdShowCase (){
        return new ShowCaseBuilder()
                .customView(R.layout.item_show_case)
                .titleTextColorRes(R.color.white)
                .spacingRes(R.dimen.spacing_show_case)
                .arrowWidth(R.dimen.arrow_width_show_case)
                .textColorRes(R.color.grey_400)
                .shadowColorRes(R.color.shadow)
                .backgroundContentColorRes(R.color.black)
                .textSizeRes(R.dimen.fontvs)
                .circleIndicatorBackgroundDrawableRes(R.drawable.selector_circle_green)
                .prevStringRes(R.string.label_back)
                .nextStringRes(R.string.next)
                .finishStringRes(R.string.title_done)
                .useCircleIndicator(true)
                .clickable(true)
                .useArrow(true)
                .useSkipWord(true)
                .skipStringRes(R.string.title_skip_2)
                .build();
    }
}